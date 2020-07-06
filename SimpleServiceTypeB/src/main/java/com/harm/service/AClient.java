package com.harm.service;

import com.harm.controller.HelloController;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@FeignClient(name = "SIMPLE-SERVICE-TYPE-A",
        //url="http://localhost:8070",
        fallbackFactory = CustomerAClientFallbackFactory.class)
public interface AClient {
    @GetMapping("/simple/msa/a/hello/{name}")
    public HelloController.HelloInfo hello(@PathVariable String name);
}

@Component
class CustomerAClientFallbackFactory implements FallbackFactory<AClient> {

    @Override
    public AClient create(Throwable cause) {
        return new AClient() {
            private Logger logger = LoggerFactory.getLogger(CustomerAClientFallbackFactory.class);
            @Override
            public HelloController.HelloInfo hello(String name) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                HelloController.HelloInfo helloInfo = new HelloController.HelloInfo.Builder()
                        .message("Hello, " + name + ". I'm B service. A service is currently down.")
                        .localAddrPort(request.getLocalAddr() + ":" + request.getLocalPort())
                        .remoteAddrPort(request.getRemoteAddr() + ":" + request.getRemotePort())
                        .build();
                return helloInfo;
            }
        };
    }
}