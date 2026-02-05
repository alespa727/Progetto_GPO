package it.edu.maxplanck.gpoProject_Server.authentication;

import org.springframework.stereotype.Service;
import it.edu.maxplanck.gpoProject_Server.dto.request.*;
import it.edu.maxplanck.gpoProject_Server.exceptions.AuthentificationException;
import it.edu.maxplanck.gpoProject_Server.exceptions.AuthentificationExceptions;
import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase.ChannelData;
import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase.CommunityData;
import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase.MessageChatData;
import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase.MessageCommunityData;
import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase.SectionData;
import it.edu.maxplanck.gpoProject_Server.util.UtilDatabase.UserData;

/**
 * Classe che serve per autentificare i vari dati interni ai DTO
 */
@Service
public final class AuthenticationRequestDTOService {

	public void authAccessDTO(RequestAccess dto) throws AuthentificationException {
		if(
			dto == null 
			|| dto.getUsername() == null || dto.getUsername().isBlank() || dto.getUsername().length() > UserData.usernameLength 
			|| dto.getPassword() == null || dto.getPassword().isBlank() || dto.getPassword().length() > UserData.passwordLength
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}

	public void authAccountDTO(RequestAccount dto) throws AuthentificationException {
		if(
			dto == null 
			|| dto.getUsername() == null || dto.getUsername().isBlank() || dto.getUsername().length() > UserData.usernameLength 
			|| dto.getPassword() == null || dto.getPassword().isBlank() || dto.getPassword().length() > UserData.passwordLength
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}

	public void authChannelDTO(RequestChannel dto) throws AuthentificationException {
		if(
			dto == null
			|| dto.getName() == null || dto.getName().isBlank() || dto.getName().length() > ChannelData.nameLenght
			|| dto.getType() == null || dto.getType().isBlank() || (!dto.getType().equals(ChannelData.TypeType.TYPE_TESTO.getType()) && !dto.getType().equals(ChannelData.TypeType.TYPE_VOCALE.getType()))
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}

	public void authChatDTO(RequestChat dto) throws AuthentificationException {
		if(
			dto == null
			|| dto.getFriend() == null
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
		this.authFriendDTO(dto.getFriend());
	}

	public void authCommunityDTO(RequestCommunity dto) throws AuthentificationException {
		if(
			dto == null
			|| dto.getName() == null || dto.getName().isBlank() || dto.getName().length() > CommunityData.nameLenght
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}
	
	public void authFriendDTO(RequestFriend dto) throws AuthentificationException {
		if(
			dto == null
			|| dto.getUsername() == null || dto.getUsername().isBlank() || dto.getUsername().length() > UserData.usernameLength
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}
	
	public void authMessageChatDTO(RequestMessageChat dto) throws AuthentificationException {
		if(
			dto == null
			|| dto.getMessage() == null || dto.getMessage().isBlank() || dto.getMessage().length() > MessageChatData.messageLength
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}

	public void authMessageCommunityDTO(RequestMessageCommunity dto) throws AuthentificationException {
		if(
			dto == null
			|| dto.getMessage() == null || dto.getMessage().isBlank() || dto.getMessage().length() > MessageCommunityData.messageLength
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}

	public void authProfileDTO(RequestProfile dto) throws AuthentificationException {
		if(
			dto == null
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}

	public void authSectionDTO(RequestSection dto) throws AuthentificationException {
		if(
			dto == null
			|| dto.getName() == null || dto.getName().isBlank() || dto.getName().length() > SectionData.nameLenght
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}
}
