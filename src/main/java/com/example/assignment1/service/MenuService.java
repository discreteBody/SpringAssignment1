package com.example.assignment1.service;

// ╔══════════════════════════════════════════════════════════════════╗
// ║            ✏️  YOUR TASK: IMPLEMENT THIS FILE                   ║
// ║  Fill in the method bodies below. Do NOT change signatures.     ║
// ║  The helper method toMenuResponse() is already done for you.    ║
// ╚══════════════════════════════════════════════════════════════════╝

import com.example.assignment1.dto.MenuRequest;
import com.example.assignment1.dto.MenuResponse;
import com.example.assignment1.exception.DuplicateResourceException;
import com.example.assignment1.exception.InvalidRequestException;
import com.example.assignment1.exception.ResourceNotFoundException;
import com.example.assignment1.model.Menu;
import com.example.assignment1.model.MenuItem;
import com.example.assignment1.model.Restaurant;
import com.example.assignment1.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;

    public MenuService(MenuRepository menuRepository,
                       RestaurantService restaurantService,
                       MenuItemService menuItemService) {
        this.menuRepository = menuRepository;
        this.restaurantService = restaurantService;
        this.menuItemService = menuItemService;
    }

    /**
     * TODO: Create a new menu.
     *
     * This is the most interesting method — it involves cross-service validation!
     *
     * Steps:
     *   1. Validate that restaurantId is not null.
     *      → throw InvalidRequestException("Restaurant ID cannot be null")
     *   2. Validate that date is not null.
     *      → throw InvalidRequestException("Menu date cannot be null")
     *   3. Validate that mealType is not null.
     *      → throw InvalidRequestException("Meal type cannot be null")
     *   4. Validate that menuItemIds is not null and not empty.
     *      → throw InvalidRequestException("Menu must contain at least one item")
     *   5. Verify the restaurant exists by calling restaurantService.getRestaurantById().
     *      (It will throw ResourceNotFoundException automatically if not found.)
     *   6. Verify ALL menu item IDs are valid by calling menuItemService.getMenuItemById()
     *      for each ID in the list. (Each call throws if the item doesn't exist.)
     *   7. Check for duplicate: use menuRepository.findByRestaurantIdAndDateAndMealType().
     *      If a menu already exists for this combination → throw DuplicateResourceException(
     *        "Menu already exists for restaurant " + restaurantId + " on " + date + " for " + mealType)
     *   8. Create a new Menu object (id = null, set all fields from request).
     *   9. Save it using the repository.
     *  10. Convert the saved menu to a MenuResponse using the toMenuResponse() helper and return it.
     */
    public MenuResponse createMenu(MenuRequest request) {

        if(request.getRestaurantId()==null){
            throw new InvalidRequestException("Restaurant ID cannot be null");
        }

        if(request.getDate()==null){
            throw new InvalidRequestException("Menu date cannot be null");
        }

        if(request.getMealType()==null){
            throw new InvalidRequestException("Meal type cannot be null");
        }

        if(request.getMenuItemIds()==null || request.getMenuItemIds().isEmpty()){
            throw new InvalidRequestException("Menu must contain at least one item");
        }

        if(restaurantService.getRestaurantById(request.getRestaurantId())==null){
            throw new ResourceNotFoundException("Restaurant not found with id: " + request.getRestaurantId());
        }

        for(Long menuItemId : request.getMenuItemIds()){
            if(menuItemService.getMenuItemById(menuItemId) == null){
                throw new ResourceNotFoundException("Menu item not found with id: " + menuItemId);
            }
        }

        Optional<Menu> menu = menuRepository.findByRestaurantIdAndDateAndMealType(request.getRestaurantId(), request.getDate(), request.getMealType());
        if(menu.isPresent()){
            throw new DuplicateResourceException("Menu already exists for restaurant " + request.getRestaurantId() + " on " + request.getDate() + " for " + request.getMealType());
        }


        Menu m = new Menu();
        m.setDate(request.getDate());
        m.setMenuItemIds(request.getMenuItemIds());
        m.setMealType(request.getMealType());
        m.setRestaurantId(request.getRestaurantId());

        menuRepository.save(m);


        MenuResponse menuResponse = new MenuResponse();
        menuResponse.setDate(m.getDate());
        menuResponse.setMealType(m.getMealType());
        menuResponse.setRestaurantLocation(restaurantService.getRestaurantById(m.getRestaurantId()).getLocation());
        menuResponse.setRestaurantName(restaurantService.getRestaurantById(m.getRestaurantId()).getName());
        menuResponse.setItems(m.getMenuItemIds().stream().map(menuItemService::getMenuItemById).toList());

        return menuResponse;
    }

    /**
     * TODO: Get a menu by its ID, returned as a MenuResponse.
     *
     * Steps:
     *   1. Find the menu by ID in the repository.
     *   2. If not found → throw ResourceNotFoundException("Menu not found with id: " + id)
     *   3. Convert to MenuResponse using toMenuResponse() and return.
     */
    public MenuResponse getMenuById(Long id) {
        // TODO: Implement this method
        Optional<Menu> menu = menuRepository.findById(id);
        if(menu.isEmpty()){
            throw new ResourceNotFoundException("Menu not found with id: " + id);
        }

        MenuResponse menuResponse = new MenuResponse();
        menuResponse.setDate(menu.get().getDate());
        menuResponse.setMealType(menu.get().getMealType());
        menuResponse.setRestaurantLocation(restaurantService.getRestaurantById(menu.get().getRestaurantId()).getLocation());
        menuResponse.setRestaurantName(restaurantService.getRestaurantById(menu.get().getRestaurantId()).getName());
        menuResponse.setItems(menu.get().getMenuItemIds().stream().map(menuItemService::getMenuItemById).toList());

        return menuResponse;
    }

    /**
     * TODO: Get all menus as a list of MenuResponse.
     *
     * Steps:
     *   1. Fetch all menus from the repository.
     *   2. Convert each one to MenuResponse using toMenuResponse().
     *   3. Return the list.
     *
     * Hint: You can use a stream with .map(this::toMenuResponse) or a for-loop.
     */
    public List<MenuResponse> getAllMenus() {
        // TODO: Implement this method
        List<Menu> menus = menuRepository.findAll();
        List<MenuResponse> menuResponses = menus.stream().map(this::toMenuResponse).toList();
        return menuResponses;
    }

    /**
     * TODO: Get all menus for a specific restaurant.
     *
     * Steps:
     *   1. Verify the restaurant exists using restaurantService.getRestaurantById().
     *   2. Find all menus for this restaurant using menuRepository.findByRestaurantId().
     *   3. Convert each to MenuResponse and return.
     */
    public List<MenuResponse> getMenusByRestaurantId(Long restaurantId) {
        // TODO: Implement this method
        if(restaurantService.getRestaurantById(restaurantId) == null){
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }

        List<Menu> menus = menuRepository.findByRestaurantId(restaurantId);
        List<MenuResponse> menuResponses = menus.stream().map(this::toMenuResponse).toList();
        return menuResponses;
    }

    /**
     * TODO: Get all menus for a specific date.
     *
     * Steps:
     *   1. Find all menus for this date using menuRepository.findByDate().
     *   2. Convert each to MenuResponse and return.
     */
    public List<MenuResponse> getMenusByDate(LocalDate date) {
        // TODO: Implement this method
        List<Menu> menus = menuRepository.findByDate(date);
        List<MenuResponse> menuResponses = menus.stream().map(this::toMenuResponse).toList();
        return menuResponses;
    }

    /**
     * TODO: Get all menus for a specific restaurant on a specific date.
     *
     * Steps:
     *   1. Verify the restaurant exists.
     *   2. Find menus using menuRepository.findByRestaurantIdAndDate().
     *   3. Convert each to MenuResponse and return.
     */
    public List<MenuResponse> getMenusByRestaurantIdAndDate(Long restaurantId, LocalDate date) {
        // TODO: Implement this method
        if(restaurantService.getRestaurantById(restaurantId) == null){
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }
        List<Menu> menus = menuRepository.findByRestaurantIdAndDate(restaurantId,date);

        return menus.stream().map(this::toMenuResponse).toList();
    }

    /**
     * TODO: Update an existing menu.
     *
     * Steps:
     *   1. Find the existing menu by ID. If not found → throw ResourceNotFoundException.
     *   2. Validate the request fields (same as create: restaurantId, date, mealType, menuItemIds).
     *   3. Verify the restaurant exists.
     *   4. Verify all menu item IDs exist.
     *   5. Check for duplicate: if the (restaurantId + date + mealType) combination has changed,
     *      check that no OTHER menu already uses that combination.
     *      Use menuRepository.findByRestaurantIdAndDateAndMealType().
     *      If a duplicate is found AND it's not the same menu (different ID) → throw DuplicateResourceException.
     *   6. Update the existing menu's fields.
     *   7. Save and convert to MenuResponse.
     */
    public MenuResponse updateMenu(Long id, MenuRequest request) {
        // TODO: Implement this method

            if(request.getRestaurantId()==null){
                throw new InvalidRequestException("Restaurant ID cannot be null");
            }

            if(request.getDate()==null){
                throw new InvalidRequestException("Menu date cannot be null");
            }

            if(request.getMealType()==null){
                throw new InvalidRequestException("Meal type cannot be null");
            }

            if(request.getMenuItemIds()==null || request.getMenuItemIds().isEmpty()){
                throw new InvalidRequestException("Menu must contain at least one item");
            }

            Menu existingMenu = menuRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));

            if(restaurantService.getRestaurantById(request.getRestaurantId())==null){
                throw new ResourceNotFoundException("Restaurant not found with id: " + request.getRestaurantId());
            }

            for(Long menuItemId : request.getMenuItemIds()){
                if(menuItemService.getMenuItemById(menuItemId) == null){
                    throw new ResourceNotFoundException("Menu item not found with id: " + menuItemId);
                }
            }

            if(!existingMenu.getRestaurantId().equals(request.getRestaurantId())
                    || !existingMenu.getDate().equals(request.getDate())
                    || !existingMenu.getMealType().equals(request.getMealType())) {
                Optional<Menu> duplicateMenu = menuRepository.findByRestaurantIdAndDateAndMealType(
                        request.getRestaurantId(), request.getDate(), request.getMealType());
                if (duplicateMenu.isPresent() && !duplicateMenu.get().getId().equals(id)) {
                    throw new DuplicateResourceException("Menu already exists for restaurant " + request.getRestaurantId() + " on " + request.getDate() + " for " + request.getMealType());
                }
            }

            existingMenu.setRestaurantId(request.getRestaurantId());
            existingMenu.setDate(request.getDate());
            existingMenu.setMealType(request.getMealType());
            existingMenu.setMenuItemIds(request.getMenuItemIds());
            menuRepository.save(existingMenu);

            MenuResponse menuResponse = new MenuResponse();
            menuResponse.setDate(existingMenu.getDate());
            menuResponse.setMealType(existingMenu.getMealType());
            menuResponse.setRestaurantLocation(restaurantService.getRestaurantById(existingMenu.getRestaurantId()).getLocation());
            menuResponse.setRestaurantName(restaurantService.getRestaurantById(existingMenu.getRestaurantId()).getName());
            menuResponse.setItems(existingMenu.getMenuItemIds().stream().map(menuItemService::getMenuItemById).toList());
            return menuResponse;
    }

    /**
     * TODO: Delete a menu by ID.
     *
     * Steps:
     *   1. Verify the menu exists (look it up, throw ResourceNotFoundException if not found).
     *   2. Delete it using the repository.
     */
    public void deleteMenu(Long id) {
        // TODO: Implement this method
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));
        menuRepository.deleteById(id);
    }

    // ═══════════════════════════════════════════════════════════════
    //  HELPER METHOD — DO NOT MODIFY
    // ═══════════════════════════════════════════════════════════════

    /**
     * Converts a Menu entity into a MenuResponse DTO.
     * Enriches the response with the restaurant's name/location and full MenuItem objects
     * (instead of just IDs).
     */
    private MenuResponse toMenuResponse(Menu menu) {
        Restaurant restaurant = restaurantService.getRestaurantById(menu.getRestaurantId());
        List<MenuItem> items = menu.getMenuItemIds().stream()
                .map(menuItemService::getMenuItemById)
                .toList();

        MenuResponse response = new MenuResponse();
        response.setId(menu.getId());
        response.setRestaurantName(restaurant.getName());
        response.setRestaurantLocation(restaurant.getLocation());
        response.setDate(menu.getDate());
        response.setMealType(menu.getMealType());
        response.setItems(items);
        return response;
    }
}
