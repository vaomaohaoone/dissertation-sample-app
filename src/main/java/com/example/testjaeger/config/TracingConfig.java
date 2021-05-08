package com.example.testjaeger.config;

import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class TracingConfig {
    @Value("${jaeger.tracer.endpoint}")
    private String jaegerEndpoint;
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public Tracer tracer() {
        return io.jaegertracing.Configuration.fromEnv(applicationName)
                .withSampler(
                        io.jaegertracing.Configuration.SamplerConfiguration.fromEnv()
                                .withType(ConstSampler.TYPE)
                                .withParam(1))
                .withReporter(
                        io.jaegertracing.Configuration.ReporterConfiguration.fromEnv()
                                .withLogSpans(true)
                                .withFlushInterval(1000)
                                .withMaxQueueSize(10000)
                                .withSender(
                                        io.jaegertracing.Configuration.SenderConfiguration.fromEnv()
                                                .withEndpoint(jaegerEndpoint)
                                ))
                .getTracer();
    }

    @PostConstruct
    public void registerToGlobalTracer() {
        if (!GlobalTracer.isRegistered()) {
            GlobalTracer.register(tracer());
        }
    }
}
