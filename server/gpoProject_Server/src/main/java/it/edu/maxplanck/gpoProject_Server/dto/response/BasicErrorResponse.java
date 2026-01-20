package it.edu.maxplanck.gpoProject_Server.dto.response;

public class BasicErrorResponse extends BasicResponse {

	private String message = "Error";
	private int errorCode = -1;

	public BasicErrorResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BasicErrorResponse(String message, int errorCode) {
		super();
		this.message = message;
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
