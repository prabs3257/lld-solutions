package com.example.fooddelivery.search;

import com.example.fooddelivery.entity.Restaurant;

import java.util.List;
import java.util.stream.Collectors;

public class SearchByCityHandler
        extends RestaurantSearchHandler {

    @Override
    protected List<Restaurant> applyFilter(
            RestaurantSearchCriteria criteria,
            List<Restaurant> restaurants) {

        // City filter was not requested.
        // Simply pass the restaurants unchanged.
        if (criteria.getCity() == null ||
                criteria.getCity().isBlank()) {

            return restaurants;
        }

        String city = criteria.getCity();

        return restaurants.stream()
                .filter(restaurant ->
                        restaurant.getAddress()
                                .getCity()
                                .equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }
}