package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.util.List;

public class ResponseCommunities extends Response {
	private final List<ResponseCommunity> communities;

	public ResponseCommunities(List<ResponseCommunity> communities) {
		super();
		this.communities = communities;
	}

	public List<ResponseCommunity> getCommunities() {
		return communities;
	}
}
