package com.example.parkingLot.ParkingSpotLock;

import com.example.parkingLot.ParkingSpots.ParkingSpot;
import com.example.parkingLot.VehicleFactoryPattern.Vehicle;

import java.util.List;

public interface ParkingSpotLockProvider {

     void lockSpot(ParkingSpot parkingSpot, Vehicle vehicle) throws Exception;
    Boolean lockPresent(List<ParkingSpot> parkingSpots, int spotNumber, Vehicle vehicle);
    void unlockSpot(ParkingSpot parkingSpot, Vehicle vehicle);
}
