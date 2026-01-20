package it.edu.maxplanck.gpoProject_Server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import it.edu.maxplanck.gpoProject_Server.cookies.CookieService;
import it.edu.maxplanck.gpoProject_Server.database.services.DatabaseService;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestAccountDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestCallDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestChannelDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestMessageChatDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestMessageCommunityDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestProfileDTO;
import it.edu.maxplanck.gpoProject_Server.dto.request.RequestSectionDTO;
import it.edu.maxplanck.gpoProject_Server.token.TokenService;
import it.edu.maxplanck.gpoProject_Server.util.UtilServer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/services")
public class ServiceApiController extends BasicApiRestController {

	/**
	 * @param tokenService
	 * @param cookieService
	 * @param databaseService
	 * @param accessTokenSubject
	 * @param accessCookieName
	 * @param refreshTokenSubject
	 * @param refreshCookieName
	 */
	public ServiceApiController(
			TokenService tokenService, 
			CookieService cookieService, 
			DatabaseService databaseService,
			@Value(UtilServer.accessTokenSubjectPath) String accessTokenSubject,
			@Value(UtilServer.accessCookiePath) String accessCookieName,
			@Value(UtilServer.refreshTokenSubjectPath) String refreshTokenSubject,
			@Value(UtilServer.refreshCookiePath) String refreshCookieName
		) {
		super(tokenService, cookieService, databaseService, accessTokenSubject, accessCookieName, refreshTokenSubject, refreshCookieName);
	}

	/**
	 * Autenticazione cookies
	 * @param request
	 * @param response
	 * @throws IllegalStateException
	 */
	protected void authenticate(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException {

		// Check cookies
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0) throw new IllegalStateException("Nessun cookie presente");

		// Check cookies
		Cookie accessCookie = this.cookieService.getCookie(cookies, this.accessCookieName);
		Cookie refreshCookie = this.cookieService.getCookie(cookies, this.refreshCookieName);
		if (accessCookie == null && refreshCookie == null) throw new IllegalStateException("Access e Refresh cookie mancanti");

		// Check access cookie
		if (accessCookie != null) {
			String accessToken = accessCookie.getValue();
			
			// Check token
			if (this.tokenService.isTokenAccessValid(accessToken)) return;
		}

		// Check refresh cookie
		if (refreshCookie != null) {
			String refreshToken = refreshCookie.getValue();
			
			// Controllo token
			if (this.tokenService.isTokenRefreshValid(refreshToken)) {

				Claims claims = this.tokenService.getClaimsRefresh(refreshToken);
				
				// Creazione nuovo cookie e nuovo token
				String newAccessToken = this.tokenService.getTokenAccess(claims.getSubject(), claims);
				Cookie newAccessCookie = this.cookieService.generateCookie(this.accessCookieName, newAccessToken, true, false, "/api/", UtilServer.timeExpirationDateAccessCookie);

				response.addCookie(newAccessCookie);
				return;
			}
		}

		throw new IllegalStateException("Cookie non validi, login necessario");
	}

	@GetMapping("logout")
	public ResponseEntity<?> logoutAccount(HttpServletRequest request, HttpServletResponse response) {

		return null;
	}
	
	// -----------------------------------------------------------------------

	@PatchMapping("account")
	public ResponseEntity<?> patchAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestAccountDTO body) {
		
		return null;
	}

	@DeleteMapping("account")
	public ResponseEntity<?> deleteAccount(HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@GetMapping("profile")
	public ResponseEntity<?> getProfilo(HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@PatchMapping("profile")
	public ResponseEntity<?> patchProfilo(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestProfileDTO body) {
		
		return null;
	}

	@GetMapping("friends")
	public ResponseEntity<?> getFriends(HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@PostMapping("chat")
	public ResponseEntity<?> postChat(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestChatDTO body) {
		
		return null;
	}

	@PostMapping("community")
	public ResponseEntity<?> postCommunity(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestCommunityDTO body) {
		
		return null;
	}

	@PostMapping("messageChat")
	public ResponseEntity<?> postMessageChat(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestMessageChatDTO body) {
		
		return null;
	}

	@PostMapping("messageCommunity")
	public ResponseEntity<?> postMessageCommunity(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestMessageCommunityDTO body) {
		
		return null;
	}

	@PostMapping("callChat")
	public ResponseEntity<Object> postCallChat(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestCallDTO body) {
		
		return null;
	}

	@PostMapping("sectionCommunity")
	public ResponseEntity<?> postSection(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestSectionDTO body) {
		
		return null;
	}

	@PostMapping("channelSection")
	public ResponseEntity<?> postChannel(HttpServletRequest request, HttpServletResponse response, @RequestBody RequestChannelDTO body) {
		
		return null;
	}

	@GetMapping("chat")
	public ResponseEntity<?> getChat(HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@GetMapping("community")
	public ResponseEntity<?> getCommunity(HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@DeleteMapping("chat")
	public ResponseEntity<?> deleteChat(HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@DeleteMapping("community")
	public ResponseEntity<?> deleteCommunity(HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	// -----------------------------------------------------------------------
	
	@GetMapping("messageChat")
	public ResponseEntity<?> getMessageChat(HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@GetMapping("callChat")
	public ResponseEntity<?> getDatiCall(HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@GetMapping("usersCommunity")
	public ResponseEntity<?> getUserCommunity(HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@GetMapping("sectionCommunity")
	public ResponseEntity<?> getSections(HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@GetMapping("channelsSection")
	public ResponseEntity<?> getChannels(HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	@GetMapping("messagesChannel")
	public ResponseEntity<?> getMessagesChannel(HttpServletRequest request, HttpServletResponse response) {

		return null;
	}
}
