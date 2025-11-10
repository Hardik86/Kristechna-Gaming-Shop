package com.kristechna.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("OUTSOURCED")
public class OutsourcedPart extends Part {
    private String companyName;

    public OutsourcedPart() {
    }

    public OutsourcedPart(String name, double price,
                          int inventory, int minInventory, int maxInventory, String companyName)
                                                                                                {
        super(name, price, inventory, minInventory, maxInventory);
        this.companyName = companyName;
    }

    // Accessors & Mutators:

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}