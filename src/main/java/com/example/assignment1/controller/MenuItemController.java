package com.example.assignment1.controller;

// ╔══════════════════════════════════════════════════════════════════╗
// ║            ✏️  YOUR TASK: IMPLEMENT THIS FILE                   ║
// ║  Fill in the method bodies below. Do NOT change signatures.     ║
// ╚══════════════════════════════════════════════════════════════════╝

import com.example.assignment1.dto.MenuItemRequest;
import com.example.assignment1.model.MenuItem;
import com.example.assignment1.service.MenuItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    /**
     * TODO: POST /api/menu-items
     * Create a new menu item.
     *
     * - Return HTTP 201 (Created) with the created MenuItem.
     */
    @PostMapping
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItemRequest request) {
        // TODO: Implement this method
        return ResponseEntity.ok().body(menuItemService.createMenuItem(request));
    }

    /**
     * TODO: GET /api/menu-items
     * Get all menu items.
     *
     * - Return HTTP 200 with the list of all menu items.
     */
    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        // TODO: Implement this method
        return ResponseEntity.ok().body(menuItemService.getAllMenuItems());
    }

    /**
     * TODO: GET /api/menu-items/{id}
     * Get a menu item by ID.
     *
     * - Return HTTP 200 with the menu item.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        // TODO: Implement this method
        return ResponseEntity.ok().body(menuItemService.getMenuItemById(id));
    }

    /**
     * TODO: PUT /api/menu-items/{id}
     * Update an existing menu item.
     *
     * - Return HTTP 200 with the updated menu item.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id,
                                                   @RequestBody MenuItemRequest request) {
        // TODO: Implement this method
        return ResponseEntity.ok().body(menuItemService.updateMenuItem(id,request));
    }

    /**
     * TODO: DELETE /api/menu-items/{id}
     * Delete a menu item.
     *
     * - Return HTTP 204 (No Content) with an empty body.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        // TODO: Implement this method
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.ok().build();
    }
}
