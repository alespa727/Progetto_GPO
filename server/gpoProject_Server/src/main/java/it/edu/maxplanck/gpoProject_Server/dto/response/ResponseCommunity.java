package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.LocalDate;
import java.util.List;

public class ResponseCommunity extends Response {
	private final String name;
	private final String inviteCode;
	private final boolean isInviteCodeValid;
	private final String description;
	private final LocalDate createdAt;
	private final List<ResponseSection> sections;

	public ResponseCommunity(String name, String inviteCode, boolean isInviteCodeValid, String description, LocalDate createdAt, List<ResponseSection> sections) {
		super();
		this.name = name;
		this.inviteCode = inviteCode;
		this.isInviteCodeValid = isInviteCodeValid;
		this.description = description;
		this.createdAt = createdAt;
		this.sections = sections;
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

	public List<ResponseSection> getSections() {
		return sections;
	}
}
