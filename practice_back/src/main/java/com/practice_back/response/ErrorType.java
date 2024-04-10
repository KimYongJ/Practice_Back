package com.practice_back.response;


import lombok.Getter;

@Getter
public enum ErrorType {
	 	OK(200, 						"성공"),
		VALID_TOKEN(200, 				"유효한 토큰입니다."),
		LOGOUT_SUCCESS(200, 			"로그아웃 성공"),
		LOGIN_SUCCESS(200, 			"로그인 성공"),
		ACCOUNT_DELETION_SUCCESS(200, "회원 탈퇴 성공"),
		AUTHENTICATION_SUCCESS(200, 	"인증 수신 완료"),
		SIGNUP_SUCCESS(201, 			"회원가입 성공"),
		MOVED_PERMANENTLY(301,		"페이지 이동"),
	    BAD_REQUEST(400, 				"요청이 부적절 합니다."),
		INVALID_PASSWORD(400, 		"유효하지 않은 비밀번호입니다."),
		PASSWORD_NO_CHANGE(400, 		"기존 비밀번호와 같습니다. 다른 비밀번호를 사용해주세요."),
		ADDRESS_LIMIT_EXCEEDED(400,   "주소 목록이 최대 한도에 도달했습니다. 더 이상 추가할 수 없습니다."),
		LOGIN_FAILED(401, 			"로그인 실패 다시 확인해주세요!"),
		MISSING_TOKEN(401,  			"로그인 후 이용 가능합니다."),
		INVALID_LOGIN_CREDENTIALS(401,"비밀번호가 잘못되었습니다."),
		INVALID_TOKEN(401,  			"유효하지 않은 토큰입니다."),
		EXPIRED_TOKEN(401,  			"로그인 시간이 만료되었습니다."),
		UNAUTHORIZED(401, 			"권한이 없습니다."),
	    NOT_FOUND(404, 				"NOT_FOUND"),
		EMAIL_NOT_FOUND(404, 			"해당하는 이메일이 없습니다."),
		DUPLICATE_EMAIL(409, 			"이미 가입된 이메일입니다."),
		DUPLICATE_ITEM(409, 			"이미 카트에 추가된 상품 입니다."),
	    INTERNAL_SERER_ERROR(500, 	"서버 문제로 응답할 수 없습니다.");
		
	    int statusCode;
	    String errStr;

	    ErrorType(int statusCode, String code) {
	        this.statusCode = statusCode;
	        this.errStr = code;
	    }
}
