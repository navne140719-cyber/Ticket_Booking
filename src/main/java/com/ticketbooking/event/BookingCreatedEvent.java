package com.ticketbooking.event;

public class BookingCreatedEvent {
    private Long bookingId;
    private Long userId;
    private Long movieId;
    private int seats;
    private double totalPrice;

    public BookingCreatedEvent() {}

    public BookingCreatedEvent(Long bookingId, Long userId, Long movieId, int seats, double totalPrice) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.movieId = movieId;
        this.seats = seats;
        this.totalPrice = totalPrice;
    }

    public Long getBookingId() {
        return bookingId;
    }
    public Long getUserId() {
        return userId;
    }
    public Long getMovieId() {
        return movieId;
    }
    public int getSeats() {
        return seats;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
}