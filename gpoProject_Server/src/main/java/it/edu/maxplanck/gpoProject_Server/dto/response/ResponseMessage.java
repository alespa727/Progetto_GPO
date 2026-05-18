package it.edu.maxplanck.gpoProject_Server.dto.response;

import java.util.HashMap;

public class ResponseMessage extends HashMap<String, Object> {
    public ResponseMessage(String message) {
        this.put("message", message);
    }
}
