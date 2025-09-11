package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@Import(TestcontainersConfiguration.class)
@DirtiesContext
@SpringBootTest
@ActiveProfiles("test")
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
