package com.ticketbooking.dto;

public class BookingResponse {

    private Long bookingId;
    private String movieName;
    private String theatre;
    private String showTime;
    private int seats;
    private double totalPrice;

    public BookingResponse(
            Long bookingId,
            String movieName,
            String theatre,
            String showTime,
            int seats,
            double totalPrice) {

        this.bookingId = bookingId;
        this.movieName = movieName;
        this.theatre = theatre;
        this.showTime = showTime;
        this.seats = seats;
        this.totalPrice = totalPrice;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getTheatre() {
        return theatre;
    }

    public String getShowTime() {
        return showTime;
    }

    public int getSeats() {
        return seats;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}