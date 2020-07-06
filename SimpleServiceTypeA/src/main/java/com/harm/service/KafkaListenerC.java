package com.harm.service;

import com.harm.config.KafkaProducerConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ConsumerAwareMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

//@Component
public class KafkaListenerC implements ConsumerAwareMessageListener<String, KafkaProducerConfig.KafkaData> {
    Logger logger = LoggerFactory.getLogger(KafkaListenerC.class);
    @KafkaListener(topics = {KafkaProducerConfig.KAFKA_TEST_TOPIC}, containerFactory = "kafkaDataKafkaListenerContainerFactory")
    @Override
    public void onMessage(ConsumerRecord<String, KafkaProducerConfig.KafkaData> data, Consumer<?, ?> consumer) {
        logger.debug("recv -> {}", data);
        logger.debug("recv -> {}", data.value());
        logger.debug("recv -> {}", consumer);
    }
}
