package com.example.testjaeger.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppKafkaListener {

    @KafkaListener(topics = "${kafka.request-topic}", containerFactory = "containerFactory")
    public void onMessage(String message) {
        log.debug("Consumed message: " + message);
    }
}
