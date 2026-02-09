package com.example.assignment1.dto;

// ======================== DO NOT MODIFY THIS FILE ========================

import com.example.assignment1.model.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Request DTO for creating or updating a Menu.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuRequest {
    private Long restaurantId;
    private LocalDate date;
    private MealType mealType;
    private List<Long> menuItemIds;
}
