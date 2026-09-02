package com.ticketbooking.dto;

public class MovieResponse {

    private Long id;
    private String name;
    private String theatre;
    private String showTime;
    private int totalSeats;
    private int availableSeats;
    private double price;
    private String posterUrl;

    public MovieResponse() {}
    public MovieResponse(
            Long id,
            String name,
            String theatre,
            String showTime,
            int totalSeats,
            int availableSeats,
            double price,
            String posterUrl) {

        this.id = id;
        this.name = name;
        this.theatre = theatre;
        this.showTime = showTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.price = price;
        this.posterUrl = posterUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTheatre() {
        return theatre;
    }

    public String getShowTime() {
        return showTime;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public double getPrice() {
        return price;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTheatre(String theatre) {
        this.theatre = theatre;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}