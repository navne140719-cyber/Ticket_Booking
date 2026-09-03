package com.ticketbooking.controller;

import com.ticketbooking.dto.MovieResponse;
import com.ticketbooking.entity.Movie;
import com.ticketbooking.service.MovieService;
import org.springframework.web.bind.annotation.*;
import com.ticketbooking.algorithm.MovieSorter;
import java.util.List;

@RestController
@RequestMapping("/movies")
@CrossOrigin(origins = "https://glittery-empanada-015d82.netlify.app")
public class MovieController {
    private final MovieService movieService;
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }
    @PostMapping
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.createMovie(movie);
    }
    @GetMapping
    public List<MovieResponse> getAllMovies() {
        return movieService.getAllMovies();
    }
    @PutMapping("/{id}")
    public Movie updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        return movieService.updateMovie(id, movie);
    }
    @DeleteMapping("/{id}")
    public String deleteMovie(@PathVariable Long id) {
        boolean deleted = movieService.deleteMovie(id);
        if (!deleted) return "Movie not found";
        return "Movie deleted successfully";
    }
    @GetMapping("/{id}")
    public MovieResponse getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }
    @GetMapping("/sort/price")
    public List<MovieResponse> getMoviesSortedByPrice() {
        return movieService.getMoviesSortedByPrice();
    }
    @GetMapping("/search")
    public List<MovieResponse> searchMovies(@RequestParam String name) {
        return movieService.searchMovies(name);
    }
    @GetMapping("/filter")
    public List<MovieResponse> filterMovies(@RequestParam double minPrice, @RequestParam double maxPrice) {
        return movieService.filterMoviesByPrice(
                minPrice,
                maxPrice
        );
    }
}


