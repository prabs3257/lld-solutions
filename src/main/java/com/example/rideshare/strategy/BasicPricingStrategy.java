package com.example.rideshare.strategy;

import com.example.rideshare.CoreEntity.Location;

public class BasicPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Location pickupLocation, Location dropLocation) {
        return pickupLocation.calcDistance(dropLocation) * 10.0;

    }
}
