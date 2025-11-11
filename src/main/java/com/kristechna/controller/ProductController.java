package com.kristechna.controller;

import com.kristechna.model.*;
import com.kristechna.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/add-product")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("availableParts", inventoryService.getAllParts());
        return "product-form";
    }

    @PostMapping("/save-product")
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("availableParts", inventoryService.getAllParts());
            return "product-form";
        }

        inventoryService.saveProduct(product);
        return "redirect:/mainscreen";
    }

    @GetMapping("/update-product/{id}")
    public String showUpdateProductForm(@PathVariable Long id, Model model) {
        Optional<Product> productOpt = inventoryService.getProductById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            model.addAttribute("product", product);
            model.addAttribute("availableParts", inventoryService.getAllParts());
            model.addAttribute("associatedParts", product.getParts());
            return "product-form";
        }
        return "redirect:/mainscreen";
    }

    @GetMapping("/add-part-to-product/{productId}/{partId}")
    public String addPartToProduct(@PathVariable Long productId, @PathVariable Long partId, Model model) {
        Optional<Product> productOpt = inventoryService.getProductById(productId);
        Optional<Part> partOpt = inventoryService.getPartById(partId);

        if (productOpt.isPresent() && partOpt.isPresent()) {
            Product product = productOpt.get();
            Part part = partOpt.get();

            // Check if part is already added (prevent duplicates)
            if (product.getParts().contains(part)) {
                model.addAttribute("errorHtml", "Part '" + part.getName() + "' already added. " +
                        "<a href='/create-multi-pack/" + part.getId() + "/2' style='color: blue; text-decoration: underline;'>" +
                        "Click here to create a 2-pack of this part</a> or use the form below to create a custom multi-pack.");
                model.addAttribute("originalPartId", part.getId());
                model.addAttribute("product", product);
                model.addAttribute("availableParts", inventoryService.getAllParts());
                model.addAttribute("associatedParts", product.getParts());
                return "product-form";
            }

            // Calculate new total parts price
            double currentPartsTotal = product.getParts().stream()
                    .mapToDouble(Part::getPrice)
                    .sum();
            double newTotal = currentPartsTotal + part.getPrice();

            // Validate product price >= parts total
            if (newTotal > product.getPrice()) {
                model.addAttribute("error", "Cannot add part: Product price $" + product.getPrice() +
                        " would be less than parts total $" + String.format("%.2f", newTotal));

                model.addAttribute("product", product);
                model.addAttribute("availableParts", inventoryService.getAllParts());
                model.addAttribute("associatedParts", product.getParts());
                return "product-form";
            }

            product.addPart(part);
            inventoryService.saveProduct(product);
        }

        return "redirect:/update-product/" + productId;
    }

    @GetMapping("/remove-part-from-product/{productId}/{partId}")
    public String removePartFromProduct(@PathVariable Long productId, @PathVariable Long partId) {
        Optional<Product> productOpt = inventoryService.getProductById(productId);
        Optional<Part> partOpt = inventoryService.getPartById(partId);

        if (productOpt.isPresent() && partOpt.isPresent()) {
            Product product = productOpt.get();
            Part part = partOpt.get();
            product.removePart(part);
            inventoryService.saveProduct(product);
        }

        return "redirect:/update-product/" + productId;
    }

    @PostMapping("/update-product-final")
    public String updateProductFinal(@Valid @ModelAttribute("product") Product product,
                                     BindingResult result, Model model) {
        if (result.hasErrors()) {
            Optional<Product> productOpt = inventoryService.getProductById(product.getId());
            if (productOpt.isPresent()) {
                Product existingProduct = productOpt.get();
                model.addAttribute("availableParts", inventoryService.getAllParts());
                model.addAttribute("associatedParts", existingProduct.getParts());
            }
            return "product-form";
        }

        Optional<Product> productOpt = inventoryService.getProductById(product.getId());
        if (productOpt.isPresent()) {
            Product existingProduct = productOpt.get();
            int oldInventory = existingProduct.getInventory();
            int newInventory = product.getInventory();

            // Price validation: product price must be >= sum of part prices
            double partsTotalPrice = existingProduct.getParts().stream()
                    .mapToDouble(Part::getPrice)
                    .sum();
            if (product.getPrice() < partsTotalPrice) {
                result.rejectValue("price", "error.product",
                        "Price of the product must be greater than or equal to the sum of the parts: $" +
                                String.format("%.2f", partsTotalPrice));
                model.addAttribute("availableParts", inventoryService.getAllParts());
                model.addAttribute("associatedParts", existingProduct.getParts());
                return "product-form";
            }

            // Handle inventory changes for parts (only when increasing inventory)
            if (newInventory > oldInventory) {
                int additionalProducts = newInventory - oldInventory;

                // Check if enough parts available
                for (Part part : existingProduct.getParts()) {
                    int newPartInventory = part.getInventory() - additionalProducts;
                    if (newPartInventory < part.getMinInventory()) {
                        result.rejectValue("inventory", "error.product",
                                "Not enough parts in inventory for: " + part.getName() +
                                        ". Available: " + part.getInventory() + ", Required: " + additionalProducts +
                                        ", Min allowed: " + part.getMinInventory());
                        model.addAttribute("availableParts", inventoryService.getAllParts());
                        model.addAttribute("associatedParts", existingProduct.getParts());
                        return "product-form";
                    }
                }

                // Update parts inventory if validation passes
                for (Part part : existingProduct.getParts()) {
                    int newPartInventory = part.getInventory() - additionalProducts;
                    part.setInventory(newPartInventory);
                    inventoryService.savePart(part);
                }
            }

            // Update product details

            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setInventory(newInventory);
            inventoryService.saveProduct(existingProduct);
            model.addAttribute("message", "Product updated successfully!");
        }

        model.addAttribute("parts", inventoryService.getAllParts());
        model.addAttribute("products", inventoryService.getAllProducts());
        return "main";
    }

    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable Long id) {
        inventoryService.deleteProduct(id);
        return "redirect:/mainscreen";
    }

    @GetMapping("/buy-product/{id}")
    public String buyProduct(@PathVariable Long id, Model model) {
        boolean success = inventoryService.buyProduct(id);

        if (success) {
            model.addAttribute("message", "Product purchased successfully!");
        } else {
            model.addAttribute("error", "Product out of stock! Cannot complete purchase.");
        }

        model.addAttribute("parts", inventoryService.getAllParts());
        model.addAttribute("products", inventoryService.getAllProducts());
        return "main";
    }
}