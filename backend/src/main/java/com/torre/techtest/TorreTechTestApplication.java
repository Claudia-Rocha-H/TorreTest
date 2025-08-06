package com.torre.techtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class TorreTechTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TorreTechTestApplication.class, args);
	}

	@RestController
	@CrossOrigin(origins = "*")
	public static class HealthController {
		
		@GetMapping("/api/health")
		public ResponseEntity<String> health() {
			return ResponseEntity.ok("TorreTechTest API is running");
		}
		
		@GetMapping("/")
		public ResponseEntity<String> root() {
			return ResponseEntity.ok("TorreTechTest Backend - API available at /api/*");
		}
	}
}
