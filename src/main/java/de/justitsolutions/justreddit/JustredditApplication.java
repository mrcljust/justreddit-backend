package de.justitsolutions.justreddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JustredditApplication {

	public static void main(String[] args) {
		SpringApplication.run(JustredditApplication.class, args);
	}

}
