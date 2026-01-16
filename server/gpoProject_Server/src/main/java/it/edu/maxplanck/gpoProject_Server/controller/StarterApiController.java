package it.edu.maxplanck.gpoProject_Server.controller;

import it.edu.maxplanck.gpoProject_Server.token.TokenService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class StarterApiController extends BasicApiRestController {

    private final TokenService tokenService;

    StarterApiController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

	@GetMapping("login")
	public String login() {
		return "Hello from login!";
	}
}
