package com.example.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
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

	@Autowired
	private DatabaseSettings databaseSettings;

	@Autowired
	private Environment environment;


	public void execute(){
		Console.startLog(ApplicationValues.class);
		Console.log("host", applicationValues.getHost());
		Console.log("sessionTimeout", applicationValues.getSessionTimeout());
		Console.log("requestTimeout", applicationValues.getRequestTimeout());
		Console.endLog();

		Console.startLog(DatabaseSettings.class);
		Console.log("host", databaseSettings.getHost());
		Console.log("username", databaseSettings.getUsername());
		Console.log("password", databaseSettings.getPassword());
		Console.endLog();

		Console.startLog(Environment.class);
		Console.log("message", environment.getProperty("app.environment.messaga"));
		Console.endLog();
	}

}
