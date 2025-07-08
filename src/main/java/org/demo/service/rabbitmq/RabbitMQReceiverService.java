package org.demo.service.rabbitmq;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.dto.AuditEventDto;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Getter
@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMQReceiverService {
    private final CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(AuditEventDto message) {
        log.info("Received message: {}", message);
        latch.countDown();
    }

}
