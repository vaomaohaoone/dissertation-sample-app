package com.example.testjaeger.service.task;

import com.example.testjaeger.properties.KafkaAppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class SendTask implements Runnable {

    private final KafkaAppProperties kafkaProperties;

    private final KafkaTemplate<Long, String> kafkaTemplate;

    private final String message;

    public SendTask(KafkaAppProperties kafkaAppProperties, KafkaTemplate<Long, String> kafkaTemplate) {
        this.kafkaProperties = kafkaAppProperties;
        this.kafkaTemplate = kafkaTemplate;
        this.message = String.format("Message from: %s", kafkaAppProperties.getProducerId());
    }

    @Override
    public void run() {
        log.debug("=== was send a message: {} ===", message);
        kafkaTemplate.send(kafkaProperties.getResponseTopic(), message);
    }
}
