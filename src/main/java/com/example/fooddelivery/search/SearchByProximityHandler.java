package com.example.fooddelivery.search;

import com.example.fooddelivery.entity.Address;
import com.example.fooddelivery.entity.Restaurant;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SearchByProximityHandler
        extends RestaurantSearchHandler {

    @Override
    protected List<Restaurant> applyFilter(
            RestaurantSearchCriteria criteria,
            List<Restaurant> restaurants) {

        Address userLocation = criteria.getUserLocation();
        Double maxDistance = criteria.getMaxDistance();

        // Proximity filtering was not requested.
        if (userLocation == null || maxDistance == null) {
            return restaurants;
        }

        return restaurants.stream()

                // Keep only restaurants within maxDistance.
                .filter(restaurant ->
                        userLocation.distanceTo(
                                restaurant.getAddress())
                                <= maxDistance)

                // Nearest restaurant first.
                .sorted(
                        Comparator.comparingDouble(
                                restaurant ->
                                        userLocation.distanceTo(
                                                restaurant.getAddress())
                        )
                )

                .collect(Collectors.toList());
    }
}