package org.demo.service.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.config.rabbitmq.ConfigureRabbitMq;
import org.demo.dto.AuditEventDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static org.demo.utils.Constants.AUDIT_QUEUE;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMQSenderService {
    private final RabbitTemplate rabbitTemplate;

    public void sendAuditMessage(AuditEventDto message) {
        rabbitTemplate.convertAndSend(AUDIT_QUEUE, message);
    }
}
