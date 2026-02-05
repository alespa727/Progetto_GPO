package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.util.List;

public class ResponseSection extends Response {
	private final Integer id;
	private final String name;
	private final List<ResponseChannel> channels;

	public ResponseSection(Integer id, String name, List<ResponseChannel> channels) {
		super();
		this.id = id;
		this.name = name;
		this.channels = channels;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<ResponseChannel> getChannels() {
		return channels;
	}
}
