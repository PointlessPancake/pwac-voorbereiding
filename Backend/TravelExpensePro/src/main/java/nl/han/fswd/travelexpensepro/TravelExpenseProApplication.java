package nl.han.fswd.travelexpensepro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class TravelExpenseProApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelExpenseProApplication.class, args);
	}

}