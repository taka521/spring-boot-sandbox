package com.example.demo.controller.api.external;

import com.example.demo.error.exception.ApplicationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("apiExternalHelloController")
public class HelloController {

    @RequestMapping("/api/hello")
    public String hello() {
        throw new ApplicationException("アプリケーションエラーをスロー");
    }

}
