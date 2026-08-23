package com.example.parkingLot.VehicleFactoryPattern.ConcreteVehicles;

import com.example.parkingLot.FareStrategyPattern.ParkingFeeStrategy;
import com.example.parkingLot.VehicleFactoryPattern.Vehicle;

public class CarVehicle extends Vehicle {
    public CarVehicle(String licensePlate, String vehicleType, ParkingFeeStrategy feeStrategy) {
        super(licensePlate, vehicleType, feeStrategy);
    }
}
