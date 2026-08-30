package com.ticketbooking.algorithm;
import com.ticketbooking.entity.Movie;
import java.util.ArrayList;
import java.util.List;

public class MovieSorter {
    public static List<Movie> sortByPrice(List<Movie> movies) {
        List<Movie> sortedMovies = new ArrayList<>(movies);
        int n = sortedMovies.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (sortedMovies.get(j).getPrice() > sortedMovies.get(j + 1).getPrice()) {
                    Movie temp = sortedMovies.get(j);
                    sortedMovies.set(j, sortedMovies.get(j + 1));
                    sortedMovies.set(j + 1, temp);
                }
            }
        }
        return sortedMovies;
    }
}