package com.harm.controller;

import com.harm.service.BClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    BClient bClient;

    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return "Hello, " + name + ". I'm A service.";
    }

    @GetMapping("/remote/hello/{name}")
    public String remoteHello(@PathVariable String name) {
        return bClient.hello(name);
    }
}
