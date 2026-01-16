package it.edu.maxplanck.gpoProject_Server.controller;

import org.springframework.web.bind.annotation.GetMapping;

public abstract class BasicApiRestController {

	@GetMapping("")
	public String HelloWorld() {
		return "Hello world form api!";
	}
}
