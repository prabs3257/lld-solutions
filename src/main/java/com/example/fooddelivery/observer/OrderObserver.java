package com.example.fooddelivery.observer;

import com.example.fooddelivery.order.Order;

public interface OrderObserver {
    void onUpdate(Order order);
}
