package org.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginAttemptDto implements Serializable {
    private String username;
    private String message;
    private Integer attempts;
    private Long timestamp;
}
