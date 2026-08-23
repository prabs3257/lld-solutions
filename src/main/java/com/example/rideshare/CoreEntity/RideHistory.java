package com.example.rideshare.CoreEntity;

import com.example.rideshare.CoreEnums.TripState;

public class RideHistory {

    private String rideId;
    private String riderId;
    private String driverId;
    private int totalFare;
    private TripState tripState;

    public RideHistory(String rideId, String riderId, String driverId, int totalFare, TripState tripState) {
        this.rideId = rideId;
        this.riderId = riderId;
        this.driverId = driverId;
        this.totalFare = totalFare;
        this.tripState = tripState;
    }
}
