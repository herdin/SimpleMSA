package com.harm.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.BatchErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

//https://justin-g.tistory.com/44
@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    @Value("${kafka.url}")
    private String bootstrapAddress;
    final boolean isBatchMode = true;

    /* The KafkaMessageListenerContainer receives all message from all topics or partitions on a single thread. */
    public ConsumerFactory<String, KafkaProducerConfig.KafkaData> kafkaDataResultConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group-" + KafkaProducerConfig.KAFKA_TEST_TOPIC);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        if(isBatchMode) {
            props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10");
        }
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(KafkaProducerConfig.KafkaData.class, false));
    }

    /* The ConcurrentMessageListenerContainer delegates to one or more KafkaMessageListenerContainer instances to provide multi-threaded consumption.
    default bean name is expected to kafkaListenerContainerFactory
    * */
    @Bean("kafkaDataKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, KafkaProducerConfig.KafkaData> kafkaDataKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaProducerConfig.KafkaData> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaDataResultConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        if(isBatchMode) {
            factory.setBatchListener(true);
        } else {
            factory.setErrorHandler(new SeekToCurrentErrorHandler());
        }

        return factory;
    }
}
