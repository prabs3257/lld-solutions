package com.example.parkingLot.ParkingLotController;

import com.example.parkingLot.ParkingSpotLock.ParkingSpotLockProviderService;
import com.example.parkingLot.ParkingSpots.ParkingSpot;
import com.example.parkingLot.VehicleFactoryPattern.Vehicle;

import java.util.List;

public class ParkingLot {
    private List<ParkingSpot> parkingSpots;
    private ParkingSpotLockProviderService parkingSpotLockProviderService;

    // Constructor to initialize the parking lot with parking spots
    public ParkingLot(List<ParkingSpot> parkingSpots,  ParkingSpotLockProviderService parkingSpotLockProviderService) {
        this.parkingSpots = parkingSpots;
        this.parkingSpotLockProviderService = parkingSpotLockProviderService;
    }
    // Method to find an available spot based on vehicle type
    public ParkingSpot findAvailableSpot(String vehicleType) {
        for (ParkingSpot spot : parkingSpots) {
            if (!spot.isOccupied() && spot.getSpotType().equals(vehicleType)) {
                return spot; // Found an available spot for the vehicle type
            }
        }
        return null; // No available spot found for the given vehicle type
    }
    // Method to park a vehicle
    public ParkingSpot parkVehicle(Vehicle vehicle) throws Exception {
//        ParkingSpot spot = findAvailableSpot(vehicle.getVehicleType());
        List<ParkingSpot> parkingSpot = parkingSpotLockProviderService.getAvailableParkingSpots(parkingSpots, vehicle);
        ParkingSpot spot = parkingSpot.stream().findFirst().orElse(null);
        if (spot != null) {
            parkingSpotLockProviderService.lockParkingSpot(spot, vehicle);
            spot.parkVehicle(vehicle); // Mark the spot as occupied
            System.out.println(
                    "Vehicle parked successfully in spot: " + spot.getSpotNumber());
            return spot;
        }
        System.out.println(
                "No parking spots available for " + vehicle.getVehicleType() + vehicle.getLicensePlate() + "!");
        return null;
    }
    // Method to vacate a parking spot
    public void vacateSpot(ParkingSpot spot, Vehicle vehicle) throws Exception {
        if (spot != null
                && spot.getVehicle().equals(vehicle)) {
            spot.vacate(); // Free the spot
            parkingSpotLockProviderService.vacateParkingSpot(spot, vehicle);
            System.out.println(vehicle.getVehicleType()
                    + " vacated the spot: " + spot.getSpotNumber());
        } else {
            System.out.println("Invalid operation! Either the spot is already vacant "
                    + "or the vehicle does not match.");
        }
    }
    // Method to find a spot by its number
    public ParkingSpot getSpotByNumber(int spotNumber) {
        for (ParkingSpot spot : parkingSpots) {
            if (spot.getSpotNumber() == spotNumber) {
                return spot;
            }
        }
        return null; // Spot not found
    }
    // Getter for parking spots
    public List<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }
}
