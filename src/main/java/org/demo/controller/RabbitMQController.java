package org.demo.controller;

import lombok.RequiredArgsConstructor;
import org.demo.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RabbitMQController {

    private final SenderService senderService;

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        senderService.send(message);
        return "Message sent: " + message;
    }
}
