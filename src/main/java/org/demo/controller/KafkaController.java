package org.demo.controller;

import lombok.RequiredArgsConstructor;
import org.demo.service.KafkaSenderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {
    private final KafkaSenderService kafkaSenderService;

    @GetMapping("/kafka-broker/send")
    public String sendMessage(String topic, String message) {
        kafkaSenderService.sendMessage(topic, message);
        return "Message sent to topic " + topic + ": " + message;
    }
}
