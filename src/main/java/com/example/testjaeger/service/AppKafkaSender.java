package com.example.testjaeger.service;

import com.example.testjaeger.service.task.SendTask;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class AppKafkaSender {

    private final SendTask sendTask;

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Value("${send-delay}")
    private Long delay;

    @EventListener(ApplicationStartedEvent.class)
    public void startSender() {
        threadPoolTaskScheduler.schedule(sendTask,
                new PeriodicTrigger(delay, TimeUnit.MILLISECONDS));
    }
}
