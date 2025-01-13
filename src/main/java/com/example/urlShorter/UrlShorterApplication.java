package com.example.urlShorter;

import com.example.urlShorter.urls.ConsoleInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UrlShorterApplication implements CommandLineRunner {

	private final ConsoleInterface consoleInterface;

	@Autowired
	public UrlShorterApplication(ConsoleInterface consoleInterface) {
		this.consoleInterface = consoleInterface;
	}

	public static void main(String[] args) {
		SpringApplication.run(UrlShorterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		consoleInterface.start();
	}
}
