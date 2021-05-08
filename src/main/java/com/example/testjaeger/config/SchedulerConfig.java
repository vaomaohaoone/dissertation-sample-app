package com.example.testjaeger.config;

import com.example.testjaeger.properties.KafkaAppProperties;
import com.example.testjaeger.service.task.SendTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        return threadPoolTaskScheduler;
    }

    @Bean
    public SendTask sendTask(KafkaAppProperties kafkaAppProperties, KafkaTemplate<Long, String> kafkaTemplate) {
        return new SendTask(kafkaAppProperties, kafkaTemplate);
    }
}
