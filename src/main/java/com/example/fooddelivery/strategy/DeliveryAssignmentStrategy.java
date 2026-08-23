package com.example.fooddelivery.strategy;

import com.example.fooddelivery.entity.DeliveryAgent;
import com.example.fooddelivery.order.Order;

import java.util.List;
import java.util.Optional;

public interface DeliveryAssignmentStrategy {
    Optional<DeliveryAgent> findAgent(Order order, List<DeliveryAgent> agents);
}
