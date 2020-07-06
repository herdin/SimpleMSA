package com.harm.controller;

import com.harm.service.AClient;
import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {
    Logger logger = LoggerFactory.getLogger(HelloController.class);
    @Autowired
    AClient aClient;

    @GetMapping("/hello/{name}")
    public HelloInfo hello(@PathVariable String name, HttpServletRequest request) {
        HelloInfo helloInfo = new HelloInfo.Builder()
                .message("Hello, " + name + ". I'm B service.")
                .localAddrPort(request.getLocalAddr() + ":" + request.getLocalPort())
                .remoteAddrPort(request.getRemoteAddr() + ":" + request.getRemotePort())
                .build();
        logger.debug("helloInfo  -> {}", helloInfo);
        return helloInfo;
    }

    @GetMapping("/remote/hello/{name}")
    public HelloInfo remoteHello(@PathVariable String name) {
        return aClient.hello(name);
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

        @Override
        public String toString() {
            return "HelloInfo{" +
                    "message='" + message + '\'' +
                    ", localAddrPort='" + localAddrPort + '\'' +
                    ", remoteAddrPort='" + remoteAddrPort + '\'' +
                    '}';
        }
    }
}
