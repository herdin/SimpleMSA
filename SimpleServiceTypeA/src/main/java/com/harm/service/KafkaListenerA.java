package com.harm.service;

import com.harm.config.KafkaProducerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

//@Component
public class KafkaListenerA implements MessageListener<String, KafkaProducerConfig.KafkaData> {
    Logger logger = LoggerFactory.getLogger(KafkaListenerA.class);

    @KafkaListener(topics = {KafkaProducerConfig.KAFKA_TEST_TOPIC}, containerFactory = "kafkaDataKafkaListenerContainerFactory")
    @Override
    public void onMessage(ConsumerRecord<String, KafkaProducerConfig.KafkaData> data) {
        logger.debug("recv -> {}", data);
        logger.debug("recv -> {}", data.value());
    }
}
