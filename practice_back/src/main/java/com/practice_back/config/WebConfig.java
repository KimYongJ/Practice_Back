package com.practice_back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// 배포환경에서 CORS 설정을 위한 클래스
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /*
    * addCorsMappings 함수에 정의된 CORS 정책은 모든 HTTP 요청 처리에 적용됨
    * 특히 각 컨트롤러나 컨트롤러 메소드에 개별적으로 적용되는 @CrossOrigin 어노테이션 보다 우선순위가 높음
    * */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOrigins("http://localhost:3000") // 클라이언트 애플리케이션(리액트)의 호스트를 지정
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메소드 지정
                .allowedHeaders("*") // 허용할 헤더 지정
                .allowCredentials(true); // 쿠키를 포함한 요청 허용
    }
}