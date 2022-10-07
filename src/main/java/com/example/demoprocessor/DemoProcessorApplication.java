package com.example.demoprocessor;

import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.cloud.stream.binder.kafka.properties.KafkaBinderConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.EnableIntegration;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableIntegration
public class DemoProcessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoProcessorApplication.class, args);
    }


    @Bean
    public KafkaBinderConfigurationProperties configurationProperties(KafkaProperties kafkaProperties) {
        return new KafkaBinderConfigurationProperties(kafkaProperties);
    }
}
