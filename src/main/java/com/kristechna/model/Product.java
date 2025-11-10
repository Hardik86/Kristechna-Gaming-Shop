package com.kristechna.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private double price;

    @Min(value = 0, message = "Inventory cannot be negative")
    private int inventory;

    @ManyToMany
    @JoinTable(
            name = "product_parts",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "part_id")
    )
    private Set<Part> parts = new HashSet<>();

    public Product() {
    }

    public Product(String name, double price, int inventory) {
        this.name = name;
        this.price = price;
        this.inventory = inventory;
    }

    // Accessors & Mutators:

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public Set<Part> getParts() {
        return parts;
    }

    public void setParts(Set<Part> parts) {
        this.parts = parts;
    }

    public void addPart(Part part) {
        this.parts.add(part);
    }

    public void removePart(Part part) {
        this.parts.remove(part);
    }
}
