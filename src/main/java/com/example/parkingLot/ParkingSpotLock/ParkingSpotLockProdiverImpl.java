package com.example.parkingLot.ParkingSpotLock;

import com.example.parkingLot.ParkingSpots.ParkingSpot;
import com.example.parkingLot.VehicleFactoryPattern.Vehicle;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingSpotLockProdiverImpl implements ParkingSpotLockProvider{

    private final ConcurrentHashMap<ParkingSpot, ParkingSpotLock> parkingSpotLockMap;

    public ParkingSpotLockProdiverImpl() {
        this.parkingSpotLockMap = new ConcurrentHashMap<>();
    }

    @Override
    public void lockSpot(ParkingSpot parkingSpot, Vehicle vehicle) throws Exception {

        synchronized (parkingSpot){
            ParkingSpotLock parkingSpotLock = parkingSpotLockMap.get(parkingSpot);
            if(parkingSpotLock == null){
                parkingSpotLockMap.put(parkingSpot, new ParkingSpotLock(parkingSpot, vehicle));
            }else{
                System.out.println("Parking spot already occupied. " + vehicle.getLicensePlate() + "not parked");
            }
        }

    }

    @Override
    public Boolean lockPresent(List<ParkingSpot> parkingSpots, int spotNumber,  Vehicle vehicle) {

        ParkingSpot parkingSpot = parkingSpots.stream()
                .filter(s -> s.getSpotNumber() == spotNumber)
                .findFirst()
                .orElse(null);

        synchronized (parkingSpot){
            return parkingSpotLockMap.get(parkingSpot) != null;
        }
    }

    @Override
    public void unlockSpot(ParkingSpot parkingSpot, Vehicle vehicle) {

        ParkingSpotLock parkingSpotLock = parkingSpotLockMap.get(parkingSpot);
        synchronized (parkingSpot){
            if(parkingSpotLock!=null && parkingSpotLock.getOwnerVehicle().equals(vehicle)){
                parkingSpotLockMap.remove(parkingSpot);
            }
        }
    }
}
