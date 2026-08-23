package com.example.rideshare;

import com.example.rideshare.CoreEntity.Driver;
import com.example.rideshare.CoreEntity.Location;
import com.example.rideshare.CoreEntity.Ride;
import com.example.rideshare.CoreEntity.Rider;
import com.example.rideshare.CoreEnums.CarType;
import com.example.rideshare.repository.DriverRepository;
import com.example.rideshare.repository.RiderRepository;
import com.example.rideshare.service.RideService;
import com.example.rideshare.strategy.BasicPricingStrategy;
import com.example.rideshare.strategy.NearestRiderMatchingStrategy;
import com.example.rideshare.strategy.PricingStrategy;
import com.example.rideshare.strategy.RiderMatchingStrategy;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        DriverRepository driverRepository = new DriverRepository();
        RiderRepository riderRepository = new RiderRepository();

        RiderMatchingStrategy riderMatchingStrategy = new NearestRiderMatchingStrategy(driverRepository);
        PricingStrategy pricingStrategy = new BasicPricingStrategy();

        RideService rideService = new RideService(riderMatchingStrategy, pricingStrategy);

        Rider riderPrabhav = new Rider("Prabhav", new Location(1.0, 1.0));
        Rider riderTom = new Rider("Tom", new Location(1.0, 1.0));
        riderRepository.save(riderTom);
        riderRepository.save(riderPrabhav);

        Driver driverHimanshi = new Driver("Himanshi", CarType.SEDAN, new Location(1.0, 1.0));
        Driver driverBob = new Driver("Bob", CarType.SEDAN, new Location(1.5, 1.1));
        Driver driverDavid = new Driver("David", CarType.SUV, new Location(3.0, 2.0));

        driverRepository.save(driverHimanshi);
        driverRepository.save(driverBob);
        driverRepository.save(driverDavid);

        Ride ride = rideService.requestRide(riderPrabhav, CarType.SEDAN, riderPrabhav.getCurrLocation(), new Location(5.0, 5.0));

        Ride rideTom = rideService.requestRide(riderTom, CarType.SEDAN, riderPrabhav.getCurrLocation(), new Location(5.0, 5.0));


        Thread t1 = new Thread(() -> {
            if(ride != null) {
                rideService.acceptRide(ride, driverHimanshi);
            }
        });

        Thread t2 = new Thread(() -> {
            if(rideTom != null) {
                rideService.acceptRide(rideTom, driverHimanshi);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();


    }
}
