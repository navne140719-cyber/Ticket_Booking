package com.ticketbooking.algorithm;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SeatManager {

    // movieId -> booked seat numbers
    private final Map<Long, Set<Integer>> bookedSeats = new HashMap<>();

    public List<Integer> bookSeats(Long movieId, int numberOfSeats) {
        Set<Integer> seats = bookedSeats.computeIfAbsent(movieId, k -> new HashSet<>());
        List<Integer> selectedSeats = new ArrayList<>();
        int seatNumber = 1;
        while (selectedSeats.size() < numberOfSeats) {
            if (!seats.contains(seatNumber)) {
                seats.add(seatNumber);
                selectedSeats.add(seatNumber);
            }
            seatNumber++;
        }
        return selectedSeats;
    }

    public boolean isSeatBooked(Long movieId, int seatNumber) {
        Set<Integer> seats = bookedSeats.get(movieId);
        if (seats == null) {
            return false;
        }
        return seats.contains(seatNumber);
    }

    public void cancelSeat(Long movieId, int seatNumber) {
        Set<Integer> seats = bookedSeats.get(movieId);
        if (seats != null) {
            seats.remove(seatNumber);
        }
    }
}



