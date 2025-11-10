package com.kristechna.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("INHOUSE")
public class InhousePart extends Part {
    private int machineId;

    public InhousePart() {
    }

    public InhousePart(String name, double price, int inventory, int minInventory,
                       int maxInventory, int machineId) {
        super(name, price, inventory, minInventory, maxInventory);
        this.machineId = machineId;
    }

    // Accessors & Mutators:

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}