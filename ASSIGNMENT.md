# Assignment 1: "What to Eat?" — Office Cafeteria Menu API

## Overview

You are building the backend for **"What to Eat?"** — an application that lets employees browse menus across multiple in-office cafeterias. The app tracks which food items are available for **breakfast, lunch, and dinner** at each restaurant, on any given date.

Your job is to implement the **Service layer** and **Controller layer** so the REST APIs work end-to-end.

---

## Architecture

```
HTTP Request
    │
    ▼
┌──────────────────┐
│   Controller      │  ← Receives HTTP requests, calls Service, returns HTTP responses
│   (YOUR CODE)     │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│   Service         │  ← Contains business logic, validation, calls Repository
│   (YOUR CODE)     │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│   Repository      │  ← Stores/retrieves data from in-memory HashMaps
│   (PRE-BUILT)     │
└──────────────────┘
```

---

## Domain Model

### Restaurant
An office cafeteria/restaurant.
- `id` (Long) — auto-generated
- `name` (String) — e.g., "Cafeteria Alpha"
- `location` (String) — e.g., "Building A, Floor 2"

### MenuItem
A food or drink item that can appear on menus.
- `id` (Long) — auto-generated
- `name` (String) — e.g., "Pancakes"
- `description` (String) — e.g., "Fluffy buttermilk pancakes with maple syrup"
- `price` (double) — e.g., 5.99
- `vegetarian` (boolean) — true if vegetarian
- `category` (ItemCategory) — one of: `APPETIZER`, `MAIN_COURSE`, `DESSERT`, `BEVERAGE`, `SIDE`

### Menu
Links a restaurant + date + meal type to a set of menu items.
- `id` (Long) — auto-generated
- `restaurantId` (Long) — which restaurant this menu belongs to
- `date` (LocalDate) — the date this menu is for
- `mealType` (MealType) — one of: `BREAKFAST`, `LUNCH`, `DINNER`
- `menuItemIds` (List<Long>) — IDs of the items on this menu

**Constraint:** Only ONE menu can exist per (restaurant + date + mealType) combination. You can't have two different BREAKFAST menus for the same restaurant on the same date.

### MenuResponse (DTO)
When returning a menu via the API, we enrich it with full details instead of raw IDs:
- `id`, `restaurantName`, `restaurantLocation`, `date`, `mealType`
- `items` — list of full `MenuItem` objects (not just IDs)

---

## What You Need to Implement

### Files to edit (6 files):

| # | File | What to implement |
|---|------|-------------------|
| 1 | `service/RestaurantService.java` | CRUD business logic for restaurants |
| 2 | `service/MenuItemService.java` | CRUD business logic for menu items |
| 3 | `service/MenuService.java` | CRUD + query logic for menus (cross-service validation) |
| 4 | `controller/RestaurantController.java` | REST endpoints for restaurants |
| 5 | `controller/MenuItemController.java` | REST endpoints for menu items |
| 6 | `controller/MenuController.java` | REST endpoints for menus |

### Files you must NOT modify:

Everything in `model/`, `dto/`, `repository/`, `exception/`, and `Assignment1Application.java`.

---

## API Endpoints to Implement

### Restaurants — `/api/restaurants`

| Method | Path | Description | Status Code |
|--------|------|-------------|-------------|
| POST | `/api/restaurants` | Create a restaurant | 201 Created |
| GET | `/api/restaurants` | List all restaurants | 200 OK |
| GET | `/api/restaurants/{id}` | Get restaurant by ID | 200 OK |
| PUT | `/api/restaurants/{id}` | Update a restaurant | 200 OK |
| DELETE | `/api/restaurants/{id}` | Delete a restaurant | 204 No Content |

### Menu Items — `/api/menu-items`

| Method | Path | Description | Status Code |
|--------|------|-------------|-------------|
| POST | `/api/menu-items` | Create a menu item | 201 Created |
| GET | `/api/menu-items` | List all menu items | 200 OK |
| GET | `/api/menu-items/{id}` | Get menu item by ID | 200 OK |
| PUT | `/api/menu-items/{id}` | Update a menu item | 200 OK |
| DELETE | `/api/menu-items/{id}` | Delete a menu item | 204 No Content |

### Menus — `/api/menus`

| Method | Path | Description | Status Code |
|--------|------|-------------|-------------|
| POST | `/api/menus` | Create a menu | 201 Created |
| GET | `/api/menus` | List all menus | 200 OK |
| GET | `/api/menus/{id}` | Get menu by ID | 200 OK |
| GET | `/api/menus/restaurant/{restaurantId}` | Get menus by restaurant | 200 OK |
| GET | `/api/menus/date/{date}` | Get menus by date (yyyy-MM-dd) | 200 OK |
| GET | `/api/menus/restaurant/{restaurantId}/date/{date}` | Get menus by restaurant + date | 200 OK |
| PUT | `/api/menus/{id}` | Update a menu | 200 OK |
| DELETE | `/api/menus/{id}` | Delete a menu | 204 No Content |

---

## Validation Rules & Error Handling

Your services should validate inputs and throw the appropriate exceptions. The `GlobalExceptionHandler` (already built) will catch them and return proper HTTP error responses.

| Exception | HTTP Status | When to throw |
|-----------|-------------|---------------|
| `InvalidRequestException` | 400 Bad Request | Empty/null name, negative price, missing required fields |
| `ResourceNotFoundException` | 404 Not Found | Restaurant/MenuItem/Menu with given ID doesn't exist |
| `DuplicateResourceException` | 409 Conflict | Menu already exists for same restaurant + date + meal type |

### Specific validations:

**Restaurant:**
- Name must not be null or blank
- Location must not be null or blank

**MenuItem:**
- Name must not be null or blank
- Price must be greater than 0
- Category must not be null

**Menu (most complex):**
- Restaurant ID must not be null
- Date must not be null
- Meal type must not be null
- Menu item IDs list must not be null or empty
- Restaurant must exist (verify by fetching it)
- All menu items must exist (verify each one)
- No duplicate menu for same restaurant + date + meal type

---

## How to Approach This

### Recommended order:
1. **Start with `RestaurantService`** — simplest CRUD, gets you comfortable with the pattern
2. **Then `RestaurantController`** — wire up the service to HTTP endpoints
3. **Then `MenuItemService`** + `MenuItemController` — similar pattern, slightly more validation
4. **Finally `MenuService`** + `MenuController` — the interesting one with cross-service calls

### Pattern for each Controller method:
```java
// 1. Call the service
SomeResult result = someService.doSomething(params);

// 2. Return with the right HTTP status
return ResponseEntity.ok(result);                              // 200
return ResponseEntity.status(HttpStatus.CREATED).body(result); // 201
return ResponseEntity.noContent().build();                     // 204
```

### Pattern for each Service method:
```java
// 1. Validate inputs (throw InvalidRequestException if bad)
// 2. Business logic checks (throw DuplicateResourceException if conflict)
// 3. Call repository
// 4. Return result
```

---

## Running the Application

```bash
./gradlew bootRun
```

The server starts at `http://localhost:8080`.

---

## Testing Your Implementation

### Option 1: Manual testing with curl

```bash
# Create a restaurant
curl -X POST http://localhost:8080/api/restaurants \
  -H "Content-Type: application/json" \
  -d '{"name": "Cafeteria Alpha", "location": "Building A, Floor 2"}'

# Create a menu item
curl -X POST http://localhost:8080/api/menu-items \
  -H "Content-Type: application/json" \
  -d '{"name": "Pancakes", "description": "Fluffy buttermilk pancakes", "price": 5.99, "vegetarian": true, "category": "MAIN_COURSE"}'

# Create a menu
curl -X POST http://localhost:8080/api/menus \
  -H "Content-Type: application/json" \
  -d '{"restaurantId": 1, "date": "2026-02-10", "mealType": "BREAKFAST", "menuItemIds": [1]}'
```

### Option 2: Automated tests (recommended)

Run the provided Python test script:

```bash
# Make sure the Spring Boot app is running first, then:
python3 test_apis.py
```

The script tests all endpoints including edge cases. **All tests should pass when your implementation is correct.**

---

## Hints

1. **`Optional.orElseThrow()`** is your friend for the "get by ID" methods:
   ```java
   return repository.findById(id)
       .orElseThrow(() -> new ResourceNotFoundException("Not found with id: " + id));
   ```

2. **Streams + map** for converting lists:
   ```java
   return menus.stream()
       .map(this::toMenuResponse)
       .toList();
   ```

3. **ResponseEntity cheat sheet:**
   - `ResponseEntity.ok(body)` → 200
   - `ResponseEntity.status(HttpStatus.CREATED).body(body)` → 201
   - `ResponseEntity.noContent().build()` → 204

4. For **string validation**, `String.isBlank()` checks for null? No — check null first:
   ```java
   if (name == null || name.isBlank()) { throw ... }
   ```

---

Good luck! Start simple, test often, and build up incrementally.
