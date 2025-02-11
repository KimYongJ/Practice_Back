package com.practice_back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


// 배포환경에서 CORS 설정을 위한 클래스

/*
 * [ @Configuration 설명 ]
 * - 해당 어노테이션을 사용하면 클래스가 스프링의 설정 정보를 포함하고있음을 나타낸다.(스프링 컨테이너에 의해 빈 설정을 위한 것으로 간주됨)
 * - 해당 어노테이션을 쓴 클래스는 자동으로 빈으로 등록됨.(@Component 어노테이션 사용 불필요)
 * - 이 클래스 안에 정의된 함수 중 @Bean 어노테이션이 붙은 메소드들로 부터 반환되는 객체들은 스프링 빈으로 등록된다. ( 함수 반환 객체가 빈으로 등록됨에 유의 )
 * */
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

    /*
    * [ corsConfigurationSource ]
    * - 스프링 시큐리티는 기본적으로 아무것도 설정하지 않으면 모든 외부 요청에대해 cors block을한다. 그래서 cors에 대한 설정을 커스텀 해야한다,
    * */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 쿠키 통신시 필수
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}