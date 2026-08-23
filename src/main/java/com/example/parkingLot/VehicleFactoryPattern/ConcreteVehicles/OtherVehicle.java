package com.example.parkingLot.VehicleFactoryPattern.ConcreteVehicles;

import com.example.parkingLot.FareStrategyPattern.ParkingFeeStrategy;
import com.example.parkingLot.VehicleFactoryPattern.Vehicle;

public class OtherVehicle extends Vehicle {
    public OtherVehicle(String licensePlate, String vehicleType, ParkingFeeStrategy feeStrategy) {
        super(licensePlate, vehicleType, feeStrategy);
    }
}
