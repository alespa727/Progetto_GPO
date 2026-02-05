package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.LocalDate;

public class ResponseCommunityData extends Response {
	private final String name;
	private final String inviteCode;
	private final boolean isInviteCodeValid;
	private final String description;
	private final LocalDate createdAt;

	public ResponseCommunityData(String name, String inviteCode, boolean isInviteCodeValid, String description,
			LocalDate createdAt) {
		super();
		this.name = name;
		this.inviteCode = inviteCode;
		this.isInviteCodeValid = isInviteCodeValid;
		this.description = description;
		this.createdAt = createdAt;
	}

	public String getName() {
		return name;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public boolean isInviteCodeValid() {
		return isInviteCodeValid;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}
}
