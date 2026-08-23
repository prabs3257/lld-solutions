package com.example.parkingLot.VehicleFactoryPattern;

import com.example.parkingLot.FareStrategyPattern.ParkingFeeStrategy;
import com.example.parkingLot.VehicleFactoryPattern.ConcreteVehicles.BikeVehicle;
import com.example.parkingLot.VehicleFactoryPattern.ConcreteVehicles.CarVehicle;
import com.example.parkingLot.VehicleFactoryPattern.ConcreteVehicles.OtherVehicle;

public class VehicleFactory {
    public static Vehicle createVehicle(String vehicleType, String licensePlate, ParkingFeeStrategy feeStrategy) {
        if (vehicleType.equalsIgnoreCase("Car")) {
            return new CarVehicle(licensePlate, vehicleType, feeStrategy);
        } else if (vehicleType.equalsIgnoreCase("Bike")) {
            return new BikeVehicle(licensePlate, vehicleType, feeStrategy);
        }
        return new OtherVehicle(licensePlate, vehicleType, feeStrategy); // For unsupported vehicle types
    }
}
