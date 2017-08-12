package com.example.domain_form.controller;

import com.example.domain_form.domain.ID;
import com.example.domain_form.domain.Name;
import com.example.domain_form.entity.User;
import com.example.domain_form.form.HelloForm;
import com.example.domain_form.service.UserFindService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HelloController {

    private final UserFindService userFindService;
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    public HelloController(final UserFindService userFindService) {
        this.userFindService = userFindService;
    }

    @ModelAttribute
    public HelloForm modelAttribute() {
        return new HelloForm();
    }

    @GetMapping("hello")
    String helloForm() {
        return "hello";
    }

    @PostMapping("hello")
    String findById(final HelloForm helloForm, final Model model) {
        logger.info("userId : " + helloForm.userId);
        logger.info("name : " + helloForm.name);

        List<User> users = userFindService.findAll().stream()
                                 .filter(u -> u.id.equals(helloForm.userId) || u.name.equals(helloForm.name))
                                 .collect(Collectors.toList());

        users.forEach(u -> logger.info(u.toString()));

        model.addAttribute("users", users);
        return "hello";
    }

}
