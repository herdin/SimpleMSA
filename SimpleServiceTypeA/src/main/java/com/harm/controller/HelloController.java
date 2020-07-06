package com.harm.controller;

import com.harm.config.KafkaProducerConfig;
import com.harm.service.BClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
public class HelloController {
    Logger logger = LoggerFactory.getLogger(HelloController.class);
    @Autowired
    BClient bClient;
    @Autowired
    KafkaTemplate<String, KafkaProducerConfig.KafkaData> kafkaDataKafkaTemplate;

    @GetMapping("/hello/{name}")
    public HelloInfo hello(@PathVariable String name) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        HelloInfo helloInfo = new HelloInfo.Builder()
                .message("Hello, " + name + ". I'm A service.")
                .localAddrPort(request.getLocalAddr() + ":" + request.getLocalPort())
                .remoteAddrPort(request.getRemoteAddr() + ":" + request.getRemotePort())
                .build();
        logger.debug("helloInfo  -> {}", helloInfo);
        return helloInfo;
    }

    @GetMapping("/remote/hello/{name}")
    public HelloInfo remoteHello(@PathVariable String name) {
        return bClient.hello(name);
    }

    public static class HelloInfo {
        String message;
        String localAddrPort;
        String remoteAddrPort;

        private HelloInfo(String message, String localAddrPort, String remoteAddrPort) {
            this.message = message;
            this.localAddrPort = localAddrPort;
            this.remoteAddrPort = remoteAddrPort;
        }

        public String getMessage() {
            return message;
        }

        public String getLocalAddrPort() {
            return localAddrPort;
        }

        public String getRemoteAddrPort() {
            return remoteAddrPort;
        }

        public static class Builder {
            String message;
            String localAddrPort;
            String remoteAddrPort;

            public Builder message(String message) {
                this.message = message;
                return this;
            }
            public Builder localAddrPort(String localAddrPort) {
                this.localAddrPort = localAddrPort;
                return this;
            }

            public Builder remoteAddrPort(String remoteAddrPort) {
                this.remoteAddrPort = remoteAddrPort;
                return this;
            }

            public HelloInfo build() {
                return new HelloInfo(message, localAddrPort, remoteAddrPort);
            }
        }
    }

    @PutMapping("/kafka/hello")
    public ResponseEntity<KafkaProducerConfig.KafkaData> putKafkaHello() {
        KafkaProducerConfig.KafkaData kafkaData = new KafkaProducerConfig.KafkaData();
        kafkaData.setMessage("hello, kafka, " + LocalDateTime.now());
        kafkaData.setNum((long) (Math.random()*100));
        kafkaDataKafkaTemplate.send(KafkaProducerConfig.KAFKA_TEST_TOPIC, kafkaData);
        return ResponseEntity.ok(kafkaData);
    }
}
