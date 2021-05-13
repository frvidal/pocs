package com.test;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @author Fr&eacute;d&eacute;ric VIDAL Starting class for the test application
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Application {

	public static void main(String[] args) {
		LoggerFactory.getLogger(Application.class.getCanonicalName()).info("Starting Backend test");
		SpringApplication.run(Application.class, args);
	}
	
}
