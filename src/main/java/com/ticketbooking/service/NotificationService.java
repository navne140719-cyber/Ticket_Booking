package com.ticketbooking.service;
import com.ticketbooking.entity.Notification;
import com.ticketbooking.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(Long userId, String message) {
        Notification notification = new Notification(userId, message);
        notificationRepository.save(notification);
        System.out.println("Notification saved to database successfully!");
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository
                .findByUserIdOrderByIdDesc(userId);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"
                                ));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}