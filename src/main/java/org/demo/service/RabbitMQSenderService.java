package org.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.config.rabbitmq.ConfigureRabbitMq;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMQSenderService {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {
        log.info("Sending message...");
        rabbitTemplate.convertAndSend(ConfigureRabbitMq.queueName, message);
    }
}
