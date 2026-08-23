package com.example.rideshare.repository;

import com.example.rideshare.CoreEntity.Driver;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DriverRepository {

    private final Map<UUID, Driver> drivers = new ConcurrentHashMap<>();

    public void save(Driver driver) {
        drivers.put(driver.getUserId(), driver);
    }


    public Optional<Driver> findById(UUID driverId) {
        return Optional.ofNullable(drivers.get(driverId));
    }

    public List<Driver> allDrivers() {
        return drivers.values().stream().toList();
    }
}
