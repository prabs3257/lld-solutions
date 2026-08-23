package com.example.movieBooking.CoreEntityClasses;

import java.util.List;
import java.util.UUID;

public class Booking {
    private Show show;
    private List<Seat> seats;
    private User user;
    private UUID bookingId;
    private boolean paymentDone;

    public Show getShow() {
        return show;
    }

    public void setShow(final Show show) {
        this.show = show;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public boolean isPaymentDone() {
        return paymentDone;
    }

    public void setPaymentDone(final boolean paymentDone) {
        this.paymentDone = paymentDone;
    }

    private int price;

    public Booking( final Show show, final List<Seat> seats, final User user, UUID bookingId, boolean paymentDone, int price ) {
        this.show = show;
        this.seats = seats;
        this.user = user;
        this.bookingId = bookingId;
        this.paymentDone = paymentDone;
        this.price = price;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(final List<Seat> seats) {
        this.seats = seats;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(final UUID bookingId) {
        this.bookingId = bookingId;
    }
}
