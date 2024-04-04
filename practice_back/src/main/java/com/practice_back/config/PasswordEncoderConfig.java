package com.practice_back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {
    /*
     * [ passwordEncoder 빈등록 설명 ]
     * - passwordEncoder() 함수를 빈으로 등록해놓고 사용한다.(가독성을 위해서..)
     * - 사용자 회원가입시 비밀번호를 그냥 저장하지 않고 해당 인코더를 사용해 암호화 한 후 저장한다.(DB에는 암호화된 데이터가 저장됨)
     * - 사용 예시 : passwordEncoder.encode(password) // 반환된 문자열을 그대로 DB에 저장
     * - BCryptPasswordEncoder : 복호화가 불가능한 인코딩 방법으로 BCrypt 해싱 알고리즘을 사용. 해시 속도를 조절할 수 있으며 브루트 포스 공격에 강하고 해시 결과가 각각 다른 솔트(salt)를 사용하기 때문에 동일한 비밀번호라도 해시 값이 다름
     * */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}