package com.example.rideshare.service;

import com.example.rideshare.CoreEntity.Driver;
import com.example.rideshare.CoreEntity.Location;
import com.example.rideshare.CoreEntity.Ride;
import com.example.rideshare.CoreEntity.Rider;
import com.example.rideshare.CoreEnums.CarType;
import com.example.rideshare.CoreEnums.DriverState;
import com.example.rideshare.CoreEnums.TripState;
import com.example.rideshare.strategy.PricingStrategy;
import com.example.rideshare.strategy.RiderMatchingStrategy;

import java.util.List;
import java.util.UUID;

public class RideService {

    private RiderMatchingStrategy riderMatchingStrategy;
    private PricingStrategy pricingStrategy;

    public RideService(RiderMatchingStrategy riderMatchingStrategy, PricingStrategy pricingStrategy) {
        this.riderMatchingStrategy = riderMatchingStrategy;
        this.pricingStrategy = pricingStrategy;
    }

    public Ride requestRide(Rider rider, CarType carType, Location pickupLocation, Location dropOffLocation) {
        List<Driver> bestDrivers = riderMatchingStrategy.findDrivers(rider, pickupLocation, carType);

        if(bestDrivers.isEmpty()){
            System.out.println("No Drivers nearby");
            return null;
        }
        double fare = pricingStrategy.calculatePrice(pickupLocation,dropOffLocation);
        Ride ride = new Ride(dropOffLocation, pickupLocation, fare
                , null, rider, UUID.randomUUID());

        System.out.println("Rider " + rider.getUserName() + " has requested a ride");
        System.out.println(bestDrivers.size() + " Drivers nearby");
        for(Driver driver : bestDrivers){
            driver.onUpdate(ride);
        }

        return ride;
    }

    public void acceptRide(Ride ride, Driver driver){

        if(!driver.tryUpdateDriverState()){
            System.out.println("Driver " + driver.getUserName() + " is already in a ride. so cant accept ride: " + ride.getRideId());
            return;
        }
        boolean assigned = false;
        synchronized (ride){

            if(ride.getTripState().equals(TripState.REQUESTED)){
                ride.setDriver(driver);
                System.out.println("Driver " + driver.getUserName() +" accepted ride " + ride.getRideId());
                assigned = true;
                ride.setTripState(TripState.IN_PROGRESS);
            }

        }
        if(!assigned){
            driver.setDriverState(DriverState.IDLE);
        }

    }
}
