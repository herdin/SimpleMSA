package com.harm.service;

import com.harm.config.KafkaProducerConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingConsumerAwareMessageListener;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.listener.BatchMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaBatchListenerA implements BatchAcknowledgingMessageListener<String, KafkaProducerConfig.KafkaData> {
    Logger logger = LoggerFactory.getLogger(KafkaBatchListenerA.class);
    @KafkaListener(topics = {KafkaProducerConfig.KAFKA_TEST_TOPIC}, containerFactory = "kafkaDataKafkaListenerContainerFactory")
    @Override
    public void onMessage(List<ConsumerRecord<String, KafkaProducerConfig.KafkaData>> data, Acknowledgment acknowledgment) {
        logger.debug("recving..");
        for(ConsumerRecord<String, KafkaProducerConfig.KafkaData> item : data) {
            logger.debug("recv -> {}", item);
            logger.debug("recv -> {}", item.value());
        }
        acknowledgment.acknowledge();
    }
}
