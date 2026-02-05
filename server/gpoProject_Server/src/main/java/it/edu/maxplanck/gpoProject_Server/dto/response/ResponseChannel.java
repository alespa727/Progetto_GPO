package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.LocalDate;

public class ResponseChannel extends Response {
	private final Integer id;
	private final String name;
	private final String type;
	private final String description;
	private final LocalDate createdAt;

	public ResponseChannel(Integer id, String name, String type, String description, LocalDate createdAt) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.description = description;
		this.createdAt = createdAt;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}
}
