package com.example.assignment1.dto;

// ======================== DO NOT MODIFY THIS FILE ========================

import com.example.assignment1.model.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or updating a MenuItem.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemRequest {
    private String name;
    private String description;
    private double price;
    private boolean vegetarian;
    private ItemCategory category;
}
