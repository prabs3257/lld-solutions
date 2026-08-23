package com.example.rideshare.strategy;

import com.example.rideshare.CoreEntity.Driver;
import com.example.rideshare.CoreEntity.Location;
import com.example.rideshare.CoreEntity.Rider;
import com.example.rideshare.CoreEnums.CarType;

import java.util.List;

public interface RiderMatchingStrategy {

    List<Driver> findDrivers(Rider rider, Location pickupLocation, CarType carType);
}
