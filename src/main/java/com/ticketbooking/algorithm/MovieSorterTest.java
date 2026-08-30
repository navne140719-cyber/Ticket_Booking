package com.ticketbooking.algorithm;
import com.ticketbooking.entity.Movie;
import java.util.ArrayList;
import java.util.List;

public class MovieSorterTest {
    public static void main(String[] args) {
        Movie movie1 = new Movie(
                "Avatar",
                "PVR",
                "9:00 PM",
                100,
                50,
                400
        );

        Movie movie2 = new Movie(
                "Avengers",
                "INOX",
                "6:00 PM",
                100,
                40,
                150
        );

        Movie movie3 = new Movie(
                "Batman",
                "Cinepolis",
                "7:30 PM",
                100,
                30,
                250
        );

        Movie movie4 = new Movie(
                "Interstellar",
                "PVR",
                "8:00 PM",
                100,
                20,
                300
        );


        List<Movie> movies = new ArrayList<>();

        movies.add(movie1);
        movies.add(movie2);
        movies.add(movie3);
        movies.add(movie4);


        System.out.println("BEFORE SORTING:");

        for (Movie movie : movies) {

            System.out.println(
                    movie.getName()
                            + " - ₹"
                            + movie.getPrice()
            );
        }


        List<Movie> sortedMovies =
                MovieSorter.sortByPrice(movies);


        System.out.println("\nAFTER SORTING:");

        for (Movie movie : sortedMovies) {

            System.out.println(
                    movie.getName()
                            + " - ₹"
                            + movie.getPrice()
            );
        }
    }
}