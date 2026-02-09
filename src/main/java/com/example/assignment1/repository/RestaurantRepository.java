package com.example.assignment1.repository;

// ======================== DO NOT MODIFY THIS FILE ========================

import com.example.assignment1.model.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory repository for Restaurant entities.
 * Uses a HashMap to store restaurants, simulating a database.
 *
 * This class is fully implemented — do NOT modify it.
 */
@Repository
public class RestaurantRepository {

    private final Map<Long, Restaurant> restaurants = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Save a restaurant. If the restaurant has no ID, one will be generated automatically.
     * If it already has an ID, it will be updated (overwritten) in the store.
     */
    public Restaurant save(Restaurant restaurant) {
        if (restaurant.getId() == null) {
            restaurant.setId(idGenerator.getAndIncrement());
        }
        restaurants.put(restaurant.getId(), restaurant);
        return restaurant;
    }

    /**
     * Find a restaurant by its ID.
     * @return Optional containing the restaurant if found, empty otherwise.
     */
    public Optional<Restaurant> findById(Long id) {
        return Optional.ofNullable(restaurants.get(id));
    }

    /**
     * Get all restaurants.
     * @return List of all restaurants (empty list if none exist).
     */
    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants.values());
    }

    /**
     * Check if a restaurant with the given ID exists.
     */
    public boolean existsById(Long id) {
        return restaurants.containsKey(id);
    }

    /**
     * Delete a restaurant by its ID.
     * @return true if the restaurant was found and deleted, false otherwise.
     */
    public boolean deleteById(Long id) {
        return restaurants.remove(id) != null;
    }
}
