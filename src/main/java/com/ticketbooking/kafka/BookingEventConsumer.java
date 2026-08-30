package com.ticketbooking.kafka;

import com.ticketbooking.event.BookingCreatedEvent;
import jakarta.annotation.PostConstruct;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookingEventConsumer {

    @PostConstruct
    public void testConsumerBean() {
        System.out.println("🔥🔥🔥 BOOKING CONSUMER BEAN CREATED 🔥🔥🔥");
    }

    @KafkaListener(
            topics = "booking-created",
            groupId = "ticket-booking-group-new",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeBookingCreatedEvent(BookingCreatedEvent event) {
        System.out.println("=================================");
        System.out.println("Booking Created Event Received!");
        System.out.println("Booking ID: " + event.getBookingId());
        System.out.println("User ID: " + event.getUserId());
        System.out.println("Movie ID: " + event.getMovieId());
        System.out.println("Seats: " + event.getSeats());
        System.out.println("Total Price: " + event.getTotalPrice());
        System.out.println("=================================");
    }
}


