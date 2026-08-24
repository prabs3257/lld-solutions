package com.example.fooddelivery.search;

import com.example.fooddelivery.entity.Restaurant;

import java.util.List;
import java.util.stream.Collectors;

public class SearchByMenuKeywordHandler
        extends RestaurantSearchHandler {

    @Override
    protected List<Restaurant> applyFilter(
            RestaurantSearchCriteria criteria,
            List<Restaurant> restaurants) {

        String keyword = criteria.getMenuKeyword();

        // Menu keyword filtering was not requested.
        if (keyword == null || keyword.isBlank()) {
            return restaurants;
        }

        String normalizedKeyword = keyword.toLowerCase();

        return restaurants.stream()
                .filter(restaurant ->
                        restaurant.getMenu()
                                .getItems()
                                .values()
                                .stream()
                                .anyMatch(item ->
                                        item.getName()
                                                .toLowerCase()
                                                .contains(normalizedKeyword)))
                .collect(Collectors.toList());
    }
}