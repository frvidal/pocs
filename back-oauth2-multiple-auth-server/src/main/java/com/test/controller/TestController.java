package com.test.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
/**
 * @author Controller for Test purpose
 */

public class TestController {

	@GetMapping("/ping")
	public ResponseEntity<String> ping()  {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);
		return new ResponseEntity<>("pong @ " + ts(), headers, HttpStatus.OK);
	}
	
	@GetMapping("/ping-secure")
	public ResponseEntity<String> pingSecure()  {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);
		return new ResponseEntity<>("pong secured @ " + ts(), headers, HttpStatus.OK);
	}

	private String ts() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		return dtf.format(now);  		
	}
}
