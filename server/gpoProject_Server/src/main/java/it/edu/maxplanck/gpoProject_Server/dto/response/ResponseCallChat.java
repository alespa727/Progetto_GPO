package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.time.LocalDateTime;

public class ResponseCallChat extends Response {
	private final Integer id;
	private final LocalDateTime startTime;
	private final LocalDateTime endTime;

	public ResponseCallChat(Integer id, LocalDateTime startTime, LocalDateTime endTime) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Integer getId() {
		return id;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}
}
