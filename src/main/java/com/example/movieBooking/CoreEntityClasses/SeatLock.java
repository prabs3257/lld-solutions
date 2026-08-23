package com.example.movieBooking.CoreEntityClasses;

import java.time.LocalDateTime;

public class SeatLock {
    public User getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(final User ownerUser) {
        this.ownerUser = ownerUser;
    }

    public SeatLock(final User ownerUser, final LocalDateTime expiryTime, final Show show, final Seat seat) {
        this.ownerUser = ownerUser;
        this.expiryTime = expiryTime;
        this.show = show;
        this.seat = seat;
    }

    private Seat seat;
    private Show show;
    private LocalDateTime expiryTime;
    private User ownerUser;

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(final Seat seat) {
        this.seat = seat;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(final Show show) {
        this.show = show;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(final LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }
}
