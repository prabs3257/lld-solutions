package com.example.rideshare.strategy;

import com.example.rideshare.CoreEntity.Location;

public interface PricingStrategy {

    double calculatePrice(Location pickupLocation, Location dropLocation);
}
