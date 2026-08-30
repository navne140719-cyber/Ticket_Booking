package com.ticketbooking.algorithm;

public class WaitingRequest {

    private Long userId;
    private Long movieId;
    private int seats;

    public WaitingRequest(Long userId, Long movieId, int seats) {
        this.userId = userId;
        this.movieId = movieId;
        this.seats = seats;
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

    @Override
    public String toString() {
        return "WaitingRequest{" +
                "userId=" + userId +
                ", movieId=" + movieId +
                ", seats=" + seats +
                '}';
    }
}