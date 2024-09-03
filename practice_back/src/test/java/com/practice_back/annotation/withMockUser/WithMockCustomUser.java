package com.practice_back.annotation.withMockUser;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String username() default  "member1";
    String password() default "12345678";
    String[] roles() default {"USER"}; // 기본값으로 USER 권한을 설정
}