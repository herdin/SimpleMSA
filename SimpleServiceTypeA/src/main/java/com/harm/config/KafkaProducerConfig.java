package com.harm.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${kafka.url}")
    private String bootstrapAddress;
    public static final String KAFKA_TEST_TOPIC = "test";
    @Bean
    public ProducerFactory<String, KafkaData> kafkaDataProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, KafkaData> kafkaDataKafkaTemplate() {
        return new KafkaTemplate<>(kafkaDataProducerFactory());
    }

    public static class KafkaData {
        Long num;
        String message;

        public Long getNum() {
            return num;
        }

        public void setNum(Long num) {
            this.num = num;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "KafkaData{" +
                    "num=" + num +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}