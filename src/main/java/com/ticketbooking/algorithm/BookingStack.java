package com.ticketbooking.algorithm;
import com.ticketbooking.entity.Booking;
import java.util.Stack;
import org.springframework.stereotype.Component;

@Component
public class BookingStack {
    private final Stack<Booking> bookingStack = new Stack<>();
    public void push(Booking booking) {
        bookingStack.push(booking);
        System.out.println("=================================");
        System.out.println("BOOKING ADDED TO STACK");
        System.out.println("Booking ID: " + booking.getId());
        System.out.println("Movie ID: " + booking.getMovieId());
        System.out.println("User ID: " + booking.getUserId());
        System.out.println("Stack Size: " + bookingStack.size());
        System.out.println("=================================");
    }

    public void removeBooking(Long bookingId) {
        bookingStack.removeIf(
                booking -> booking.getId().equals(bookingId)
        );
    }

    public Booking peek() {
        if (bookingStack.isEmpty()) {
            return null;
        }
        return bookingStack.peek();
    }

    public Booking pop() {
        if (bookingStack.isEmpty()) return null;
        return bookingStack.pop();
    }

    public boolean isEmpty() {
        return bookingStack.isEmpty();
    }

    public int size() {
        return bookingStack.size();
    }
}
