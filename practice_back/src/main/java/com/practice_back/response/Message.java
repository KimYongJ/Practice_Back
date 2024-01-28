package com.practice_back.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Getter , Setter, toString, equals, hashCode 자동생성
@AllArgsConstructor // 모든 파라미터를 받는 생성자 자동 생성
@NoArgsConstructor
public class Message {
	
	private ErrorType status;
    private String message;
    private Object data;

}