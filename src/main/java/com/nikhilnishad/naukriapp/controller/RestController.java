package com.nikhilnishad.naukriapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nikhilnishad.naukriapp.model.UserCred;
import com.nikhilnishad.naukriapp.service.SeleniumService;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	@Autowired
	private SeleniumService service;
	
	@PostMapping("/launch")
	public String startBot(@RequestBody UserCred userCred) {
		service.startBotForUser(userCred);
		return "Profile Updated";
	}

}
