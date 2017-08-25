package com.example.validation_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@SpringBootApplication
public class ValidationDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValidationDemoApplication.class, args);
	}

	@ModelAttribute
	public DemoForm setForm(){
		return new DemoForm();
	}

	@GetMapping("/")
	public String hello(){
		return "index";
	}

	@PostMapping("/")
	public String validate(@Validated final DemoForm form, final BindingResult result, final Model model){
		if(result.hasErrors()){
			model.addAttribute("errors", result.getAllErrors());
		}
		return "index";
	}

}
