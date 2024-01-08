package com.practice_back.response;
import lombok.Data;

@Data
public class Message {
	
	private com.practice_back.response.StatusEnum status;
    private String message;
    private Object data;

    public Message() {
        this.status = com.practice_back.response.StatusEnum.BAD_REQUEST;
        this.data = null;
        this.message = null;
    }
}