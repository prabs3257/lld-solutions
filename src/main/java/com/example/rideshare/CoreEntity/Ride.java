package com.example.rideshare.CoreEntity;

import com.example.rideshare.CoreEnums.TripState;

import java.util.UUID;

public class Ride {

    private UUID rideId;
    private Rider rider;
    private Driver driver;
    private double totalFare;
    private Location startLocation;
    private Location endLocation;
    private volatile TripState tripState;

    public Ride(Location endLocation, Location startLocation, double totalFare, Driver driver, Rider rider, UUID rideId) {
        this.endLocation = endLocation;
        this.startLocation = startLocation;
        this.totalFare = totalFare;
        this.driver = driver;
        this.rider = rider;
        this.rideId = rideId;
        this.tripState = TripState.REQUESTED;
    }

    public UUID getRideId() {
        return rideId;
    }

    public void setRideId(UUID rideId) {
        this.rideId = rideId;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(int totalFare) {
        this.totalFare = totalFare;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public TripState getTripState() {
        return tripState;
    }

    public void setTripState(TripState tripState) {
        System.out.println("RideState of ride: " + this.rideId + " changed to " + tripState);
        this.tripState = tripState;
    }
}
