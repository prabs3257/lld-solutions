package com.example.parkingLot;

import com.example.parkingLot.FareStrategyPattern.ConcreteStrategies.BasicHourlyRateStrategy;
import com.example.parkingLot.FareStrategyPattern.ConcreteStrategies.PremiumRateStrategy;
import com.example.parkingLot.FareStrategyPattern.ParkingFeeStrategy;
import com.example.parkingLot.ParkingLotController.ParkingLot;
import com.example.parkingLot.ParkingSpotLock.ParkingSpotLockProdiverImpl;
import com.example.parkingLot.ParkingSpotLock.ParkingSpotLockProvider;
import com.example.parkingLot.ParkingSpotLock.ParkingSpotLockProviderService;
import com.example.parkingLot.ParkingSpots.ConcreteParkingSpots.CarParkingSpot;
import com.example.parkingLot.ParkingSpots.ParkingSpot;
import com.example.parkingLot.PaymentStrategyPattern.ConcretePaymentStrategies.CashPayment;
import com.example.parkingLot.PaymentStrategyPattern.ConcretePaymentStrategies.CreditCardPayment;
import com.example.parkingLot.PaymentStrategyPattern.PaymentStrategy;
import com.example.parkingLot.VehicleFactoryPattern.Vehicle;
import com.example.parkingLot.VehicleFactoryPattern.VehicleFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // Initialize parking spots
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        parkingSpots.add(new CarParkingSpot(1, "Car"));

        // Initialize parking lot
        ParkingSpotLockProvider parkingSpotLockProvider = new ParkingSpotLockProdiverImpl();
        ParkingSpotLockProviderService parkingSpotLockProviderService = new ParkingSpotLockProviderService(parkingSpotLockProvider);
        ParkingLot parkingLot = new ParkingLot(parkingSpots,  parkingSpotLockProviderService);
        // Create fee strategies
        ParkingFeeStrategy basicHourlyRateStrategy = new BasicHourlyRateStrategy();
        ParkingFeeStrategy premiumRateStrategy = new PremiumRateStrategy();
        // Create vehicles using Factory Pattern with fee strategies
        Vehicle car1 = VehicleFactory.createVehicle("Car", "CAR123", basicHourlyRateStrategy);
        Vehicle car2 = VehicleFactory.createVehicle("Car", "CAR345", basicHourlyRateStrategy);

        Vehicle bike1 = VehicleFactory.createVehicle("Bike", "BIKE456", premiumRateStrategy);
        Vehicle bike2 = VehicleFactory.createVehicle("Bike", "BIKE123", premiumRateStrategy);


        Thread t1 = new Thread(() -> {
            try {

                ParkingSpot carSpot = parkingLot.parkVehicle(car1);
                Thread.sleep(2000);
                parkingLot.vacateSpot(carSpot, car1);
            } catch (Exception e) {
                System.err.println("User1 booking (seats 5,6,7) failed: " + e.getMessage());
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(3000);
                ParkingSpot carSpot2 = parkingLot.parkVehicle(car2);
            } catch (Exception e) {
                System.err.println("User2 booking (seats 5,6,7) failed: " + e.getMessage());
            }
        });


        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // Park vehicles
//        ParkingSpot carSpot = parkingLot.parkVehicle(car1);
//        ParkingSpot bikeSpot = parkingLot.parkVehicle(bike1);
//
//        ParkingSpot carSpot2 = parkingLot.parkVehicle(car2);
//        ParkingSpot bikeSpot2 = parkingLot.parkVehicle(bike2);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Select payment method for your vehicle:");
        System.out.println("1. Credit Card");
        System.out.println("2. Cash");
        int paymentMethod = scanner.nextInt();
        // Process payments using Strategy Patterns
//        if (carSpot != null) {
//            // Calculate fee using the specific strategy for the vehicle
//            double carFee = car1.calculateFee(2, DurationType.HOURS);
//            PaymentStrategy carPaymentStrategy =
//                    getPaymentStrategy(paymentMethod, carFee);
//            carPaymentStrategy.processPayment(carFee);
//            parkingLot.vacateSpot(carSpot, car1);
//        }
//        if (bikeSpot != null) {
//            // Calculate fee using the specific strategy for the vehicle
//            double bikeFee = bike1.calculateFee(3, DurationType.HOURS);
//            PaymentStrategy bikePaymentStrategy =
//                    getPaymentStrategy(paymentMethod, bikeFee);
//            bikePaymentStrategy.processPayment(bikeFee);
//            parkingLot.vacateSpot(bikeSpot, bike1);
//        }
        scanner.close();
    }
    private static PaymentStrategy getPaymentStrategy(
            int paymentMethod, double fee) {
        switch (paymentMethod) {
            case 1:
                return new CreditCardPayment(fee);
            case 2:
                return new CashPayment(fee);
            default:
                System.out.println("Invalid choice! Default to Credit card payment.");
                return new CreditCardPayment(fee);
        }
    }
    }

    /*

Output :

Vehicle parked successfully in spot: 1
Vehicle parked successfully in spot: 3
Vehicle parked successfully in spot: 2
Vehicle parked successfully in spot: 4
Select payment method for your vehicle:
1. Credit Card
2. Cash
1
Processing credit card payment of $20.0
Car vacated the spot: 1
Processing credit card payment of $24.0
Bike vacated the spot: 3

Process finished with exit code 0

     */