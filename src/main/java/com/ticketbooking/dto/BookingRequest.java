package com.ticketbooking.dto;

public class BookingRequest {

    private Long movieId;
    private int seats;

    public BookingRequest() {
    }

    public Long getMovieId() {
        return movieId;
    }

    public int getSeats() {
        return seats;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}