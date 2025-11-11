package com.kristechna;

import com.kristechna.model.InhousePart;
import com.kristechna.model.OutsourcedPart;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PartTest {

    @Test
    public void testInhousePartInventoryValidationWithinRange() {
        // Test valid inventory within min/max range

        InhousePart part = new InhousePart("Test GPU", 299.99, 15, 5, 25, 1001);

        // Test boundaries:

        part.setInventory(5);
        assertEquals(5, part.getInventory());
        assertTrue(part.getInventory() >= part.getMinInventory(),
                "Inventory should be greater than or equal to minimum");

        part.setInventory(25);
        assertEquals(25, part.getInventory());
        assertTrue(part.getInventory() <= part.getMaxInventory(),
                "Inventory should be less than or equal to maximum");


        part.setInventory(15);
        assertTrue(part.getInventory() >= part.getMinInventory() &&
                        part.getInventory() <= part.getMaxInventory(),
                "Inventory should be within min/max range");
    }

    @Test
    public void testOutsourcedPartMinMaxInventoryConstraints() {
        // Test min/max constraints for outsourced parts:

        OutsourcedPart part = new OutsourcedPart("Test RAM", 89.99,
                50, 10, 100, "Corsair");


        assertEquals(10, part.getMinInventory());
        assertEquals(100, part.getMaxInventory());
        assertEquals(50, part.getInventory());

        // Test that min cannot be greater than max (business logic validation):

        part.setMinInventory(20);
        part.setMaxInventory(80);
        assertTrue(part.getMinInventory() <= part.getMaxInventory(),
                "Minimum inventory should not exceed maximum inventory");

        // Test inventory stays within bounds:

        part.setInventory(20);
        assertEquals(20, part.getInventory());
        assertTrue(part.getInventory() >= part.getMinInventory());

        part.setInventory(80);
        assertEquals(80, part.getInventory());
        assertTrue(part.getInventory() <= part.getMaxInventory());
    }
}