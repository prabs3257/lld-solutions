package com.example.movieBooking.CoreEntityClasses;

public class Seat {

    private int seatId;
    private int price;

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(final int seatId) {
        this.seatId = seatId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public Seat(final int seatId, final int price) {
        this.seatId = seatId;
        this.price = price;
    }
}
