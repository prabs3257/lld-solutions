package com.example.fooddelivery.order;

import com.example.fooddelivery.entity.Customer;
import com.example.fooddelivery.entity.DeliveryAgent;
import com.example.fooddelivery.entity.Restaurant;
import com.example.fooddelivery.strategy.DeliveryAssignmentStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

public class OrderManager {
    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    private DeliveryAssignmentStrategy assignmentStrategy;

    public void setAssignmentStrategy(DeliveryAssignmentStrategy assignmentStrategy) {
        this.assignmentStrategy = assignmentStrategy;
    }

    public Order placeOrder(Customer customer, Restaurant restaurant, List<OrderItem> items) {
        if (customer == null || restaurant == null) {
            throw new NoSuchElementException("Customer or Restaurant not found.");
        }

        Order order = new Order(customer, restaurant, items);
        orders.put(order.getId(), order);
        customer.addOrderToHistory(order);
        System.out.printf("Order %s placed by %s at %s.\n",
                order.getId(), customer.getName(), restaurant.getName());
        order.setStatus(OrderStatus.PENDING);
        return order;
    }

    public void updateOrderStatus(String orderId, OrderStatus newStatus, List<DeliveryAgent> availableAgents) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new NoSuchElementException("Order not found.");
        }

        order.setStatus(newStatus);

        if (newStatus == OrderStatus.READY_FOR_PICKUP) {
            assignDelivery(order, availableAgents);
        }
    }

    public void cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("ERROR: Order with ID " + orderId + " not found.");
            return;
        }

        if (order.cancel()) {
            System.out.println("SUCCESS: Order " + orderId + " has been successfully canceled.");
        } else {
            System.out.println("FAILED: Order " + orderId + " could not be canceled. Its status is: " + order.getStatus());
        }
    }

    public void assignDelivery(Order order, List<DeliveryAgent> availableAgents) {
        if (assignmentStrategy == null) {
            System.out.println("No delivery assignment strategy configured for order " + order.getId());
            return;
        }

        List<DeliveryAgent> agents = availableAgents == null ? new ArrayList<>() : availableAgents;
        assignmentStrategy.findAgent(order, agents).ifPresentOrElse(
                agent -> {
                    order.assignDeliveryAgent(agent);
                    System.out.printf("Agent %s (dist: %.2f) assigned to order %s.\n",
                            agent.getName(),
                            agent.getCurrentLocation().distanceTo(order.getRestaurant().getAddress()),
                            order.getId());
                    order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
                },
                () -> System.out.println("No available delivery agents found for order " + order.getId())
        );
    }

    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }
}
