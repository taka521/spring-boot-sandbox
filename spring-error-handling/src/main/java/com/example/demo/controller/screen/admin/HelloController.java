package com.example.demo.controller.screen.admin;

import com.example.demo.error.exception.ItemNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("adminHelloController")
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        throw new ItemNotFoundException("ItemNotFoundException");
    }

}
