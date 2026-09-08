package com.ticketbooking.controller;

import com.ticketbooking.entity.Notification;
import com.ticketbooking.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}")
    public List<Notification> getNotifications(
            @PathVariable Long userId) {

        return notificationService.getUserNotifications(userId);
    }

    @PutMapping("/{notificationId}/read")
    public String markAsRead(
            @PathVariable Long notificationId) {

        notificationService.markAsRead(notificationId);

        return "Notification marked as read";
    }
}