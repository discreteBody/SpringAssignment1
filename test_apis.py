"""
What to Eat? — End-to-End API Test Suite
=========================================
Run with:  python3 test_apis.py

Make sure the Spring Boot app is running on http://localhost:8080 first.
"""

import requests
import sys

BASE_URL = "http://localhost:8080"
PASS = 0
FAIL = 0
ERRORS = []


# ─── Helpers ────────────────────────────────────────────────────────────────

def test(name, condition, detail=""):
    global PASS, FAIL
    if condition:
        PASS += 1
        print(f"  ✅ {name}")
    else:
        FAIL += 1
        msg = f"  ❌ {name}" + (f" — {detail}" if detail else "")
        print(msg)
        ERRORS.append(name)


def post(path, json_body):
    return requests.post(f"{BASE_URL}{path}", json=json_body)


def get(path):
    return requests.get(f"{BASE_URL}{path}")


def put(path, json_body):
    return requests.put(f"{BASE_URL}{path}", json=json_body)


def delete(path):
    return requests.delete(f"{BASE_URL}{path}")


# ─── 1. Restaurant CRUD Tests ──────────────────────────────────────────────

def test_restaurants():
    print("\n═══ RESTAURANT CRUD TESTS ═══\n")

    # --- Create ---
    print("--- Create ---")
    r = post("/api/restaurants", {"name": "Cafeteria Alpha", "location": "Building A, Floor 2"})
    test("Create restaurant returns 201", r.status_code == 201, f"got {r.status_code}")
    alpha = r.json()
    test("Created restaurant has id", alpha.get("id") is not None)
    test("Created restaurant name matches", alpha.get("name") == "Cafeteria Alpha")
    test("Created restaurant location matches", alpha.get("location") == "Building A, Floor 2")
    alpha_id = alpha.get("id")

    r = post("/api/restaurants", {"name": "Bistro Beta", "location": "Building B, Lobby"})
    test("Create second restaurant returns 201", r.status_code == 201)
    beta = r.json()
    beta_id = beta.get("id")

    # --- Create Validation ---
    print("\n--- Create Validation ---")
    r = post("/api/restaurants", {"name": "", "location": "Somewhere"})
    test("Empty name returns 400", r.status_code == 400, f"got {r.status_code}")

    r = post("/api/restaurants", {"name": "Test", "location": ""})
    test("Empty location returns 400", r.status_code == 400, f"got {r.status_code}")

    r = post("/api/restaurants", {"name": None, "location": "Somewhere"})
    test("Null name returns 400", r.status_code == 400, f"got {r.status_code}")

    r = post("/api/restaurants", {"name": "   ", "location": "Somewhere"})
    test("Blank name (whitespace) returns 400", r.status_code == 400, f"got {r.status_code}")

    # --- Get All ---
    print("\n--- Get All ---")
    r = get("/api/restaurants")
    test("Get all returns 200", r.status_code == 200)
    test("Get all returns at least 2 restaurants", len(r.json()) >= 2, f"got {len(r.json())}")

    # --- Get By ID ---
    print("\n--- Get By ID ---")
    r = get(f"/api/restaurants/{alpha_id}")
    test("Get by ID returns 200", r.status_code == 200)
    test("Get by ID returns correct restaurant", r.json().get("name") == "Cafeteria Alpha")

    # --- Get Not Found ---
    print("\n--- Not Found ---")
    r = get("/api/restaurants/99999")
    test("Get non-existent returns 404", r.status_code == 404, f"got {r.status_code}")

    # --- Update ---
    print("\n--- Update ---")
    r = put(f"/api/restaurants/{alpha_id}", {"name": "Cafeteria Alpha Prime", "location": "Building A, Floor 3"})
    test("Update returns 200", r.status_code == 200, f"got {r.status_code}")
    test("Updated name is correct", r.json().get("name") == "Cafeteria Alpha Prime")
    test("Updated location is correct", r.json().get("location") == "Building A, Floor 3")

    # --- Update Validation ---
    print("\n--- Update Validation ---")
    r = put(f"/api/restaurants/{alpha_id}", {"name": "", "location": "Somewhere"})
    test("Update with empty name returns 400", r.status_code == 400, f"got {r.status_code}")

    r = put("/api/restaurants/99999", {"name": "X", "location": "Y"})
    test("Update non-existent returns 404", r.status_code == 404, f"got {r.status_code}")

    # --- Delete ---
    print("\n--- Delete ---")
    # Create a throwaway restaurant to delete
    r = post("/api/restaurants", {"name": "To Delete", "location": "Nowhere"})
    del_id = r.json().get("id")

    r = delete(f"/api/restaurants/{del_id}")
    test("Delete returns 204", r.status_code == 204, f"got {r.status_code}")

    r = get(f"/api/restaurants/{del_id}")
    test("Deleted restaurant is gone (404)", r.status_code == 404, f"got {r.status_code}")

    r = delete("/api/restaurants/99999")
    test("Delete non-existent returns 404", r.status_code == 404, f"got {r.status_code}")

    return alpha_id, beta_id


# ─── 2. MenuItem CRUD Tests ────────────────────────────────────────────────

def test_menu_items():
    print("\n═══ MENU ITEM CRUD TESTS ═══\n")

    # --- Create ---
    print("--- Create ---")
    items_data = [
        {"name": "Pancakes", "description": "Fluffy buttermilk pancakes with maple syrup", "price": 5.99, "vegetarian": True, "category": "MAIN_COURSE"},
        {"name": "Scrambled Eggs", "description": "Farm-fresh scrambled eggs", "price": 4.49, "vegetarian": True, "category": "MAIN_COURSE"},
        {"name": "Bacon Strips", "description": "Crispy smoked bacon", "price": 3.99, "vegetarian": False, "category": "SIDE"},
        {"name": "Orange Juice", "description": "Freshly squeezed OJ", "price": 2.99, "vegetarian": True, "category": "BEVERAGE"},
        {"name": "Caesar Salad", "description": "Romaine lettuce with caesar dressing", "price": 7.99, "vegetarian": True, "category": "APPETIZER"},
        {"name": "Grilled Chicken", "description": "Herb-marinated grilled chicken breast", "price": 12.99, "vegetarian": False, "category": "MAIN_COURSE"},
        {"name": "Chocolate Cake", "description": "Rich dark chocolate layer cake", "price": 6.49, "vegetarian": True, "category": "DESSERT"},
        {"name": "Tomato Soup", "description": "Creamy tomato basil soup", "price": 5.49, "vegetarian": True, "category": "APPETIZER"},
        {"name": "Steak", "description": "8oz ribeye steak with garlic butter", "price": 18.99, "vegetarian": False, "category": "MAIN_COURSE"},
        {"name": "Iced Tea", "description": "Sweet peach iced tea", "price": 2.49, "vegetarian": True, "category": "BEVERAGE"},
    ]

    item_ids = []
    for item_data in items_data:
        r = post("/api/menu-items", item_data)
        test(f"Create '{item_data['name']}' returns 201", r.status_code == 201, f"got {r.status_code}")
        item = r.json()
        test(f"'{item_data['name']}' has id", item.get("id") is not None)
        item_ids.append(item.get("id"))

    # --- Create Validation ---
    print("\n--- Create Validation ---")
    r = post("/api/menu-items", {"name": "", "description": "Test", "price": 5.0, "vegetarian": True, "category": "SIDE"})
    test("Empty name returns 400", r.status_code == 400, f"got {r.status_code}")

    r = post("/api/menu-items", {"name": "Test", "description": "Test", "price": -1.0, "vegetarian": True, "category": "SIDE"})
    test("Negative price returns 400", r.status_code == 400, f"got {r.status_code}")

    r = post("/api/menu-items", {"name": "Test", "description": "Test", "price": 0, "vegetarian": True, "category": "SIDE"})
    test("Zero price returns 400", r.status_code == 400, f"got {r.status_code}")

    r = post("/api/menu-items", {"name": "Test", "description": "Test", "price": 5.0, "vegetarian": True, "category": None})
    test("Null category returns 400", r.status_code == 400, f"got {r.status_code}")

    # --- Get All ---
    print("\n--- Get All ---")
    r = get("/api/menu-items")
    test("Get all returns 200", r.status_code == 200)
    test("Get all returns at least 10 items", len(r.json()) >= 10, f"got {len(r.json())}")

    # --- Get By ID ---
    print("\n--- Get By ID ---")
    r = get(f"/api/menu-items/{item_ids[0]}")
    test("Get by ID returns 200", r.status_code == 200)
    test("Get by ID returns correct item", r.json().get("name") == "Pancakes")
    test("Price is correct", r.json().get("price") == 5.99)
    test("Vegetarian flag is correct", r.json().get("vegetarian") is True)

    # --- Not Found ---
    print("\n--- Not Found ---")
    r = get("/api/menu-items/99999")
    test("Get non-existent returns 404", r.status_code == 404, f"got {r.status_code}")

    # --- Update ---
    print("\n--- Update ---")
    r = put(f"/api/menu-items/{item_ids[0]}", {
        "name": "Blueberry Pancakes",
        "description": "Fluffy pancakes with fresh blueberries",
        "price": 7.49,
        "vegetarian": True,
        "category": "MAIN_COURSE"
    })
    test("Update returns 200", r.status_code == 200, f"got {r.status_code}")
    test("Updated name is correct", r.json().get("name") == "Blueberry Pancakes")
    test("Updated price is correct", r.json().get("price") == 7.49)

    # Revert name for later tests
    put(f"/api/menu-items/{item_ids[0]}", {
        "name": "Pancakes",
        "description": "Fluffy buttermilk pancakes with maple syrup",
        "price": 5.99,
        "vegetarian": True,
        "category": "MAIN_COURSE"
    })

    # --- Update Validation ---
    print("\n--- Update Validation ---")
    r = put(f"/api/menu-items/{item_ids[0]}", {
        "name": "",
        "description": "Test",
        "price": 5.0,
        "vegetarian": True,
        "category": "SIDE"
    })
    test("Update with empty name returns 400", r.status_code == 400, f"got {r.status_code}")

    r = put("/api/menu-items/99999", {
        "name": "X",
        "description": "X",
        "price": 1.0,
        "vegetarian": True,
        "category": "SIDE"
    })
    test("Update non-existent returns 404", r.status_code == 404, f"got {r.status_code}")

    # --- Delete ---
    print("\n--- Delete ---")
    r = post("/api/menu-items", {"name": "To Delete", "description": "temp", "price": 1.0, "vegetarian": True, "category": "SIDE"})
    del_id = r.json().get("id")

    r = delete(f"/api/menu-items/{del_id}")
    test("Delete returns 204", r.status_code == 204, f"got {r.status_code}")

    r = get(f"/api/menu-items/{del_id}")
    test("Deleted item is gone (404)", r.status_code == 404, f"got {r.status_code}")

    r = delete("/api/menu-items/99999")
    test("Delete non-existent returns 404", r.status_code == 404, f"got {r.status_code}")

    return item_ids


# ─── 3. Menu CRUD + Query Tests ────────────────────────────────────────────

def test_menus(alpha_id, beta_id, item_ids):
    print("\n═══ MENU CRUD + QUERY TESTS ═══\n")

    date1 = "2026-02-10"
    date2 = "2026-02-11"

    breakfast_items = item_ids[0:4]   # Pancakes, Scrambled Eggs, Bacon, OJ
    lunch_items = item_ids[4:7]       # Caesar Salad, Grilled Chicken, Chocolate Cake
    dinner_items = item_ids[7:10]     # Tomato Soup, Steak, Iced Tea

    # --- Create Menus ---
    print("--- Create Menus ---")

    # Alpha breakfast on date1
    r = post("/api/menus", {
        "restaurantId": alpha_id,
        "date": date1,
        "mealType": "BREAKFAST",
        "menuItemIds": breakfast_items
    })
    test("Create Alpha breakfast (date1) returns 201", r.status_code == 201, f"got {r.status_code}")
    menu1 = r.json()
    test("Menu response has id", menu1.get("id") is not None)
    test("Menu response has restaurantName", menu1.get("restaurantName") is not None)
    test("Menu response has items list", isinstance(menu1.get("items"), list))
    test("Menu response items count matches", len(menu1.get("items", [])) == len(breakfast_items))
    menu1_id = menu1.get("id")

    # Alpha lunch on date1
    r = post("/api/menus", {
        "restaurantId": alpha_id,
        "date": date1,
        "mealType": "LUNCH",
        "menuItemIds": lunch_items
    })
    test("Create Alpha lunch (date1) returns 201", r.status_code == 201, f"got {r.status_code}")

    # Alpha dinner on date1
    r = post("/api/menus", {
        "restaurantId": alpha_id,
        "date": date1,
        "mealType": "DINNER",
        "menuItemIds": dinner_items
    })
    test("Create Alpha dinner (date1) returns 201", r.status_code == 201, f"got {r.status_code}")

    # Beta breakfast on date1
    r = post("/api/menus", {
        "restaurantId": beta_id,
        "date": date1,
        "mealType": "BREAKFAST",
        "menuItemIds": [item_ids[0], item_ids[3]]  # Just pancakes and OJ
    })
    test("Create Beta breakfast (date1) returns 201", r.status_code == 201, f"got {r.status_code}")

    # Alpha breakfast on date2
    r = post("/api/menus", {
        "restaurantId": alpha_id,
        "date": date2,
        "mealType": "BREAKFAST",
        "menuItemIds": [item_ids[1], item_ids[3]]  # Eggs and OJ
    })
    test("Create Alpha breakfast (date2) returns 201", r.status_code == 201, f"got {r.status_code}")

    # --- Create Validation ---
    print("\n--- Create Validation ---")

    r = post("/api/menus", {
        "restaurantId": None,
        "date": date1,
        "mealType": "BREAKFAST",
        "menuItemIds": [item_ids[0]]
    })
    test("Null restaurantId returns 400", r.status_code == 400, f"got {r.status_code}")

    r = post("/api/menus", {
        "restaurantId": alpha_id,
        "date": None,
        "mealType": "BREAKFAST",
        "menuItemIds": [item_ids[0]]
    })
    test("Null date returns 400", r.status_code == 400, f"got {r.status_code}")

    r = post("/api/menus", {
        "restaurantId": alpha_id,
        "date": date1,
        "mealType": None,
        "menuItemIds": [item_ids[0]]
    })
    test("Null mealType returns 400", r.status_code == 400, f"got {r.status_code}")

    r = post("/api/menus", {
        "restaurantId": alpha_id,
        "date": date1,
        "mealType": "BREAKFAST",
        "menuItemIds": []
    })
    test("Empty menuItemIds returns 400", r.status_code == 400, f"got {r.status_code}")

    r = post("/api/menus", {
        "restaurantId": alpha_id,
        "date": date1,
        "mealType": "BREAKFAST",
        "menuItemIds": None
    })
    test("Null menuItemIds returns 400", r.status_code == 400, f"got {r.status_code}")

    # --- Duplicate Check ---
    print("\n--- Duplicate Check ---")
    r = post("/api/menus", {
        "restaurantId": alpha_id,
        "date": date1,
        "mealType": "BREAKFAST",
        "menuItemIds": [item_ids[0]]
    })
    test("Duplicate menu returns 409 Conflict", r.status_code == 409, f"got {r.status_code}")

    # --- Invalid References ---
    print("\n--- Invalid References ---")
    r = post("/api/menus", {
        "restaurantId": 99999,
        "date": date1,
        "mealType": "LUNCH",
        "menuItemIds": [item_ids[0]]
    })
    test("Non-existent restaurant returns 404", r.status_code == 404, f"got {r.status_code}")

    r = post("/api/menus", {
        "restaurantId": alpha_id,
        "date": "2026-03-01",
        "mealType": "LUNCH",
        "menuItemIds": [99999]
    })
    test("Non-existent menu item returns 404", r.status_code == 404, f"got {r.status_code}")

    # --- Get By ID ---
    print("\n--- Get By ID ---")
    r = get(f"/api/menus/{menu1_id}")
    test("Get menu by ID returns 200", r.status_code == 200, f"got {r.status_code}")
    data = r.json()
    test("Menu has correct mealType", data.get("mealType") == "BREAKFAST")
    test("Menu has correct date", data.get("date") == date1)
    test("Menu items are enriched (have names)", all("name" in item for item in data.get("items", [])))

    r = get("/api/menus/99999")
    test("Get non-existent menu returns 404", r.status_code == 404, f"got {r.status_code}")

    # --- Get All ---
    print("\n--- Get All ---")
    r = get("/api/menus")
    test("Get all menus returns 200", r.status_code == 200)
    test("Get all returns at least 5 menus", len(r.json()) >= 5, f"got {len(r.json())}")

    # --- Query: By Restaurant ---
    print("\n--- Query: By Restaurant ---")
    r = get(f"/api/menus/restaurant/{alpha_id}")
    test("Get menus by restaurant returns 200", r.status_code == 200)
    alpha_menus = r.json()
    test("Alpha has 4 menus (3 on date1 + 1 on date2)", len(alpha_menus) == 4, f"got {len(alpha_menus)}")

    r = get(f"/api/menus/restaurant/{beta_id}")
    beta_menus = r.json()
    test("Beta has 1 menu", len(beta_menus) == 1, f"got {len(beta_menus)}")

    r = get("/api/menus/restaurant/99999")
    test("Non-existent restaurant returns 404", r.status_code == 404, f"got {r.status_code}")

    # --- Query: By Date ---
    print("\n--- Query: By Date ---")
    r = get(f"/api/menus/date/{date1}")
    test("Get menus by date1 returns 200", r.status_code == 200)
    date1_menus = r.json()
    test("Date1 has 4 menus (3 Alpha + 1 Beta)", len(date1_menus) == 4, f"got {len(date1_menus)}")

    r = get(f"/api/menus/date/{date2}")
    date2_menus = r.json()
    test("Date2 has 1 menu", len(date2_menus) == 1, f"got {len(date2_menus)}")

    r = get("/api/menus/date/2099-12-31")
    empty_menus = r.json()
    test("Future date returns empty list", len(empty_menus) == 0, f"got {len(empty_menus)}")

    # --- Query: By Restaurant + Date ---
    print("\n--- Query: By Restaurant + Date ---")
    r = get(f"/api/menus/restaurant/{alpha_id}/date/{date1}")
    test("Alpha on date1 returns 200", r.status_code == 200)
    alpha_date1 = r.json()
    test("Alpha on date1 has 3 menus (B/L/D)", len(alpha_date1) == 3, f"got {len(alpha_date1)}")

    meal_types = {m.get("mealType") for m in alpha_date1}
    test("All meal types present", meal_types == {"BREAKFAST", "LUNCH", "DINNER"}, f"got {meal_types}")

    r = get(f"/api/menus/restaurant/{alpha_id}/date/{date2}")
    alpha_date2 = r.json()
    test("Alpha on date2 has 1 menu", len(alpha_date2) == 1, f"got {len(alpha_date2)}")

    r = get(f"/api/menus/restaurant/{beta_id}/date/{date2}")
    beta_date2 = r.json()
    test("Beta on date2 has 0 menus", len(beta_date2) == 0, f"got {len(beta_date2)}")

    # --- Update Menu ---
    print("\n--- Update Menu ---")
    r = put(f"/api/menus/{menu1_id}", {
        "restaurantId": alpha_id,
        "date": date1,
        "mealType": "BREAKFAST",
        "menuItemIds": [item_ids[0], item_ids[1], item_ids[2], item_ids[3], item_ids[6]]  # Added chocolate cake to breakfast!
    })
    test("Update menu returns 200", r.status_code == 200, f"got {r.status_code}")
    updated = r.json()
    test("Updated menu has 5 items", len(updated.get("items", [])) == 5, f"got {len(updated.get('items', []))}")

    # Update non-existent
    r = put("/api/menus/99999", {
        "restaurantId": alpha_id,
        "date": date1,
        "mealType": "BREAKFAST",
        "menuItemIds": [item_ids[0]]
    })
    test("Update non-existent menu returns 404", r.status_code == 404, f"got {r.status_code}")

    # --- Delete Menu ---
    print("\n--- Delete Menu ---")
    r = post("/api/menus", {
        "restaurantId": beta_id,
        "date": "2026-03-15",
        "mealType": "DINNER",
        "menuItemIds": [item_ids[8]]
    })
    del_menu_id = r.json().get("id")

    r = delete(f"/api/menus/{del_menu_id}")
    test("Delete menu returns 204", r.status_code == 204, f"got {r.status_code}")

    r = get(f"/api/menus/{del_menu_id}")
    test("Deleted menu is gone (404)", r.status_code == 404, f"got {r.status_code}")

    r = delete("/api/menus/99999")
    test("Delete non-existent menu returns 404", r.status_code == 404, f"got {r.status_code}")


# ─── Main ───────────────────────────────────────────────────────────────────

def main():
    print("=" * 60)
    print("  What to Eat? — API Test Suite")
    print("=" * 60)

    # Check if server is running
    try:
        requests.get(f"{BASE_URL}/api/restaurants", timeout=5)
    except requests.ConnectionError:
        print(f"\n❌ Cannot connect to {BASE_URL}")
        print("   Make sure the Spring Boot app is running: ./gradlew bootRun\n")
        sys.exit(1)

    alpha_id, beta_id = test_restaurants()
    item_ids = test_menu_items()
    test_menus(alpha_id, beta_id, item_ids)

    # --- Summary ---
    total = PASS + FAIL
    print("\n" + "=" * 60)
    print(f"  RESULTS: {PASS}/{total} passed, {FAIL} failed")
    print("=" * 60)

    if FAIL > 0:
        print("\n  Failed tests:")
        for e in ERRORS:
            print(f"    ❌ {e}")
        print()
        sys.exit(1)
    else:
        print("\n  🎉 All tests passed! Great job!\n")
        sys.exit(0)


if __name__ == "__main__":
    main()
