package com.example.movieBooking.providers;

import com.example.movieBooking.CoreEntityClasses.Seat;
import com.example.movieBooking.CoreEntityClasses.SeatLock;
import com.example.movieBooking.CoreEntityClasses.Show;
import com.example.movieBooking.CoreEntityClasses.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SeatLockProviderImpl implements SeatLockProvider{

    private Map<Show, Map<Seat, SeatLock>> seatLockMap = new ConcurrentHashMap<>();

    @Override
    public void lockSeat(final Show show, final List<Seat> seats, final User user) {

        Map<Seat, SeatLock> seatLocks = seatLockMap.computeIfAbsent(show, k -> new ConcurrentHashMap<>());

        synchronized (seatLocks){

            for (Seat seat : seats) {
                if (seatLocks.containsKey(seat) && isValidLock(show, seat)) {
                    throw new IllegalStateException("Seat is already locked: " + seat.getSeatId());
                }
                SeatLock seatLock = new SeatLock(user, java.time.LocalDateTime.now().plusSeconds(2), show, seat);
                seatLocks.put(seat, seatLock);
            }
        }
    }

    @Override
    public void unlockSeat(final Show show, final List<Seat> seats, final User user) {

        Map<Seat, SeatLock> seatLocks = seatLockMap.computeIfAbsent(show, k -> new ConcurrentHashMap<>());

        synchronized (seatLocks){

            for (Seat seat : seats) {
                SeatLock seatLock = seatLocks.get(seat);
                if (seatLock == null) {
                    throw new IllegalStateException("Seat is not locked: " + seat);
                }
                if (!seatLock.getOwnerUser().equals(user)) {
                    throw new IllegalStateException("Seat is locked by another user: " + seat);
                }
                seatLocks.remove(seat);
            }
        }
    }

    @Override
    public Boolean isValidLock(final Show show, final Seat seat) {
        Map<Seat, SeatLock> seatLocks = seatLockMap.get(show);

        synchronized (seatLocks){
            SeatLock seatLock = seatLocks.get(seat);
            if (seatLock != null) {
                return seatLock.getExpiryTime().isAfter(java.time.LocalDateTime.now());
            }
        }
        return false;
    }
}
