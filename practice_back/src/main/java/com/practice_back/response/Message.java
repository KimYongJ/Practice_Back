package com.practice_back.response;
import lombok.Data;

@Data // Getter , Setter, toString, equals, hashCode 자동생성
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