package com.example.testjaeger.config;

import com.example.testjaeger.properties.KafkaAppProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс конфигурации Kafka
 */
@Configuration
@EnableKafka
@RequiredArgsConstructor
@EnableConfigurationProperties(value = KafkaAppProperties.class)
public class KafkaAppConfig {

    private final KafkaAppProperties kafkaAppProperties;

    /**
     * Бин создания KafkaAdmin клиента
     * @return KafkaAdmin объект
     */
    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAppProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    /**
     * Бин создания (если не существует) request топика
     * Количество партиций будет устновлено по умолчанию в 1, количество реплик 1 (since kafka 2.6)
     * @return NewTopic объект
     */
    @Bean
    public NewTopic requestTopic() {
        return TopicBuilder.name(kafkaAppProperties.getRequestTopic())
                .build();
    }

    /**
     * Бин создания (если не существует) response топика
     * Количество партиций будет устновлено по умолчанию в 1, количество реплик 1 (since kafka 2.6)
     * @return NewTopic объект
     */
    @Bean
    public NewTopic responseTopic() {
        return TopicBuilder.name(kafkaAppProperties.getResponseTopic())
                .build();
    }

    /**
     * Бин создания ProducerFactory для конфигурации записи данных в Kafka
     * @return ProducerFactory<Long, String> объект
     */
    @Bean
    public ProducerFactory<Long, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(getProducerConfig(kafkaAppProperties.getProducerId()));
    }


    /**
     * Бин создания ContainerFactory
     * @return ConcurrentKafkaListenerContainerFactory<String, String>
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> containerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerProperties()));
        return factory;
    }

    /**
     * Бин создания KafkaTemplate для записи данных в Kafka
     * @return KafkaTemplate<Long, String> объект
     */
    @Bean
    public KafkaTemplate<Long, String> kafkaTemplate() {
        return new KafkaTemplate<Long, String>(producerFactory());
    }

    /**
     * Бин создания конфигурации Kafka consumer
     * @return Properties объект
     */
    @Bean
    public Map<String, Object> consumerProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAppProperties.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaAppProperties.getConsumerGroupId());
        return props;
    }

    /**
     * Метод создания конфигурации Kafka producer
     * @param clientId - producer kafka id
     * @return - map с конфигурацией Kafka producer
     */
    private Map<String, Object> getProducerConfig(String clientId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAppProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        return props;
    }
}
