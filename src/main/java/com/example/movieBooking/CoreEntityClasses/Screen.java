package com.example.movieBooking.CoreEntityClasses;

import java.util.List;

public class Screen {

    private List<Seat> seats;

    public Screen(final List<Seat> seats) {
        this.seats = seats;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(final List<Seat> seats) {
        this.seats = seats;
    }
}
