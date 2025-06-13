package org.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.demo.config.rabbitmq.ConfigureRabbitMq;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SenderService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String message) {
        log.info("Sending message...");
        rabbitTemplate.convertAndSend(ConfigureRabbitMq.queueName, message);
    }
}
