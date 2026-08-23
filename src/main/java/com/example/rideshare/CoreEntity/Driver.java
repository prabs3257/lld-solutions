package com.example.rideshare.CoreEntity;

import com.example.rideshare.CoreEnums.CarType;
import com.example.rideshare.CoreEnums.DriverState;
import com.example.rideshare.CoreEnums.TripState;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Driver extends User {

    private CarType carType;
    private AtomicReference<DriverState> driverState;
    private Location currLocation;

    public Driver(String name, CarType carType,  Location currLocation) {
        super(UUID.randomUUID(), name);
        this.carType = carType;
        this.currLocation = currLocation;
        this.driverState = new AtomicReference<>(DriverState.IDLE);
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public DriverState getDriverState() {
        return driverState.get();
    }

    public Boolean tryUpdateDriverState() {

        return this.driverState.compareAndSet(DriverState.IDLE, DriverState.IN_TRIP);
    }

    public void setDriverState(DriverState driverState) {
        this.driverState.set(driverState);
    }

    public Location getCurrLocation() {
        return currLocation;
    }

    public void setCurrLocation(Location currLocation) {
        this.currLocation = currLocation;
    }

    @Override
    public void onUpdate(Ride ride){
        System.out.printf("--- Notification for Driver %s ---\n", this.getUserName());
        if(ride.getTripState().equals(TripState.REQUESTED)){
            System.out.printf("--- Driver %s is requested for trip---\n", this.getUserName());
        }
    }
}
