package com.example.testjaeger.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties приложения для работы с Kafka
 */
@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaAppProperties {
    /**
     * Имя Kafka request топика
     */
    private String requestTopic;
    /**
     * Имя Kafka response топика
     */
    private String responseTopic;
    /**
     * Id Kafka продьюсера со вторым типом входных данных
     */
    private String producerId;
    /**
     * Id Kafka консьюмер группы
     */
    private String consumerGroupId;
    /**
     * Список Kafka брокеров
     */
    private String bootstrapServers;
}
