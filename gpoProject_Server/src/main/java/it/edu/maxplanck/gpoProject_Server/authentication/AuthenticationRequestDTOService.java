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

	public void authAccessDTO(RequestAccessDTO dto) throws AuthentificationException {
		if(
			dto == null 
			|| dto.username() == null || dto.username().isBlank() || dto.username().length() > UserData.usernameLength 
			|| dto.password() == null || dto.password().isBlank() || dto.password().length() > UserData.passwordLength
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}

    public void deleteDTO(DeleteAccountDTO dto) throws AuthentificationException {
        if(
                dto == null || dto.password() == null || dto.password().isBlank() || dto.password().length() > UserData.passwordLength
        ) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
    }

	public void authAccountDTO(RequestCredentials dto) throws AuthentificationException {
		if(
			dto == null 
			|| dto.username() == null || dto.username().isBlank() || dto.username().length() > UserData.usernameLength 
			|| dto.password() == null || dto.password().isBlank() || dto.password().length() > UserData.passwordLength
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}

	public void authChannelDTO(RequestChannelDTO dto) throws AuthentificationException {
		if(
			dto == null
			|| dto.name() == null || dto.name().isBlank() || dto.name().length() > ChannelData.nameLenght
			|| dto.type() == null || dto.type().isBlank() || (!dto.type().equals(ChannelData.TypeType.TYPE_TESTO.getType()) && !dto.type().equals(ChannelData.TypeType.TYPE_VOCALE.getType()))
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}

	public void authChatDTO(RequestChatDTO dto) throws AuthentificationException {
		if(
			dto == null
			|| dto.friend() == null
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
		this.authFriendDTO(dto.friend());
	}

	public void authCommunityDTO(RequestCommunityDTO dto) throws AuthentificationException {
		if(
			dto == null
			|| dto.name() == null || dto.name().isBlank() || dto.name().length() > CommunityData.nameLenght
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}
	
	public void authFriendDTO(RequestFriendDTO dto) throws AuthentificationException {
		if(
			dto == null
			|| dto.username() == null || dto.username().isBlank() || dto.username().length() > UserData.usernameLength
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}
	
	public void authMessageChatDTO(RequestMessageChatDTO dto) throws AuthentificationException {
		if(
			dto == null
			|| dto.message() == null || dto.message().isBlank() || dto.message().length() > MessageChatData.messageLength
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}

	public void authMessageCommunityDTO(RequestMessageCommunityDTO dto) throws AuthentificationException {
		if(
			dto == null
			|| dto.message() == null || dto.message().isBlank() || dto.message().length() > MessageCommunityData.messageLength
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}

	public void authProfileDTO(RequestProfileDTO dto) throws AuthentificationException {
		if(
			dto == null
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}

	public void authSectionDTO(RequestSectionDTO dto) throws AuthentificationException {
		if(
			dto == null
			|| dto.name() == null || dto.name().isBlank() || dto.name().length() > SectionData.nameLenght
		) throw new AuthentificationException(AuthentificationExceptions.AUTH_INSERTED_DTO_DATA_IS_NOT_VALID);
	}
}
