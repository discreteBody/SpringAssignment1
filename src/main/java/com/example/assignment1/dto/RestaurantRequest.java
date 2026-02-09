package com.example.assignment1.dto;

// ======================== DO NOT MODIFY THIS FILE ========================

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or updating a Restaurant.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRequest {
    private String name;
    private String location;
}
