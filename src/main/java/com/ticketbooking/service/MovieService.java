package com.ticketbooking.service;

import com.ticketbooking.entity.Movie;
import com.ticketbooking.repository.MovieRepository;
import com.ticketbooking.dto.MovieResponse;
import com.ticketbooking.algorithm.MovieSorter;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    // Convert Movie entity -> MovieResponse
    private MovieResponse convertToResponse(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getName(),
                movie.getTheatre(),
                movie.getShowTime(),
                movie.getTotalSeats(),
                movie.getAvailableSeats(),
                movie.getPrice(),
                movie.getPosterUrl()
        );
    }

    public List<MovieResponse> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        List<MovieResponse> responses = new ArrayList<>();
        for (Movie movie : movies) responses.add(convertToResponse(movie));
        return responses;
    }

    public MovieResponse getMovieById(Long id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie == null) return null;
        return convertToResponse(movie);
    }

    public List<MovieResponse> searchMovies(String name) {
        List<Movie> movies = movieRepository.findByNameContainingIgnoreCase(name);
        List<MovieResponse> responses = new ArrayList<>();
        for (Movie movie : movies) {
            responses.add(convertToResponse(movie));
        }
        return responses;
    }

    public List<MovieResponse> filterMoviesByPrice(double minPrice, double maxPrice) {
        List<Movie> movies = movieRepository.findByPriceBetween(minPrice, maxPrice);
        List<MovieResponse> responses = new ArrayList<>();
        for (Movie movie : movies) {
            responses.add(convertToResponse(movie));
        }
        return responses;
    }

    public List<MovieResponse> getMoviesSortedByPrice() {
        List<Movie> movies = movieRepository.findAll();
        List<Movie> sortedMovies = MovieSorter.sortByPrice(movies);
        List<MovieResponse> responses = new ArrayList<>();
        for (Movie movie : sortedMovies) {
            responses.add(convertToResponse(movie));
        }
        return responses;
    }
}