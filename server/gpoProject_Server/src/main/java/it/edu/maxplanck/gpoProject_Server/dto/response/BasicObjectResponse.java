package it.edu.maxplanck.gpoProject_Server.dto.response;

public class BasicObjectResponse<T> extends BasicResponse {

	private T response = null;

	public BasicObjectResponse() {
	}

	public BasicObjectResponse(T response) {
		this.setResponse(response);
	}

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}
}
