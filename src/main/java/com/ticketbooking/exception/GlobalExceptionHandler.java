package com.ticketbooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientSeatsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInsufficientSeats(
            InsufficientSeatsException exception) {

        return exception.getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(
            IllegalArgumentException exception) {

        return exception.getMessage();
    }

    @ExceptionHandler(BookingAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleBookingAccessDenied(
            BookingAccessDeniedException exception) {

        return exception.getMessage();
    }
}