package com.harm.service;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SIMPLE-SERVICE-TYPE-A",
        //url="http://localhost:8070",
        fallbackFactory = CustomerAClientFallbackFactory.class)
public interface AClient {
    @GetMapping("/simple/msa/a/hello/{name}")
    public String hello(@PathVariable String name);
}

@Component
class CustomerAClientFallbackFactory implements FallbackFactory<AClient> {

    @Override
    public AClient create(Throwable cause) {
        return new AClient() {
            private Logger logger = LoggerFactory.getLogger(CustomerAClientFallbackFactory.class);
            @Override
            public String hello(String name) {
                return "Hello, " + name + ". I'm A service. B service is currently down.";
            }
        };
    }
}