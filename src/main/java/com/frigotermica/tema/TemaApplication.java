package com.frigotermica.tema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class TemaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemaApplication.class, args);

	}

}
