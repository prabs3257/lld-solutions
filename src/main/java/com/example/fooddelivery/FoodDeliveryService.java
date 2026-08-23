package com.example.fooddelivery;

import com.example.fooddelivery.entity.*;
import com.example.fooddelivery.order.Order;
import com.example.fooddelivery.order.OrderItem;
import com.example.fooddelivery.order.OrderManager;
import com.example.fooddelivery.order.OrderStatus;
import com.example.fooddelivery.search.RestaurantSearchStrategy;
import com.example.fooddelivery.strategy.DeliveryAssignmentStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

public class FoodDeliveryService {
    private static volatile FoodDeliveryService instance;
    private final Map<String, Customer> customers = new ConcurrentHashMap<>();
    private final Map<String, Restaurant> restaurants = new ConcurrentHashMap<>();
    private final Map<String, DeliveryAgent> deliveryAgents = new ConcurrentHashMap<>();
    private final OrderManager orderManager = new OrderManager();
    private DeliveryAssignmentStrategy assignmentStrategy;

    private FoodDeliveryService() {}

    public static FoodDeliveryService getInstance() {
        if (instance == null) {
            synchronized (FoodDeliveryService.class) {
                if (instance == null) instance = new FoodDeliveryService();
            }
        }
        return instance;
    }

    public void setAssignmentStrategy(DeliveryAssignmentStrategy assignmentStrategy) {
        this.assignmentStrategy = assignmentStrategy;
        orderManager.setAssignmentStrategy(assignmentStrategy);
    }

    // --- Registration ---
    public Customer registerCustomer(String name, String phone, Address address) {
        Customer customer = new Customer(name, phone, address);
        customers.put(customer.getId(), customer);
        return customer;
    }

    public Restaurant registerRestaurant(String name, Address address) {
        Restaurant restaurant = new Restaurant(name, address);
        restaurants.put(restaurant.getId(), restaurant);
        return restaurant;
    }

    public DeliveryAgent registerDeliveryAgent(String name, String phone, Address initialLocation) {
        DeliveryAgent deliveryAgent = new DeliveryAgent(name, phone, initialLocation);
        deliveryAgents.put(deliveryAgent.getId(), deliveryAgent);
        return deliveryAgent;
    }

    public Order placeOrder(String customerId, String restaurantId, List<OrderItem> items) {
        Customer customer = customers.get(customerId);
        Restaurant restaurant = restaurants.get(restaurantId);
        if (customer == null || restaurant == null) {
            throw new NoSuchElementException("Customer or Restaurant not found.");
        }
        return orderManager.placeOrder(customer, restaurant, items);
    }

    public void updateOrderStatus(String orderId, OrderStatus newStatus) {
        List<DeliveryAgent> availableAgents = new ArrayList<>(deliveryAgents.values());
        orderManager.updateOrderStatus(orderId, newStatus, availableAgents);
    }

    public void cancelOrder(String orderId) {
        orderManager.cancelOrder(orderId);
    }

    public List<Restaurant> searchRestaurants(List<RestaurantSearchStrategy> strategies) {
        // Start with the full list of restaurants
        List<Restaurant> results = new ArrayList<>(restaurants.values());

        // Sequentially apply each filter strategy
        // We can also use chain of responsibility design pattern here
        for (RestaurantSearchStrategy strategy : strategies) {
            results = strategy.filter(results);
        }

        return results;
    }

    public Menu getRestaurantMenu(String restaurantId) {
        Restaurant restaurant = restaurants.get(restaurantId);
        if (restaurant == null) {
            throw new NoSuchElementException("Restaurant with ID " + restaurantId + " not found.");
        }
        return restaurant.getMenu();
    }
}
