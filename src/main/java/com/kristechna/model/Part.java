package com.kristechna.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "part_type")
public abstract class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private double price;

    @Min(value = 0, message = "Inventory cannot be negative")
    private int inventory;

    @Min(value = 0, message = "Min inventory cannot be negative")
    private int minInventory;

    @Min(value = 1, message = "Max inventory must be at least 1")
    private int maxInventory;

    public Part() {
    }

    public Part(String name, double price, int inventory,
                int minInventory, int maxInventory) {
        this.name = name;
        this.price = price;
        this.inventory = inventory;
        this.minInventory = minInventory;
        this.maxInventory = maxInventory;
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

    public int getMinInventory() {
        return minInventory;
    }

    public void setMinInventory(int minInventory) {
        this.minInventory = minInventory;
    }

    public int getMaxInventory() {
        return maxInventory;
    }

    public void setMaxInventory(int maxInventory) {
        this.maxInventory = maxInventory;
    }
}
