package com.ticketbooking.algorithm;
import com.ticketbooking.entity.Movie;
import java.util.Comparator;
import java.util.List;

public class MovieSearch {
    public static Movie binarySearchById(List<Movie> movies, Long movieId) {
        // Binary search requires sorted data
        movies.sort(Comparator.comparing(Movie::getId));
        int low = 0;
        int high = movies.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            Long currentId = movies.get(mid).getId();
            if (currentId.equals(movieId)) return movies.get(mid);
            if (currentId < movieId) low = mid + 1;
             else high = mid - 1;
        }
        return null;
    }
}