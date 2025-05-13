package com.example.notification.service;

import com.example.notification.model.Notification;
import com.example.notification.model.Notification.NotificationStatus;
import com.example.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @Mock
    private NotificationRepository repository;
    @Mock
    private KafkaTemplate<String, Notification> kafkaTemplate;
    @InjectMocks
    private NotificationServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNotification() {
        Notification notification = new Notification();
        notification.setMessage("Teste");
        notification.setRecipient("teste@exemplo.com");
        notification.setStatus(NotificationStatus.PENDING);

        when(repository.save(any(Notification.class))).thenReturn(notification);

        Notification created = service.createNotification(notification);
        assertEquals("Teste", created.getMessage());
        assertEquals("teste@exemplo.com", created.getRecipient());
        verify(kafkaTemplate, times(1)).send(anyString(), anyString(), any(Notification.class));
    }

    @Test
    void testGetNotification() {
        Notification notification = new Notification();
        notification.setId("1");
        when(repository.findById("1")).thenReturn(Optional.of(notification));
        Notification found = service.getNotification("1");
        assertEquals("1", found.getId());
    }
} 