package com.example.rideshare.strategy;

import com.example.rideshare.CoreEntity.Driver;
import com.example.rideshare.CoreEntity.Location;
import com.example.rideshare.CoreEntity.Rider;
import com.example.rideshare.CoreEnums.CarType;
import com.example.rideshare.repository.DriverRepository;

import java.util.List;

public class NearestRiderMatchingStrategy implements RiderMatchingStrategy {

    private DriverRepository driverRepository;
    private final int MAX_DISTANCE = 5;

    public NearestRiderMatchingStrategy(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public List<Driver> findDrivers(Rider rider, Location pickupLocation, CarType carType) {
        return driverRepository.allDrivers().stream()
                .filter(driver -> isNearby(rider, driver)
                        && carType.equals(driver.getCarType()))
                .toList();

    }

    private Boolean isNearby(Rider rider, Driver driver){
        return rider.getCurrLocation().calcDistance(driver.getCurrLocation()) <= MAX_DISTANCE;
    }
}
