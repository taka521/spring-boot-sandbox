package com.example.demo.controller.api.internal;

import com.example.demo.error.exception.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("apiInternalHelloController")
public class HelloController {

    @RequestMapping("/api/internal/hello")
    public String hello() {
        throw new AuthenticationException("AuthenticationExceptionです");
    }
}
