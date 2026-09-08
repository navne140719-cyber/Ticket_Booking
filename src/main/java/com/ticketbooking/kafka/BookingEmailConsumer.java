package com.ticketbooking.kafka;
import com.ticketbooking.event.BookingCreatedEvent;
import com.ticketbooking.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookingEmailConsumer {
    private final EmailService emailService;
    public BookingEmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }
    @KafkaListener(
            topics = "booking-created",
            groupId = "email-group"
    )

    public void consumeBookingCreatedEvent(BookingCreatedEvent event) {
        System.out.println("=================================");
        System.out.println("EMAIL CONSUMER");
        System.out.println("=================================");
        System.out.println("Preparing booking confirmation email...");
        System.out.println("Booking ID: " + event.getBookingId());
        System.out.println("User: " + event.getUserName());
        System.out.println("Email: " + event.getUserEmail());
        System.out.println("Movie: " + event.getMovieName());
        System.out.println("Seats: " + event.getSeats());
        System.out.println("Total Price: " + event.getTotalPrice());
        emailService.sendBookingConfirmation(
                event.getUserEmail(),
                event.getUserName(),
                event.getMovieName(),
                event.getSeats(),
                event.getTotalPrice()
        );
        System.out.println("REAL EMAIL SENT SUCCESSFULLY!");
        System.out.println("=================================");
    }
}