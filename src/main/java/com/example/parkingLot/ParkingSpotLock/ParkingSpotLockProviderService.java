package com.example.parkingLot.ParkingSpotLock;

import com.example.parkingLot.ParkingSpots.ParkingSpot;
import com.example.parkingLot.VehicleFactoryPattern.Vehicle;

import java.util.List;
import java.util.stream.Collectors;

public class ParkingSpotLockProviderService {

    private ParkingSpotLockProvider parkingSpotLockProvider;

    public ParkingSpotLockProviderService(ParkingSpotLockProvider parkingSpotLockProvider) {
        this.parkingSpotLockProvider = parkingSpotLockProvider;
    }

    public void lockParkingSpot(ParkingSpot parkingSpot, Vehicle vehicle) throws Exception {
        parkingSpotLockProvider.lockSpot(parkingSpot, vehicle);
    }

    public void vacateParkingSpot(ParkingSpot parkingSpot, Vehicle vehicle) throws Exception {
        parkingSpotLockProvider.unlockSpot(parkingSpot, vehicle);
    }

    public List<ParkingSpot> getAvailableParkingSpots(List<ParkingSpot> parkingSpots, Vehicle vehicle) {

        return parkingSpots.stream()
                .filter(parkingSpot ->
                        !parkingSpotLockProvider.lockPresent(
                                parkingSpots,
                                parkingSpot.getSpotNumber(),
                                vehicle
                        ) && parkingSpot.canParkVehicle(vehicle)
                )
                .collect(Collectors.toList());
    }

}
