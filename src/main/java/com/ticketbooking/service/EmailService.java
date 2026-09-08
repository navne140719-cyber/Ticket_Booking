package com.ticketbooking.service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBookingConfirmation(String to, String name, String movieName, int seats, double totalPrice) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Booking Confirmation - " + movieName);
        message.setText(
                "Hello " + name + ",\n\n" +
                        "Your movie booking has been confirmed successfully!\n\n" +
                        "Booking Details:\n" +
                        "-------------------------\n" +
                        "Movie: " + movieName + "\n" +
                        "Seats: " + seats + "\n" +
                        "Total Price: ₹" + totalPrice + "\n" +
                        "-------------------------\n\n" +
                        "Thank you for booking with us!\n\n" +
                        "Ticket Booking System"
        );
        mailSender.send(message);
    }
}
