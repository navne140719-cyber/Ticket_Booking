package com.ticketbooking.kafka;

import com.ticketbooking.event.BookingCreatedEvent;
import com.ticketbooking.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookingNotificationConsumer {

    private final NotificationService notificationService;

    public BookingNotificationConsumer(
            NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "booking-created",
            groupId = "notification-group"
    )
    public void consumeBookingCreatedEvent(
            BookingCreatedEvent event) {

        System.out.println("=================================");
        System.out.println("NOTIFICATION CONSUMER");
        System.out.println("=================================");

        String message =
                "Your booking #" +
                        event.getBookingId() +
                        " for " +
                        event.getMovieName() +
                        " has been confirmed.";

        notificationService.createNotification(
                event.getUserId(),
                message
        );

        System.out.println(
                "Notification created for user: "
                        + event.getUserName()
        );

        System.out.println("=================================");
    }
}