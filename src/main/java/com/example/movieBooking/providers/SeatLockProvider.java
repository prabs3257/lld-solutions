package com.example.movieBooking.providers;

import com.example.movieBooking.CoreEntityClasses.Seat;
import com.example.movieBooking.CoreEntityClasses.Show;
import com.example.movieBooking.CoreEntityClasses.User;

import java.util.List;

public interface SeatLockProvider {

    void lockSeat(Show show, List<Seat> seats, User user);
    void unlockSeat(Show show, List<Seat> seats, User user);
    Boolean isValidLock(Show show, Seat seat);
}
