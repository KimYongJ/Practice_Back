package com.practice_back.response;

public enum StatusEnum {
	 	OK(200, "성공"),
		LOGIN_SUCCESS(200, "로그인 성공"),
		SIGNUP_SUCCESS(201, "회원가입 성공"),
	    BAD_REQUEST(400, "요청이 부적절 합니다."),
		UNAUTHORIZED(401, "권한이 없습니다."),
	    NOT_FOUND(404, "NOT_FOUND"),
	    INTERNAL_SERER_ERROR(500, "서버 문제로 응답할 수 없습니다.");
		
	    int statusCode;
	    String code;

	    StatusEnum(int statusCode, String code) {
	        this.statusCode = statusCode;
	        this.code = code;
	    }
}
