package it.edu.maxplanck.gpoProject_Server.dto.request;

import java.util.List;

public record RequestFriendsDTO(
	List<RequestFriendDTO> users
) {}
