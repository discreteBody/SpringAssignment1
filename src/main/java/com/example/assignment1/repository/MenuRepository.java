package com.example.assignment1.repository;

// ======================== DO NOT MODIFY THIS FILE ========================

import com.example.assignment1.model.MealType;
import com.example.assignment1.model.Menu;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory repository for Menu entities.
 * Uses a HashMap to store menus, simulating a database.
 * Provides various query methods to filter menus by restaurant, date, and meal type.
 *
 * This class is fully implemented — do NOT modify it.
 */
@Repository
public class MenuRepository {

    private final Map<Long, Menu> menus = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Save a menu. If it has no ID, one will be generated automatically.
     */
    public Menu save(Menu menu) {
        if (menu.getId() == null) {
            menu.setId(idGenerator.getAndIncrement());
        }
        menus.put(menu.getId(), menu);
        return menu;
    }

    /**
     * Find a menu by its ID.
     */
    public Optional<Menu> findById(Long id) {
        return Optional.ofNullable(menus.get(id));
    }

    /**
     * Get all menus.
     */
    public List<Menu> findAll() {
        return new ArrayList<>(menus.values());
    }

    /**
     * Find all menus for a specific restaurant.
     */
    public List<Menu> findByRestaurantId(Long restaurantId) {
        return menus.values().stream()
                .filter(menu -> menu.getRestaurantId().equals(restaurantId))
                .collect(Collectors.toList());
    }

    /**
     * Find all menus for a specific date.
     */
    public List<Menu> findByDate(LocalDate date) {
        return menus.values().stream()
                .filter(menu -> menu.getDate().equals(date))
                .collect(Collectors.toList());
    }

    /**
     * Find all menus of a specific meal type.
     */
    public List<Menu> findByMealType(MealType mealType) {
        return menus.values().stream()
                .filter(menu -> menu.getMealType().equals(mealType))
                .collect(Collectors.toList());
    }

    /**
     * Find all menus for a specific restaurant on a specific date.
     * This typically returns up to 3 menus (breakfast, lunch, dinner).
     */
    public List<Menu> findByRestaurantIdAndDate(Long restaurantId, LocalDate date) {
        return menus.values().stream()
                .filter(menu -> menu.getRestaurantId().equals(restaurantId)
                        && menu.getDate().equals(date))
                .collect(Collectors.toList());
    }

    /**
     * Find a specific menu for a restaurant, date, and meal type combination.
     * Since this combination must be unique, this returns at most one menu.
     */
    public Optional<Menu> findByRestaurantIdAndDateAndMealType(Long restaurantId, LocalDate date, MealType mealType) {
        return menus.values().stream()
                .filter(menu -> menu.getRestaurantId().equals(restaurantId)
                        && menu.getDate().equals(date)
                        && menu.getMealType().equals(mealType))
                .findFirst();
    }

    /**
     * Check if a menu with the given ID exists.
     */
    public boolean existsById(Long id) {
        return menus.containsKey(id);
    }

    /**
     * Delete a menu by its ID.
     * @return true if found and deleted, false otherwise.
     */
    public boolean deleteById(Long id) {
        return menus.remove(id) != null;
    }
}
