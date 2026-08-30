package com.ticketbooking.repository;

import com.ticketbooking.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Search movies by name
    List<Movie> findByNameContainingIgnoreCase(String name);

    // Filter movies by price
    List<Movie> findByPriceBetween(
            double minPrice,
            double maxPrice
    );

    // Normal find - used for READ operations
    @Override
    Optional<Movie> findById(Long id);

    // Locked find - used when modifying available seats
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM Movie m WHERE m.id = :id")
    Optional<Movie> findByIdForUpdate(
            @Param("id") Long id
    );
}