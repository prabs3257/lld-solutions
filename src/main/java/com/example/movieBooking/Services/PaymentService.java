package com.example.movieBooking.Services;

import com.example.movieBooking.CoreEntityClasses.Booking;
import com.example.movieBooking.CoreEntityClasses.User;
import com.example.movieBooking.providers.SeatLockProvider;
import com.example.movieBooking.strategy.PaymentStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentService {

    private int maxRetries = 3; // Maximum number of retries for payment processing
    private SeatLockProvider seatLockProvider;
    private Map<Booking, Integer> retryCountMap = new ConcurrentHashMap<>(); // Map to track retry counts for each booking
    private BookingService bookingService;

    public PaymentService(SeatLockProvider seatLockProvider, BookingService bookingService) {
        this.seatLockProvider = seatLockProvider;
        this.bookingService = bookingService;
    }

    public boolean processPayment(Booking booking, User user, PaymentStrategy paymentStrategy) {

        if(!user.equals(booking.getUser())) {
            System.out.println("User mismatch for booking: " + booking.getBookingId());
            return false;
        }
        if(paymentStrategy.processPayment(booking)){
            System.out.println("Payment successful for booking: " + booking.getBookingId());
            bookingService.confirmBooking(booking);
            return true;
        }else{
            System.out.println("Payment processing failed for booking: " + booking.getBookingId());
            retryCountMap.put(booking, retryCountMap.getOrDefault(booking, 0) + 1);

            if(retryCountMap.get(booking) > maxRetries){
                System.out.println("Max retries reached for booking: " + booking.getBookingId() + " unlocking seats.");
                seatLockProvider.unlockSeat(booking.getShow(), booking.getSeats(), booking.getUser());
                bookingService.removeBooking(booking);
                return false;
            }
            return false;
        }

    }
}
