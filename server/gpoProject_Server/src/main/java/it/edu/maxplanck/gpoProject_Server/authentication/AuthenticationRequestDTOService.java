package it.edu.maxplanck.gpoProject_Server.authentication;

import org.springframework.stereotype.Service;
import it.edu.maxplanck.gpoProject_Server.dto.request.*;
import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase.UserData;

@Service
public class AuthenticationRequestDTOService {

	public void authAccessDTO(RequestAccessDTO dto) throws IllegalArgumentException {
		
		if(
			dto == null 
			|| dto.username() == null || dto.username().length() > UserData.usernameLength 
			|| dto.password() == null || dto.password().length() > UserData.passwordLength
		) throw new IllegalArgumentException("Dati inseriti non validi");
	}

	public void authAccountDTO(RequestAccountDTO dto) throws IllegalArgumentException {
		
	}
	

	public void authCallDTO(RequestCallDTO dto) throws IllegalArgumentException {
		
	}

	public void authChannelDTO(RequestChannelDTO dto) throws IllegalArgumentException {
		
	}

	public void authChatDTO(RequestChatDTO dto) throws IllegalArgumentException {
		
	}

	public void authCommunityDTO(RequestCommunityDTO dto) throws IllegalArgumentException {
		
	}
	
	public void authFriendDTO(RequestFriendDTO body) throws IllegalArgumentException {
		
	}
	
	public void authMessageChatDTO(RequestMessageChatDTO dto) throws IllegalArgumentException {
		
	}

	public void authMessageCommunityDTO(RequestMessageCommunityDTO dto) throws IllegalArgumentException {
		
	}

	public void authProfileDTO(RequestProfileDTO dto) throws IllegalArgumentException {
		
	}

	public void authSectionDTO(RequestSectionDTO dto) throws IllegalArgumentException {
		
	}
}
