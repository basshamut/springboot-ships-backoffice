package org.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.demo.dto.LoginRequestJson;
import org.demo.service.JwtService;
import org.demo.service.kafka.KafkaReceiverService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.demo.utils.Constants.API_VERSION_PATH;

@RestController
@RequestMapping(value = API_VERSION_PATH)
@Validated
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

    private final JwtService jwtService;
    private final KafkaReceiverService kafkaReceiverService;

    @PostMapping("/login")
    public ResponseEntity<JsonNode> login(@RequestBody @NotNull LoginRequestJson loginRequestJson) throws JsonProcessingException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestJson.getUsername(), loginRequestJson.getPassword()));
        if (authentication.isAuthenticated()) {
            var token = jwtService.generateToken(authentication);
            var responseMapped = objectMapper.createObjectNode();

            responseMapped.put("token", token);
            responseMapped.put("type", "Bearer");

            return ResponseEntity.ok().body(responseMapped);
        } else {
            throw new UsernameNotFoundException("invalid user request");
        }
    }
}
