package com.example.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class ExternalSettingValuesApplication implements CommandLineRunner {

	@Autowired
	private SimpleComponent component;

	public static void main(String... args) {
		SpringApplication.run(ExternalSettingValuesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		this.component.execute();
	}

}

@Component
class SimpleComponent {

	@Autowired
	private ApplicationValues applicationValues;

	public void execute(){
		Console.log("host", applicationValues.getHost());
		Console.log("sessionTimeout", applicationValues.getSessionTimeout());
		Console.log("requestTimeout", applicationValues.getRequestTimeout());
	}

}
