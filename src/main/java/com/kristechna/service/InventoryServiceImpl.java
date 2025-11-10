package com.kristechna.service;

import com.kristechna.model.InhousePart;
import com.kristechna.model.OutsourcedPart;
import com.kristechna.model.Part;
import com.kristechna.model.Product;
import com.kristechna.repository.PartRepository;
import com.kristechna.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Part> getAllParts() {
        return partRepository.findAllByOrderByNameAsc();
    }

    @Override
    public List<Part> searchParts(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllParts();
        }
        return partRepository.findByNameContainingIgnoreCase(name.trim());
    }

    @Override
    public Optional<Part> getPartById(Long id) {
        return partRepository.findById(id);
    }

    @Override
    public Part savePart(Part part) {
        return partRepository.save(part);
    }

    @Override
    public void deletePart(Long id) {
        partRepository.deleteById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAllByOrderByNameAsc();
    }

    @Override
    public List<Product> searchProducts(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.findByNameContainingIgnoreCase(name.trim());
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean buyProduct(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (product.getInventory() > 0) {
                product.setInventory(product.getInventory() - 1);
                productRepository.save(product);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInventoryEmpty() {
        return partRepository.count() == 0 && productRepository.count() == 0;
    }

    @Override
    public boolean isPartUsedInProducts(Long partId) {
        Optional<Part> partOpt = getPartById(partId);
        if (partOpt.isPresent()) {
            Part part = partOpt.get();
            return getAllProducts().stream()
                    .anyMatch(product -> product.getParts().contains(part));
        }
        return false;
    }

    @Override
    @Transactional
    public Part createMultiPackPart(Long originalPartId, int quantity) {
        Optional<Part> originalPartOpt = getPartById(originalPartId);
        if (originalPartOpt.isEmpty()) {
            throw new IllegalArgumentException("Original part not found");
        }

        Part originalPart = originalPartOpt.get();
        String multiPackName = quantity + "x " + originalPart.getName();
        double multiPackPrice = originalPart.getPrice() * quantity;
        int multiPackInventory = originalPart.getInventory(); // Or adjust as needed

        Part multiPackPart;
        if (originalPart instanceof InhousePart inhousePart) {
            multiPackPart = new InhousePart(
                    multiPackName,
                    multiPackPrice,
                    multiPackInventory,
                    originalPart.getMinInventory(),
                    originalPart.getMaxInventory(),
                    inhousePart.getMachineId() + 1000 // Different machine ID
            );
        } else if (originalPart instanceof OutsourcedPart outsourcedPart) {
            multiPackPart = new OutsourcedPart(
                    multiPackName,
                    multiPackPrice,
                    multiPackInventory,
                    originalPart.getMinInventory(),
                    originalPart.getMaxInventory(),
                    outsourcedPart.getCompanyName()
            );
        } else {
            throw new IllegalArgumentException("Unknown part type");
        }

        return savePart(multiPackPart);
    }

    @Override
    @Transactional
    public void addSampleInventory() {
        if (isInventoryEmpty()) {
            // Create parts using Set to prevent duplicates
            Set<Part> partsSet = new HashSet<>();

            Part gpu = new InhousePart("NVIDIA RTX 4080", 1199.99, 15, 2, 25, 1001);
            Part cpu = new InhousePart("Intel Core i9-14900K", 589.99, 25, 5, 50, 1002);
            Part ram = new OutsourcedPart("Corsair 32GB DDR5", 129.99, 40, 10, 100, "Corsair");
            Part ssd = new OutsourcedPart("Samsung 2TB NVMe SSD", 199.99, 30, 5, 80, "Samsung");
            Part motherboard = new InhousePart("ASUS ROG Z790", 449.99, 18, 3, 30, 1003);

            partsSet.add(gpu);
            partsSet.add(cpu);
            partsSet.add(ram);
            partsSet.add(ssd);
            partsSet.add(motherboard);

            // Save all parts from the Set
            for (Part part : partsSet) {
                partRepository.save(part);
            }

            // Create products using Set to prevent duplicates
            Set<Product> productsSet = new HashSet<>();

            Product gamingPc1 = new Product("KrisTechna Pro Gaming PC", 2499.99, 8);
            gamingPc1.addPart(gpu);
            gamingPc1.addPart(cpu);
            gamingPc1.addPart(ram);
            gamingPc1.addPart(ssd);
            gamingPc1.addPart(motherboard);

            Product gamingPc2 = new Product("KrisTechna Elite Gaming Rig", 1899.99, 12);
            gamingPc2.addPart(cpu);
            gamingPc2.addPart(ram);
            gamingPc2.addPart(ssd);
            gamingPc2.addPart(motherboard);

            Product gamingLaptop = new Product("KrisTechna Gaming Laptop", 1599.99, 6);
            Product vrReadyPc = new Product("KrisTechna VR Ready System", 1299.99, 10);
            Product streamingPc = new Product("KrisTechna Streaming Setup", 999.99, 15);

            productsSet.add(gamingPc1);
            productsSet.add(gamingPc2);
            productsSet.add(gamingLaptop);
            productsSet.add(vrReadyPc);
            productsSet.add(streamingPc);

            // Save all products from the Set
            for (Product product : productsSet) {
                productRepository.save(product);
            }
        }
    }
}
