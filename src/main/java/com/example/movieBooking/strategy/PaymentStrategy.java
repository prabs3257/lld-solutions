package com.example.movieBooking.strategy;

import com.example.movieBooking.CoreEntityClasses.Booking;

public interface PaymentStrategy {

    boolean processPayment(Booking booking);
}
