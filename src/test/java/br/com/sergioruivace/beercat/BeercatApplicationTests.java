package br.com.sergioruivace.beercat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class BeercatApplicationTests {

	@Test
	void contextLoads() {
	}

}
