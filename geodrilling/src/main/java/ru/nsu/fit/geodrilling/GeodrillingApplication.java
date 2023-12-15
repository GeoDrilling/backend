package ru.nsu.fit.geodrilling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.nsu.fit.geodrilling.services.file.LasFileService;

@SpringBootApplication
public class GeodrillingApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeodrillingApplication.class, args);
	}


}
