package com.example.notification.repository;

import com.example.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    Page<Notification> findAllByStatus(Notification.NotificationStatus status, Pageable pageable);
} 