package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@DirtiesContext
@ActiveProfiles("test")
public class TestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.from(DemoApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
