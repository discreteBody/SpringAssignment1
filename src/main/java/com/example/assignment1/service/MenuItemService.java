package com.example.assignment1.service;

// ╔══════════════════════════════════════════════════════════════════╗
// ║            ✏️  YOUR TASK: IMPLEMENT THIS FILE                   ║
// ║  Fill in the method bodies below. Do NOT change signatures.     ║
// ╚══════════════════════════════════════════════════════════════════╝

import com.example.assignment1.dto.MenuItemRequest;
import com.example.assignment1.exception.InvalidRequestException;
import com.example.assignment1.exception.ResourceNotFoundException;
import com.example.assignment1.model.MenuItem;
import com.example.assignment1.repository.MenuItemRepository;
import com.example.assignment1.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;

    public MenuItemService(MenuItemRepository menuItemRepository, MenuRepository menuRepository) {
        this.menuItemRepository = menuItemRepository;
        this.menuRepository = menuRepository;
    }

    /**
     * TODO: Create a new menu item.
     *
     * Steps:
     *   1. Validate that the name is not null or blank.
     *      If invalid → throw InvalidRequestException("Menu item name cannot be empty")
     *   2. Validate that the price is greater than 0.
     *      If invalid → throw InvalidRequestException("Menu item price must be greater than 0")
     *   3. Validate that the category is not null.
     *      If invalid → throw InvalidRequestException("Menu item category cannot be null")
     *   4. Create a new MenuItem object from the request fields (id should be null).
     *   5. Save using the repository and return the saved menu item.
     */
    public MenuItem createMenuItem(MenuItemRequest request) {
        // TODO: Implement this method
        if(request.getName() == null || request.getName().equals("")) {
            throw new InvalidRequestException("Menu item name cannot be empty");
        }

        if(request.getPrice() <= 0) {
            throw new InvalidRequestException("Menu item price must be greater than 0");
        }

        if(request.getCategory() == null || request.getCategory().equals("")) {
            throw new InvalidRequestException("Menu item Category cannot be empty");
        }

        MenuItem menuItem = new MenuItem();
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setVegetarian(request.isVegetarian());
        menuItem.setCategory(request.getCategory());
        menuItemRepository.save(menuItem);

        return menuItem;
    }

    /**
     * TODO: Get a menu item by its ID.
     *
     * Steps:
     *   1. Look up the menu item in the repository using findById.
     *   2. If not found → throw ResourceNotFoundException("Menu item not found with id: " + id)
     *   3. Return the found menu item.
     */
    public MenuItem getMenuItemById(Long id) {
        // TODO: Implement this method
        return menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
    }

    /**
     * TODO: Get all menu items.
     *
     * @return list of all menu items (empty list if none exist)
     */
    public List<MenuItem> getAllMenuItems() {
        // TODO: Implement this method
        return menuItemRepository.findAll();
    }

    /**
     * TODO: Update an existing menu item.
     *
     * Steps:
     *   1. Fetch the existing menu item using getMenuItemById (will throw if not found).
     *   2. Validate the request fields (same validations as create):
     *      - name not null/blank
     *      - price > 0
     *      - category not null
     *   3. Update all fields of the existing menu item with values from the request:
     *      name, description, price, vegetarian, category.
     *   4. Save and return the updated menu item.
     */
    public MenuItem updateMenuItem(Long id, MenuItemRequest request) {
        // TODO: Implement this method
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        if(request.getName() == null || request.getName().equals("")) {
            throw new InvalidRequestException("Menu item name cannot be empty");
        }

        if(request.getPrice() <= 0) {
            throw new InvalidRequestException("Menu item price must be greater than 0");
        }

        if(request.getCategory() == null || request.getCategory().equals("")) {
            throw new InvalidRequestException("Menu item Category cannot be empty");
        }

        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setVegetarian(request.isVegetarian());
        menuItem.setCategory(request.getCategory());

        menuItemRepository.save(menuItem);

        return menuItem;
    }

    /**
     * TODO: Delete a menu item by ID.
     *
     * Steps:
     *   1. Verify the menu item exists using getMenuItemById (will throw if not found).
     *   2. Delete it from the repository using deleteById.
     */
    public void deleteMenuItem(Long id) {
        // TODO: Implement this method
        menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        menuItemRepository.deleteById(id);
    }
}
