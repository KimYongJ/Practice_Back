package com.practice_back.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Getter , Setter, toString, equals, hashCode 자동생성
@AllArgsConstructor
@NoArgsConstructor
public class Message {
	
	private ErrorType status;
    private String message;
    private Object data;

}