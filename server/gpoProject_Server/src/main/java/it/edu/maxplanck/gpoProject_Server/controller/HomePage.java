package it.edu.maxplanck.gpoProject_Server.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("home")
public class HomePage {

	@GetMapping
	public String test() {
		return "Hello world from home!";
	}
	
	@GetMapping("all")
	public String testStar() {
		return "Hello star!";
	}
	
	static class Test {
		public int a;
		public int b;
		public String c;
	}
	
	@GetMapping("json")
	public Object testJson() {
		Test object = new Test();
		object.a = 10;
		object.b = 20;
		object.c = (10 + 20) + "";
		
		return object;
	}
	
	@GetMapping("response_entity")
	public ResponseEntity<Object> testResponseEntity(){
		return ResponseEntity.ok("Hello word from reponse entity!");
	}
	
	@GetMapping("json_response_entity")
	public ResponseEntity<String> testResponseEntityGson() {
	    
		/*
	    Gson gson = new GsonBuilder()
	                    .serializeNulls() 
	                    .setPrettyPrinting()
	                    .create();
	    */
	    
	    List<String> list = new ArrayList<>();
	    list.add(null);
	    list.add("hello from list");
	    list.add("hello world from json!");
	    
	    // String jsonOutput = gson.toJson(list);
	    String jsonOutput = list.toString();
	    
	    return ResponseEntity.ok().body(jsonOutput);
	    // return ResponseEntity.ok().body(list);
	}
}
