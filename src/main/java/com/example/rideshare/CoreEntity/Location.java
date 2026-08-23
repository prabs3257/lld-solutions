package com.example.rideshare.CoreEntity;

import static java.lang.Math.pow;

public class Location {

    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double calcDistance(Location otherLocation){
        return pow(pow(otherLocation.latitude-this.latitude,2) +  pow(otherLocation.longitude-this.longitude,2), 0.5);
    }
}
