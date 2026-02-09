package com.example.assignment1.repository;

// ======================== DO NOT MODIFY THIS FILE ========================

import com.example.assignment1.model.MenuItem;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory repository for MenuItem entities.
 * Uses a HashMap to store menu items, simulating a database.
 *
 * This class is fully implemented — do NOT modify it.
 */
@Repository
public class MenuItemRepository {

    private final Map<Long, MenuItem> menuItems = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Save a menu item. If it has no ID, one will be generated automatically.
     */
    public MenuItem save(MenuItem menuItem) {
        if (menuItem.getId() == null) {
            menuItem.setId(idGenerator.getAndIncrement());
        }
        menuItems.put(menuItem.getId(), menuItem);
        return menuItem;
    }

    /**
     * Find a menu item by its ID.
     */
    public Optional<MenuItem> findById(Long id) {
        return Optional.ofNullable(menuItems.get(id));
    }

    /**
     * Get all menu items.
     */
    public List<MenuItem> findAll() {
        return new ArrayList<>(menuItems.values());
    }

    /**
     * Find multiple menu items by their IDs.
     * Only returns items that actually exist (silently skips missing IDs).
     */
    public List<MenuItem> findAllByIds(List<Long> ids) {
        return ids.stream()
                .map(menuItems::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Check if a menu item with the given ID exists.
     */
    public boolean existsById(Long id) {
        return menuItems.containsKey(id);
    }

    /**
     * Delete a menu item by its ID.
     * @return true if found and deleted, false otherwise.
     */
    public boolean deleteById(Long id) {
        return menuItems.remove(id) != null;
    }
}
