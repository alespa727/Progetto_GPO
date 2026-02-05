package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.util.List;

public class ResponseAccounts extends Response {
	private final List<ResponseAccount> users;

	public ResponseAccounts(List<ResponseAccount> users) {
		super();
		this.users = users;
	}

	public List<ResponseAccount> getUsers() {
		return users;
	}
}
