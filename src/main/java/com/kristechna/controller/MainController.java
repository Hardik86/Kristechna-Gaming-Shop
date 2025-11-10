package com.kristechna.controller;

import com.kristechna.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/mainscreen";
    }

    @GetMapping("/mainscreen")
    public String mainScreen(@RequestParam(required = false) String partSearch,
                             @RequestParam(required = false) String productSearch,
                             Model model) {

        // Load sample inventory if only empty:

        if (inventoryService.isInventoryEmpty()) {
            inventoryService.addSampleInventory();
        }

        // Handle part search:

        if (partSearch != null && !partSearch.trim().isEmpty()) {
            model.addAttribute("parts", inventoryService.searchParts(partSearch.trim()));
            model.addAttribute("partSearch", partSearch.trim());
        } else {
            model.addAttribute("parts", inventoryService.getAllParts());
            model.addAttribute("partSearch", "");
        }

        // Handel Prodcut search:

        if (productSearch != null && !productSearch.trim().isEmpty()) {
            model.addAttribute("products", inventoryService.searchProducts(productSearch.trim()));
            model.addAttribute("productSearch", productSearch.trim());
        } else {
            model.addAttribute("products", inventoryService.getAllProducts());
            model.addAttribute("productSearch", "");
        }

        return "main";
    }

    @GetMapping("/about")
    public String aboutPage(Model model) {
        return "about";
    }
}
