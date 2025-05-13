package com.example.notification.service;

import com.example.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface NotificationService {
    Notification createNotification(Notification notification);
    Page<Notification> getAllNotifications(Pageable pageable, Notification.NotificationStatus status);
    Notification getNotification(String id);
    Notification updateNotification(Notification notification);
    void deleteNotification(String id);
} 