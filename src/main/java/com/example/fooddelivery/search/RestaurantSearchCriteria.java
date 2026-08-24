package com.example.fooddelivery.search;


import com.example.fooddelivery.entity.Address;

public class RestaurantSearchCriteria {

    private String city;
    private Address userLocation;
    private Double maxDistance;
    private String menuKeyword;

    public RestaurantSearchCriteria() {
    }

    public String getCity() {
        return city;
    }

    public RestaurantSearchCriteria setCity(String city) {
        this.city = city;
        return this;
    }

    public Address getUserLocation() {
        return userLocation;
    }

    public Double getMaxDistance() {
        return maxDistance;
    }

    public RestaurantSearchCriteria setProximity(
            Address userLocation,
            double maxDistance) {

        this.userLocation = userLocation;
        this.maxDistance = maxDistance;
        return this;
    }

    public String getMenuKeyword() {
        return menuKeyword;
    }

    public RestaurantSearchCriteria setMenuKeyword(String menuKeyword) {
        this.menuKeyword = menuKeyword;
        return this;
    }
}