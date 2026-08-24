package com.example.fooddelivery;

import com.example.fooddelivery.entity.*;
import com.example.fooddelivery.order.OrderItem;
import com.example.fooddelivery.order.OrderStatus;
import com.example.fooddelivery.search.*;
import com.example.fooddelivery.strategy.NearestAvailableAgentStrategy;

import java.util.List;

public class FoodDeliveryServiceDemo {
    public static void main(String[] args) {
        // 1. Setup the system
        FoodDeliveryService service = FoodDeliveryService.getInstance();
        service.setAssignmentStrategy(new NearestAvailableAgentStrategy());

        // 2. Define Addresses
        Address aliceAddress = new Address("123 Maple St", "Springfield", "12345", 40.7128, -74.0060);
        Address pizzaAddress = new Address("456 Oak Ave", "Springfield", "12345", 40.7138, -74.0070);
        Address burgerAddress = new Address("789 Pine Ln", "Springfield", "12345", 40.7108, -74.0050);
        Address tacoAddress = new Address("101 Elm Ct", "Shelbyville", "54321", 41.7528, -75.0160);

        // 3. Register entities
        Customer alice = service.registerCustomer("Alice", "123-4567-890", aliceAddress);
        Restaurant pizzaPalace = service.registerRestaurant("Pizza Palace", pizzaAddress);
        Restaurant burgerBarn = service.registerRestaurant("Burger Barn", burgerAddress);
        Restaurant tacoTown = service.registerRestaurant("Taco Town", tacoAddress);
        service.registerDeliveryAgent("Bob", "321-4567-880", new Address("1 B", "Springfield", "12345", 40.71, -74.00));

        // 4. Setup menus
        pizzaPalace.addToMenu(new MenuItem("P001", "Margherita Pizza", 12.99));
        pizzaPalace.addToMenu(new MenuItem("P002", "Veggie Pizza", 11.99));
        burgerBarn.addToMenu(new MenuItem("B001", "Classic Burger", 8.99));
        tacoTown.addToMenu(new MenuItem("T001", "Crunchy Taco", 3.50));

        // 5. Demonstrate Search Functionality

        System.out.println("\n--- 1. Searching for Restaurants ---");

// ----------------------------------------------------
// A. Search by City
// ----------------------------------------------------

        System.out.println(
                "\n(A) Restaurants in 'Springfield':"
        );

        RestaurantSearchCriteria cityCriteria =
                new RestaurantSearchCriteria()
                        .setCity("Springfield");

        List<Restaurant> springfieldRestaurants =
                service.searchRestaurants(cityCriteria);

        springfieldRestaurants.forEach(
                restaurant ->
                        System.out.println(
                                " - " + restaurant.getName()
                        )
        );

// ----------------------------------------------------
// B. Search by Proximity
// ----------------------------------------------------

        System.out.println(
                "\n(B) Restaurants near Alice "
                        + "(within 0.01 distance units):"
        );

        RestaurantSearchCriteria proximityCriteria =
                new RestaurantSearchCriteria()
                        .setProximity(
                                aliceAddress,
                                0.01
                        );

        List<Restaurant> nearbyRestaurants =
                service.searchRestaurants(
                        proximityCriteria
                );

        nearbyRestaurants.forEach(
                restaurant ->
                        System.out.printf(
                                " - %s (Distance: %.4f)%n",
                                restaurant.getName(),
                                aliceAddress.distanceTo(
                                        restaurant.getAddress()
                                )
                        )
        );

// ----------------------------------------------------
// C. Search by Menu Keyword
// ----------------------------------------------------

        System.out.println(
                "\n(C) Restaurants that serve 'Pizza':"
        );

        RestaurantSearchCriteria pizzaCriteria =
                new RestaurantSearchCriteria()
                        .setMenuKeyword("Pizza");

        List<Restaurant> pizzaRestaurants =
                service.searchRestaurants(
                        pizzaCriteria
                );

        pizzaRestaurants.forEach(
                restaurant ->
                        System.out.println(
                                " - " + restaurant.getName()
                        )
        );

// ----------------------------------------------------
// D. Combined Search
// ----------------------------------------------------

        System.out.println(
                "\n(D) Burger joints near Alice:"
        );
        RestaurantSearchCriteria burgerNearAliceCriteria =
                new RestaurantSearchCriteria()
                        .setProximity(
                                aliceAddress,
                                0.01
                        )
                        .setMenuKeyword("Burger");

        List<Restaurant> burgerJointsNearAlice =
                service.searchRestaurants(
                        burgerNearAliceCriteria
                );
        burgerJointsNearAlice.forEach(
                restaurant ->
                        System.out.println(" - " + restaurant.getName())
        );
        // 6. Demonstrate Browsing a Menu
        System.out.println("\n--- 2. Browsing a Menu ---");
        System.out.println("\nMenu for 'Pizza Palace':");
        Menu pizzaMenu = service.getRestaurantMenu(pizzaPalace.getId());
        pizzaMenu.getItems().values().forEach(item ->
                System.out.printf("  - %s: $%.2f\n", item.getName(), item.getPrice())
        );

        // 7. Alice places an order from a searched restaurant
        System.out.println("\n--- 3. Placing an Order ---");
        if (!pizzaRestaurants.isEmpty()) {
            Restaurant chosenRestaurant = pizzaRestaurants.get(0);
            MenuItem chosenItem = chosenRestaurant.getMenu().getItem("P001");

            System.out.printf("\nAlice is ordering '%s' from '%s'.\n", chosenItem.getName(), chosenRestaurant.getName());
            var order = service.placeOrder(alice.getId(), chosenRestaurant.getId(), List.of(new OrderItem(chosenItem, 1)));

            System.out.println("\n--- Restaurant starts preparing the order ---");
            service.updateOrderStatus(order.getId(), OrderStatus.PREPARING);

            System.out.println("\n--- Order is ready for pickup ---");
            System.out.println("System will now find the nearest available delivery agent...");
            service.updateOrderStatus(order.getId(), OrderStatus.READY_FOR_PICKUP);

            System.out.println("\n--- Agent delivers the order ---");
            service.updateOrderStatus(order.getId(), OrderStatus.DELIVERED);
        }
    }
}
