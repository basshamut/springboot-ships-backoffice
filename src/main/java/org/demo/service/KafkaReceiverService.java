package org.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
public class KafkaReceiverService {
    @KafkaListener(topics = "test", groupId = "ships-consumer-group")
    public void receive(String message) {
        log.info("Received message: {}", message);
    }
}
