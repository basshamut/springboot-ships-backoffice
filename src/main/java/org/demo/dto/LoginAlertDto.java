package org.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginAlertDto {
    private String username;
    private String ipAddress;
    private LocalDateTime timestamp;
    private int attemptCount;
    private String alertType;
    private String message;
}
