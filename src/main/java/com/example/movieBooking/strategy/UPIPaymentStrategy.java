package com.example.movieBooking.strategy;

import com.example.movieBooking.CoreEntityClasses.Booking;

public class UPIPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(final Booking booking) {
//        System.out.println("UPIPaymentStrategy payment successful for booking: " + booking.getBookingId());
        return true;
    }
}
