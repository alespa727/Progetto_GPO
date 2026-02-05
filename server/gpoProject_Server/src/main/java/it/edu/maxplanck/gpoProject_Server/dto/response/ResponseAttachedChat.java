package it.edu.maxplanck.gpoProject_Server.dto.response;

public class ResponseAttachedChat extends Response {
	private final Integer id;
	private final String path;
	private final String filename;
	private final String extension;

	public ResponseAttachedChat(Integer id, String path, String filename, String extension) {
		super();
		this.id = id;
		this.path = path;
		this.filename = filename;
		this.extension = extension;
	}

	public Integer getId() {
		return id;
	}

	public String getPath() {
		return path;
	}

	public String getFilename() {
		return filename;
	}

	public String getExtension() {
		return extension;
	}
}
