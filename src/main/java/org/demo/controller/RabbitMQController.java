package org.demo.controller;

import lombok.RequiredArgsConstructor;
import org.demo.service.RabbitMQSenderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RabbitMQController {

    private final RabbitMQSenderService rabbitMQSenderService;

    @GetMapping("rabbitmq-broker/send")
    public String sendMessage(@RequestParam String message) {
        rabbitMQSenderService.sendMessage(message);
        return "Message sent: " + message;
    }
}
