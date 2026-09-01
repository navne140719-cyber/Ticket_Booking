package com.ticketbooking.kafka;
import com.ticketbooking.event.BookingCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingEventProducer {
    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;
    public BookingEventProducer(KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendBookingCreatedEvent(BookingCreatedEvent event) {
        kafkaTemplate.send(
                "booking-created",
                event.getBookingId().toString(),
                event
        );
    }
}