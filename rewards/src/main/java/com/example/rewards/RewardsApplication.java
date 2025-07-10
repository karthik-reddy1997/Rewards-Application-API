package com.example.rewards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class that boots up the Rewards Program application.
 */
@SpringBootApplication
public class RewardsApplication {

	/**
	 * The entry point of the Rewards Program Spring Boot application.
	 *
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {
		SpringApplication.run(RewardsApplication.class, args);
	}
}
