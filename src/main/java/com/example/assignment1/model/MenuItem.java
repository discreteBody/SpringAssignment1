package com.example.assignment1.model;

// ======================== DO NOT MODIFY THIS FILE ========================

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single food/drink item that can appear on a menu.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {
    private Long id;
    private String name;
    private String description;
    private double price;
    private boolean vegetarian;
    private ItemCategory category;
}
