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
public class PartController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/add-inhouse-part")
    public String showAddInhousePartForm(Model model) {
        model.addAttribute("part", new InhousePart());
        return "inhouse-part-form";
    }

    @GetMapping("/add-outsourced-part")
    public String showAddOutsourcedPartForm(Model model) {
        model.addAttribute("part", new OutsourcedPart());
        return "outsourced-part-form";
    }

    @GetMapping("/create-multi-pack-manual")
    public String createMultiPackPartManual(@RequestParam Long originalPartId,
                                            @RequestParam int quantity,
                                            Model model) {
        try {
            Part multiPackPart = inventoryService.createMultiPackPart(originalPartId, quantity);
            model.addAttribute("message", "Multi-pack part created: " + multiPackPart.getName());
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create multi-pack part: " + e.getMessage());
        }

        model.addAttribute("parts", inventoryService.getAllParts());
        model.addAttribute("products", inventoryService.getAllProducts());
        return "main";
    }

    @GetMapping("/create-multi-pack/{partId}/{quantity}")
    public String createMultiPackPart(@PathVariable Long partId, @PathVariable int quantity, Model model) {
        try {
            Part multiPackPart = inventoryService.createMultiPackPart(partId, quantity);
            model.addAttribute("message", "Multi-pack part created: " + multiPackPart.getName());
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create multi-pack part: " + e.getMessage());
        }

        model.addAttribute("parts", inventoryService.getAllParts());
        model.addAttribute("products", inventoryService.getAllProducts());
        return "main";
    }

    @PostMapping("/save-inhouse-part")
    public String saveInhousePart(@Valid @ModelAttribute("part") InhousePart part,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "inhouse-part-form";
        }

        // Inventory validation between min and max
        if (part.getInventory() < part.getMinInventory()) {
            result.rejectValue("inventory", "error.part",
                    "Inventory cannot be less than minimum inventory: " + part.getMinInventory());
            return "inhouse-part-form";
        }

        if (part.getInventory() > part.getMaxInventory()) {
            result.rejectValue("inventory", "error.part",
                    "Inventory cannot exceed maximum inventory: " + part.getMaxInventory());
            return "inhouse-part-form";
        }

        inventoryService.savePart(part);
        return "redirect:/mainscreen";
    }

    @PostMapping("/save-outsourced-part")
    public String saveOutsourcedPart(@Valid @ModelAttribute("part") OutsourcedPart part,
                                     BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "outsourced-part-form";
        }

        // Inventory validation between min and max
        if (part.getInventory() < part.getMinInventory()) {
            result.rejectValue("inventory", "error.part",
                    "Inventory cannot be less than minimum inventory: " + part.getMinInventory());
            return "outsourced-part-form";
        }

        if (part.getInventory() > part.getMaxInventory()) {
            result.rejectValue("inventory", "error.part",
                    "Inventory cannot exceed maximum inventory: " + part.getMaxInventory());
            return "outsourced-part-form";
        }

        inventoryService.savePart(part);
        return "redirect:/mainscreen";
    }

    @GetMapping("/update-part/{id}")
    public String showUpdatePartForm(@PathVariable Long id, Model model) {
        Optional<Part> partOpt = inventoryService.getPartById(id);
        if (partOpt.isPresent()) {
            Part part = partOpt.get();
            if (part instanceof InhousePart inhousePart) {
                model.addAttribute("part", inhousePart);
                return "inhouse-part-form";
            } else if (part instanceof OutsourcedPart outsourcedPart) {
                model.addAttribute("part", outsourcedPart);
                return "outsourced-part-form";
            }
        }
        return "redirect:/mainscreen";
    }

    @GetMapping("/delete-part/{id}")
    public String deletePart(@PathVariable Long id, Model model) {
        try {
            // Use the service method to check if part is used
            if (inventoryService.isPartUsedInProducts(id)) {
                model.addAttribute("error", "Cannot delete part: It is used in one or more products. You can set its inventory to 0 instead.");
            } else {
                inventoryService.deletePart(id);
                model.addAttribute("message", "Part deleted successfully!");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Cannot delete part: It may be used in products. You can set its inventory to 0 instead.");
        }

        model.addAttribute("parts", inventoryService.getAllParts());
        model.addAttribute("products", inventoryService.getAllProducts());
        return "main";
    }
}