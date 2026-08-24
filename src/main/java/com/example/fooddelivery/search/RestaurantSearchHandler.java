package com.example.fooddelivery.search;

import com.example.fooddelivery.entity.Restaurant;

import java.util.List;

public abstract class RestaurantSearchHandler {

    private RestaurantSearchHandler nextHandler;

    /**
     * Connects the current handler to the next handler.
     *
     * Returning nextHandler allows:
     *
     * cityHandler
     *      .setNext(proximityHandler)
     *      .setNext(menuHandler);
     */
    public RestaurantSearchHandler setNext(
            RestaurantSearchHandler nextHandler) {

        this.nextHandler = nextHandler;
        return nextHandler;
    }

    /**
     * Template method for processing the chain.
     *
     * 1. Current handler applies its responsibility.
     * 2. Result is passed to next handler.
     * 3. If there is no next handler, result is returned.
     */
    public final List<Restaurant> handle(
            RestaurantSearchCriteria criteria,
            List<Restaurant> restaurants) {

        List<Restaurant> filteredRestaurants =
                applyFilter(criteria, restaurants);

        // Nothing remains, so we can short-circuit the chain.
        if (filteredRestaurants.isEmpty()) {
            return filteredRestaurants;
        }

        // End of chain.
        if (nextHandler == null) {
            return filteredRestaurants;
        }

        // Pass the current result to the next handler.
        return nextHandler.handle(
                criteria,
                filteredRestaurants
        );
    }

    /**
     * Every concrete handler implements only its own responsibility.
     */
    protected abstract List<Restaurant> applyFilter(
            RestaurantSearchCriteria criteria,
            List<Restaurant> restaurants);
}