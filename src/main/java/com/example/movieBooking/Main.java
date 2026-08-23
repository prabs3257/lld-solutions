package com.example.movieBooking;

import com.example.movieBooking.CoreEntityClasses.*;
import com.example.movieBooking.Services.BookingService;
import com.example.movieBooking.Services.PaymentService;
import com.example.movieBooking.providers.SeatLockProvider;
import com.example.movieBooking.providers.SeatLockProviderImpl;
import com.example.movieBooking.strategy.CardPaymentStrategy;
import com.example.movieBooking.strategy.PaymentStrategy;
import com.example.movieBooking.strategy.UPIPaymentStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        SeatLockProvider seatLockProvider = new SeatLockProviderImpl();
        PaymentStrategy paymentStrategy = new UPIPaymentStrategy();
        PaymentStrategy paymentStrategyFail = new CardPaymentStrategy();
        BookingService bookingService = BookingService.getInstance(seatLockProvider);
        PaymentService paymentService = new PaymentService(seatLockProvider, bookingService);

        List<Seat> seats = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            seats.add(new Seat(i, 100));
        }

        Screen screen = new Screen(seats);
        Theatre theatre = new Theatre(List.of(screen));
        Movie movie = new Movie("Avengers");
        Show show = new Show(movie, screen, UUID.randomUUID());
        Show show2 = new Show(movie, screen, UUID.randomUUID());


        User userPrabhav = new User(UUID.randomUUID(), "Prabhav");


        User userHimanshi = new User(UUID.randomUUID(), "Himanshi");

//        Thread.sleep(3000);

        Thread t1 = new Thread(() -> {
            try {
//                Thread.sleep(6000);
                Booking bookingPrabhav = bookingService.createBooking(show, List.of(1,2,3), userPrabhav);
                Thread.sleep(1000);
                if(bookingPrabhav != null) {
                    paymentService.processPayment(bookingPrabhav, userPrabhav, new UPIPaymentStrategy());
                }
                Booking bookingPrabhav1 = bookingService.createBooking(show, List.of(4,5), userPrabhav);
                Thread.sleep(1000);
                if(bookingPrabhav != null) {
                    paymentService.processPayment(bookingPrabhav1, userPrabhav, new UPIPaymentStrategy());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });

        Thread t2 = new Thread(() -> {

            try {
                Thread.sleep(3000);
                Booking bookingHimanshi = bookingService.createBooking(show, List.of(5), userHimanshi);
                Thread.sleep(1000);
                if(bookingHimanshi != null) {
                    paymentService.processPayment(bookingHimanshi, userHimanshi, new UPIPaymentStrategy());
//                    paymentService.processPayment(bookingHimanshi, userHimanshi, new CardPaymentStrategy());
//                    paymentService.processPayment(bookingHimanshi, userHimanshi, new CardPaymentStrategy());
//                    paymentService.processPayment(bookingHimanshi, userHimanshi, new CardPaymentStrategy());
//                    paymentService.processPayment(bookingHimanshi, userHimanshi, new CardPaymentStrategy());
//
//                    paymentService.processPayment(bookingHimanshi, userHimanshi, new CardPaymentStrategy());
//                    paymentService.processPayment(bookingHimanshi, userHimanshi, new CardPaymentStrategy());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
