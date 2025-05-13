package com.example.notification.worker;

import com.example.notification.model.Notification;
import com.example.notification.repository.NotificationRepository;
import com.example.notification.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class NotificationWorker {

    private static final Logger logger = LoggerFactory.getLogger(NotificationWorker.class);

    @Autowired
    private NotificationRepository repository;
    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "notifications", groupId = "notification-group")
    @Transactional
    public void processNotification(Notification notification) {
        logger.info("Recebida notificação do Kafka para processamento. ID: {}", notification.getId());
        try {
            // Simula processamento da notificação
            Thread.sleep(1000);
            logger.info("Processando notificação: {}", notification);
            
            // Atualiza o status e adiciona timestamp de processamento
            notification.setStatus(Notification.NotificationStatus.PROCESSED);
            notification.setProcessedAt(LocalDateTime.now());
            
            // Salva a notificação processada
            repository.save(notification);
            
            logger.info("Notificação processada e salva com sucesso. ID: {}", notification.getId());
            // Envia e-mail
            emailService.sendNotificationEmail(
                notification.getRecipient(),
                "Notificação processada",
                "Sua notificação foi processada com sucesso! Mensagem: " + notification.getMessage()
            );
            logger.info("E-mail enviado para {}", notification.getRecipient());
        } catch (InterruptedException e) {
            notification.setStatus(Notification.NotificationStatus.FAILED);
            repository.save(notification);
            logger.error("Erro ao processar notificação: {}", notification.getId(), e);
            Thread.currentThread().interrupt();
        }
    }
} 