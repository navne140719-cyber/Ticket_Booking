package com.ticketbooking.event;

public class BookingCreatedEvent {

    private Long bookingId;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long movieId;
    private String movieName;
    private int seats;
    private double totalPrice;

    public BookingCreatedEvent() {}
    public BookingCreatedEvent(
            Long bookingId,
            Long userId,
            String userName,
            String userEmail,
            Long movieId,
            String movieName,
            int seats,
            double totalPrice
    ) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.movieId = movieId;
        this.movieName = movieName;
        this.seats = seats;
        this.totalPrice = totalPrice;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Long getMovieId() {
        return movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public int getSeats() {
        return seats;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}