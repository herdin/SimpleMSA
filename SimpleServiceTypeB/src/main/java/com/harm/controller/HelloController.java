package com.harm.controller;

import com.harm.service.AClient;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    AClient aClient;

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return "Hello, " + name + ". I'm B service.";
    }

    @GetMapping("/remote/hello/{name}")
    public String remoteHello(@PathVariable String name) {
        return aClient.hello(name);
    }
}
