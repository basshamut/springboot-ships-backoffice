package org.demo.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.dto.LoginAttemptDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static org.demo.utils.Constants.LOGIN_ATTEMPTS_TOPIC;
import static org.demo.utils.Constants.SHIPS_CONSUMER_GROUP;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaReceiverService {
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = LOGIN_ATTEMPTS_TOPIC, groupId = SHIPS_CONSUMER_GROUP)
    public void loginAttempsReceive(String message) {
        try {
            LoginAttemptDto loginAttempt = objectMapper.readValue(message, LoginAttemptDto.class);
            log.info("Received login attempt message: User={}, Attempts={}, Timestamp={}", loginAttempt.getUsername(), loginAttempt.getAttempts(), loginAttempt.getTimestamp());
        } catch (JsonProcessingException e) {
            log.error("Error deserializing message from JSON: {}", e.getMessage());
            log.error("Original message: {}", message);
        }
    }
}
