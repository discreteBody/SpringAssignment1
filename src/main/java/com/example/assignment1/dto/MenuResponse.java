package com.example.assignment1.dto;

// ======================== DO NOT MODIFY THIS FILE ========================

import com.example.assignment1.model.MealType;
import com.example.assignment1.model.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Response DTO for a Menu. Enriched with restaurant details and full menu item objects.
 * Instead of returning raw IDs, this provides a user-friendly view of the menu.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponse {
    private Long id;
    private String restaurantName;
    private String restaurantLocation;
    private LocalDate date;
    private MealType mealType;
    private List<MenuItem> items;
}
