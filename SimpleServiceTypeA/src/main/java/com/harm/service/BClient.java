package com.harm.service;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SIMPLE-SERVICE-TYPE-B",
        //url="http://localhost:8070",
        fallbackFactory = CustomerBClientFallbackFactory.class)
public interface BClient {
    @GetMapping("/simple/msa/b/hello/{name}")
    public String hello(@PathVariable String name);
}

@Component
class CustomerBClientFallbackFactory implements FallbackFactory<BClient> {

    @Override
    public BClient create(Throwable cause) {
        return new BClient() {
            private Logger logger = LoggerFactory.getLogger(CustomerBClientFallbackFactory.class);
            @Override
            public String hello(String name) {
                return "Hello, " + name + ". I'm A service. B service is currently down.";
            }
        };
    }
}