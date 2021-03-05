package com.test.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
		return new ResponseEntity<>("pong", headers, HttpStatus.OK);
	}
	
	@GetMapping("/ping-secure")
	public ResponseEntity<String> pingSecure()  {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);
		return new ResponseEntity<>("pong secured", headers, HttpStatus.OK);
	}
}
