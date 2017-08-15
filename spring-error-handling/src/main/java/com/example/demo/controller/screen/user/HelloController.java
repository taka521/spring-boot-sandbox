package com.example.demo.controller.screen.user;

import com.example.demo.error.exception.ApplicationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("userHelloController")
public class HelloController {

    @RequestMapping("/admin/hello")
    public String hello() {
        throw new ApplicationException("ApplicationException");
    }
}
