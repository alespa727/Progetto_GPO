package it.edu.maxplanck.gpoProject_Server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class StarterApiController extends BasicApiRestController {
	
	@PostMapping("registration")
	public String registration() {
		return "Hello from registration!";
	}
	
	@GetMapping("login")
	public String login() {
		return "Hello from login!";
	}
}
