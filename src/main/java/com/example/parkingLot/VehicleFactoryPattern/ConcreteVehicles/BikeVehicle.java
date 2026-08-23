package com.example.parkingLot.VehicleFactoryPattern.ConcreteVehicles;

import com.example.parkingLot.FareStrategyPattern.ParkingFeeStrategy;
import com.example.parkingLot.VehicleFactoryPattern.Vehicle;

public class BikeVehicle extends Vehicle {

    public BikeVehicle(String licensePlate, String vehicleType, ParkingFeeStrategy feeStrategy) {
        super(licensePlate, vehicleType, feeStrategy);
    }
}
