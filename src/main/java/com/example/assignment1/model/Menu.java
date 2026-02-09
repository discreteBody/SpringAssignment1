package com.example.assignment1.model;

// ======================== DO NOT MODIFY THIS FILE ========================

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a menu for a specific restaurant, date, and meal type.
 * A menu ties together a restaurant, a date, a meal type (breakfast/lunch/dinner),
 * and a list of menu item IDs that are available.
 *
 * CONSTRAINT: There can only be ONE menu per (restaurantId + date + mealType) combination.
 * For example, "Cafeteria A" can have only one BREAKFAST menu on 2026-02-10.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    private Long id;
    private Long restaurantId;
    private LocalDate date;
    private MealType mealType;
    private List<Long> menuItemIds;
}
