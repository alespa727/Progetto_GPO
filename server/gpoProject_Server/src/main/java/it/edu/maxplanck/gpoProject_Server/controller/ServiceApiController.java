package it.edu.maxplanck.gpoProject_Server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/services")
public class ServiceApiController extends BasicApiRestController {

	
	/// User (users/)--------------------------------------------
	@PatchMapping("account")
	public ResponseEntity<Object> patchAccount() {
		return null;
	}
	
	@DeleteMapping("account")
	public ResponseEntity<Object> deleteAccount() {
		return null;
	}
	
	@GetMapping("profile")
	public ResponseEntity<Object> getProfilo() {
		return null;
	}
	
	@PatchMapping("profile")
	public ResponseEntity<Object> patchProfilo() {
		return null;
	}
	
	@GetMapping("friends")
	public ResponseEntity<Object> getFriends() {
		return null;
	}
	
	@PostMapping("chat")
	public ResponseEntity<Object> postChat() {
		return null;
	}
	
	@PostMapping("community")
	public ResponseEntity<Object> postCommunity() {
		return null;
	}
	

	@PostMapping("messageChat")
	public ResponseEntity<Object> postMessageChat() {
		return null;
	}
	
	@PostMapping("messageCommunity")
	public ResponseEntity<Object> postMessageCommunity() {
		return null;
	}
	
	@PostMapping("callChat")
	public ResponseEntity<Object> postCallChat() {
		return null;
	}
	
	@PostMapping("sectionCommunity")
	public ResponseEntity<Object> postSection() {
		return null;
	}
	
	@PostMapping("channelSection")
	public ResponseEntity<Object> postChannel() {
		return null;
	}
	
	@GetMapping("chat")
	public ResponseEntity<Object> getChat() {
		return null;
	}
	
	@GetMapping("community")
	public ResponseEntity<Object> getCommunity() {
		return null;
	}
	
	@DeleteMapping("chat")
	public ResponseEntity<Object> deleteChat() {
		return null;
	}
	
	@DeleteMapping("community")
	public ResponseEntity<Object> deleteCommunity() {
		return null;
	}
	
	// --------------------------------------------------
	
	@GetMapping("messageChat")
	public ResponseEntity<Object> getMessageChat() {
		return null;
	}
	
	@GetMapping("callChat")
	public ResponseEntity<Object> getDatiCall() {
		return null;
	}
	
	@GetMapping("usersCommunity")
	public ResponseEntity<Object> getUserCommunity() {
		return null;
	}
	
	@GetMapping("sectionCommunity")
	public ResponseEntity<Object> getSections() {
		return null;
	}
	
	@GetMapping("channelsSection")
	public ResponseEntity<Object> getChannels() {
		return null;
	}
	
	@GetMapping("messagesChannel")
	public ResponseEntity<Object> getMessagesChannel() {
		return null;
	}
}
