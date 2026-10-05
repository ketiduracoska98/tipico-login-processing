package org.example.loginprocessingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class LoginProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginProcessingServiceApplication.class, args);
	}

}
