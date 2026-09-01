package com.ticketbooking.service;
import java.util.ArrayList;
import java.util.List;
import com.ticketbooking.entity.Booking;
import com.ticketbooking.entity.Movie;
import com.ticketbooking.kafka.BookingEventProducer;
import com.ticketbooking.repository.BookingRepository;
import com.ticketbooking.repository.MovieRepository;
import com.ticketbooking.exception.InsufficientSeatsException;
import com.ticketbooking.exception.BookingAccessDeniedException;
import com.ticketbooking.event.BookingCreatedEvent;
import com.ticketbooking.dto.BookingResponse;
import com.ticketbooking.algorithm.SeatManager;
import com.ticketbooking.algorithm.WaitingQueue;
import com.ticketbooking.algorithm.WaitingRequest;
import com.ticketbooking.algorithm.BookingStack;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    private final MovieRepository movieRepository;

    private final BookingEventProducer bookingEventProducer;

    private final SeatManager seatManager;

    private final WaitingQueue waitingQueue;

    private final BookingStack bookingStack;


    public BookingService(
            BookingRepository bookingRepository,
            MovieRepository movieRepository,
            BookingEventProducer bookingEventProducer,
            SeatManager seatManager,
            WaitingQueue waitingQueue,
            BookingStack bookingStack) {
        this.bookingRepository = bookingRepository;
        this.movieRepository = movieRepository;
        this.bookingEventProducer = bookingEventProducer;
        this.seatManager = seatManager;
        this.waitingQueue = waitingQueue;
        this.bookingStack = bookingStack;
    }

    // RESTORE BOOKINGS INTO STACK WHEN APPLICATION STARTS

    @PostConstruct
    public void loadBookingsIntoStack() {
        List<Booking> bookings = bookingRepository.findAllByOrderByIdAsc();
        for (Booking booking : bookings) bookingStack.push(booking);
        System.out.println("BOOKING STACK RESTORED");
        System.out.println("Total bookings loaded: " + bookingStack.size());
    }

    // GET MY BOOKINGS

    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(Long userId) {
        System.out.println("GETTING BOOKINGS FOR USER: " + userId);
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        System.out.println("BOOKINGS FOUND: " + bookings.size());
        List<BookingResponse> responses = new ArrayList<>();
        for (Booking booking : bookings) {
            System.out.println("Processing booking ID: " + booking.getId());
            Movie movie = movieRepository.findById(booking.getMovieId()).orElse(null);
            if (movie == null) {
                System.out.println("Movie not found: " + booking.getMovieId());
                continue;
            }
            BookingResponse response = new BookingResponse(
                            booking.getId(),
                            movie.getName(),
                            movie.getTheatre(),
                            movie.getShowTime(),
                            booking.getSeats(),
                            booking.getTotalPrice());
            responses.add(response);
        }
        System.out.println("BOOKING RESPONSES: " + responses.size());
        return responses;
    }

    // BOOK TICKET
    @Transactional
    public Booking bookTicket(Long userId, Long movieId, int seats, boolean fromQueue) {
        System.out.println("BOOKING TICKET");
        System.out.println("User ID: " + userId);
        System.out.println("Movie ID: " + movieId);
        System.out.println("Seats: " + seats);

        // VALIDATE SEATS

        if (seats <= 0) throw new IllegalArgumentException("Number of seats must be greater than 0");
        Movie movie = movieRepository.findByIdForUpdate(movieId).orElse(null);
        if (movie == null) {
            System.out.println("Movie not found");
            return null;
        }
        System.out.println("Movie: " + movie.getName());
        System.out.println("Available seats: " + movie.getAvailableSeats());

                        //      CHECK AVAILABLE SEATS

        if (movie.getAvailableSeats() < seats) {
            WaitingRequest request = new WaitingRequest(userId, movieId, seats);
            waitingQueue.addToQueue(movieId, request);
            throw new InsufficientSeatsException("Not enough seats available. " + "You have been added to the waiting list."
            );
        }

        // ALLOCATE SEATS

        List<Integer> selectedSeats = seatManager.bookSeats(movieId, seats);
        System.out.println("Selected seats: " + selectedSeats);

        // CALCULATE PRICE

        double totalPrice = seats * movie.getPrice();

        // REDUCE AVAILABLE SEATS

        movie.setAvailableSeats(movie.getAvailableSeats() - seats);
        movieRepository.save(movie);

        System.out.println("Remaining seats: " + movie.getAvailableSeats());

        // CREATE BOOKING

        Booking booking = new Booking(userId, movieId, seats, totalPrice);
        Booking savedBooking = bookingRepository.save(booking);
        System.out.println("Booking created: " + savedBooking.getId());

        // ADD BOOKING TO STACK

        bookingStack.push(savedBooking);

        // CREATE KAFKA EVENT

        BookingCreatedEvent event = new BookingCreatedEvent(
                        savedBooking.getId(),
                        savedBooking.getUserId(),
                        savedBooking.getMovieId(),
                        savedBooking.getSeats(),
                        savedBooking.getTotalPrice()
                );

        bookingEventProducer.sendBookingCreatedEvent(event);
        System.out.println("Kafka booking event sent");
        System.out.println("=================================");
        return savedBooking;
    }

    // =========================================================
    // CANCEL BOOKING
    // =========================================================

    @Transactional
    public void cancelBooking(Long bookingId, Long userId) {
        System.out.println("CANCELLING BOOKING");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("User ID: " + userId);

        // =====================================================
        // FIND BOOKING
        // =====================================================

        Booking booking =
                bookingRepository
                        .findById(bookingId)
                        .orElse(null);


        if (booking == null) {

            System.out.println(
                    "Booking not found"
            );

            return;
        }


        // =====================================================
        // CHECK USER OWNERSHIP
        // =====================================================

        if (!booking.getUserId().equals(userId)) throw new BookingAccessDeniedException("You are not allowed to cancel this booking");
        Long movieId = booking.getMovieId();
        Movie movie = movieRepository.findByIdForUpdate(movieId).orElse(null);
        if (movie == null) {
            System.out.println("Movie not found");
            return;
        }

        // =====================================================
        // RETURN SEATS
        // =====================================================

        movie.setAvailableSeats(movie.getAvailableSeats() + booking.getSeats());
        movieRepository.save(movie);
        System.out.println("Seats returned: " + booking.getSeats());
        System.out.println("Available seats now: " + movie.getAvailableSeats());

        // DELETE BOOKING

        bookingRepository.delete(booking);

        // REMOVE FROM STACK

        bookingStack.removeBooking(bookingId);

        // CHECK WAITING QUEUE

        WaitingRequest nextRequest = waitingQueue.getNext(movieId);
        System.out.println("CHECKING WAITING QUEUE");
        System.out.println("Movie ID: " + movieId);
        System.out.println("Available Seats: " + movie.getAvailableSeats());
        System.out.println("Waiting Queue: " + waitingQueue.getWaitingList(movieId));

        // NO WAITING USER

        if (nextRequest == null) {
            System.out.println("NO USER IN WAITING QUEUE");
            return;
        }
        System.out.println("NEXT USER: " + nextRequest.getUserId());
        System.out.println("REQUESTED SEATS: " + nextRequest.getSeats());


        // PROCESS WAITING USEr

        if (movie.getAvailableSeats() >= nextRequest.getSeats()) {
            /*
             * Remove the request only when
             * enough seats are available.
             */

            waitingQueue.removeNext(movieId);
            System.out.println("Processing waiting request");
            System.out.println("User: " + nextRequest.getUserId());

            // =================================================
            // CREATE BOOKING FOR WAITING USER
            // =================================================

            bookTicket(
                    nextRequest.getUserId(),
                    nextRequest.getMovieId(),
                    nextRequest.getSeats(),
                    true
            );
        }
    }
}