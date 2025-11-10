package com.kristechna.service;

import com.kristechna.model.Part;
import com.kristechna.model.Product;

import java.util.List;
import java.util.Optional;

public interface InventoryService {

    // Part methods:

    List<Part> getAllParts();

    List<Part> searchParts(String name);

    Optional<Part> getPartById(Long id);

    Part savePart(Part part);

    void deletePart(Long id);

    // Product methods:

    List<Product> getAllProducts();

    List<Product> searchProducts(String name);

    Optional<Product> getProductById(Long id);

    Product saveProduct(Product product);

    void deleteProduct(Long id);

    // Business logic

    Part createMultiPackPart(Long originalPartId, int quantity);

    boolean buyProduct(Long productId);

    void addSampleInventory();

    boolean isInventoryEmpty();

    boolean isPartUsedInProducts(Long partId);
}
