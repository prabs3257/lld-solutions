package com.example.movieBooking.Services;

import com.example.movieBooking.CoreEntityClasses.Booking;
import com.example.movieBooking.CoreEntityClasses.Seat;
import com.example.movieBooking.CoreEntityClasses.Show;
import com.example.movieBooking.CoreEntityClasses.User;
import com.example.movieBooking.providers.SeatLockProvider;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BookingService {

    private Map<UUID, List<Booking>> showBookingsMap = new ConcurrentHashMap<>();
    private SeatLockProvider seatLockProvider;

    private static BookingService instance;

    private BookingService(final SeatLockProvider seatLockProvider) {
        this.seatLockProvider = seatLockProvider;
    }

    public static synchronized BookingService getInstance(SeatLockProvider seatLockProvider) {
        if (instance == null) {
            instance = new BookingService(seatLockProvider);
        }
        return instance;
    }

    public Booking createBooking(Show show, List<Integer> seatIds, User user) {
        // Implementation for creating a booking

        List<Seat> seats = show.getScreen().getSeats().stream()
                .filter(seat -> seatIds.contains(seat.getSeatId()))
                .collect(Collectors.toList());

        List<Seat> alreadyBookedSeats = Optional
                .ofNullable(showBookingsMap.get(show.getShowId()))
                .orElse(Collections.emptyList())
                .stream()
                .filter(booking -> booking != null && booking.isPaymentDone())
                .map(Booking::getSeats)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        if (alreadyBookedSeats.stream().anyMatch(seat -> seats.contains(seat))) {
            throw new IllegalStateException("One or more seats are already booked.");
        }

        try{
            seatLockProvider.lockSeat(show,seats,user);
            int totalPrice = seats.stream().map(Seat::getPrice).reduce(0, Integer::sum);
            UUID bookingId = UUID.randomUUID();

            System.out.println("User " + user.getName() + " has created a booking id: " + bookingId +" for show " + show.getShowId() + " " +
                    "with seats " + seatIds +
                    " and total price: " + totalPrice);

            Booking newBooking = new Booking( show, seats, user, bookingId, false, totalPrice);
            showBookingsMap.computeIfAbsent(show.getShowId(), k -> new ArrayList<>()).add(newBooking);

            return newBooking;
        }catch (Exception e){
            System.out.println(user.getName() + " couldnt create booking, reason : " + e.getMessage());
            return null;
        }


    }

    public void confirmBooking(Booking booking) {

        Optional<Seat> seatNotLocked = Optional.ofNullable(booking.getSeats()).orElse(Collections.emptyList()).stream()
                .filter(seat -> !seatLockProvider.isValidLock(booking.getShow(), seat))
                .findFirst();


        if(seatNotLocked.isPresent()){
            System.out.println("Some seats are not locked for booking " + booking.getBookingId() + ". Cannot confirm booking.");
            return;
        }
        booking.setPaymentDone(true);
        System.out.println("Booking with id: " + booking.getBookingId() + " by " + booking.getUser().getName() + " has been confirmed" +
                " " +
                "successfully.");

    }

    public void removeBooking(Booking booking) {
        List<Booking> bookings = showBookingsMap.get(booking.getShow().getShowId());
        if (bookings != null) {
            bookings.remove(booking);
        }
    }
}
