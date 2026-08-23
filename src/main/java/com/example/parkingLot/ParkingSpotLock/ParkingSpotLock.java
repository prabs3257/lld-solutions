package com.example.parkingLot.ParkingSpotLock;

import com.example.parkingLot.ParkingSpots.ParkingSpot;
import com.example.parkingLot.VehicleFactoryPattern.Vehicle;

public class ParkingSpotLock {

    private ParkingSpot parkingSpot;
    private Vehicle  ownerVehicle;

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public Vehicle getOwnerVehicle() {
        return ownerVehicle;
    }

    public void setOwnerVehicle(Vehicle ownerVehicle) {
        this.ownerVehicle = ownerVehicle;
    }

    public ParkingSpotLock(ParkingSpot parkingSpot, Vehicle ownerVehicle) {
        this.parkingSpot = parkingSpot;
        this.ownerVehicle = ownerVehicle;
    }
}
