package it.edu.maxplanck.gpoProject_Server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api")
public abstract class BasicApiRestController {

	@GetMapping("")
	public String HelloWorld() {
		return "Hello world form api!";
	}
}
