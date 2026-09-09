package com.ticketbooking.kafka;

import com.ticketbooking.event.BookingCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingEventProducer {

    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;

    @Value("${KAFKA_ENABLED:true}")
    private boolean kafkaEnabled;

    public BookingEventProducer(
            KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBookingCreatedEvent(BookingCreatedEvent event) {

        if (!kafkaEnabled) {
            System.out.println(
                    "Kafka is disabled. Skipping booking event."
            );
            return;
        }

        try {

            kafkaTemplate.send(
                    "booking-created",
                    event.getBookingId().toString(),
                    event
            );

            System.out.println(
                    "Kafka event submitted successfully"
            );

        } catch (Exception e) {

            System.out.println(
                    "Kafka unavailable. Booking will continue."
            );

            System.out.println(
                    "Kafka error: " + e.getMessage()
            );
        }
    }
}