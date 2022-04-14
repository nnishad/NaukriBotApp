package com.nikhilnishad.naukriapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nikhilnishad.naukriapp.model.UserCred;
import com.nikhilnishad.naukriapp.service.SeleniumService;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	@Autowired
	private SeleniumService service;
	
	@PostMapping("/launch")
	public ResponseEntity<String> startBot(@RequestBody UserCred userCred) {
		String result=service.startBotForUser(userCred);
		if(result!=null) return ResponseEntity.ok(result);
		return new ResponseEntity<String>("Unable to Update", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/stop")
	public String stopBot() {
		System.exit(0);
		return "Stop Instruction Executed";
	}
	
	@GetMapping
	public String home() {
		return "server is up";
	}

}
