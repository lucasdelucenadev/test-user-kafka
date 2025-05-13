package com.example.notification.service;

import com.example.notification.model.Notification;
import com.example.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private static final String TOPIC = "notifications";

    @Autowired
    private NotificationRepository repository;

    @Autowired
    private KafkaTemplate<String, Notification> kafkaTemplate;

    @Override
    @Transactional
    public Notification createNotification(Notification notification) {
        logger.info("Salvando notificação no banco de dados: {}", notification);
        notification = repository.save(notification);
        logger.info("Enviando notificação para o Kafka. ID: {}", notification.getId());
        kafkaTemplate.send(TOPIC, notification.getId(), notification);
        return notification;
    }

    @Override
    public Page<Notification> getAllNotifications(Pageable pageable, Notification.NotificationStatus status) {
        logger.info("Buscando notificações com paginação e filtro. Status: {}", status);
        if (status != null) {
            return repository.findAllByStatus(status, pageable);
        } else {
            return repository.findAll(pageable);
        }
    }

    @Override
    public Notification getNotification(String id) {
        logger.info("Buscando notificação por ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
    }

    @Override
    @Transactional
    public Notification updateNotification(Notification notification) {
        logger.info("Atualizando notificação com ID: {}", notification.getId());
        if (repository.existsById(notification.getId())) {
            return repository.save(notification);
        }
        throw new RuntimeException("Notificação não encontrada");
    }

    @Override
    @Transactional
    public void deleteNotification(String id) {
        logger.info("Removendo notificação com ID: {}", id);
        repository.deleteById(id);
    }
} 