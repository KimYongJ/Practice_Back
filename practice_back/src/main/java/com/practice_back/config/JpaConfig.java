package com.practice_back.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/*
 * [ @Configuration 설명 ]
 * - 해당 어노테이션을 사용하면 클래스가 스프링의 설정 정보를 포함하고있음을 나타낸다.(스프링 컨테이너에 의해 빈 설정을 위한 것으로 간주됨)
 * - 해당 어노테이션을 쓴 클래스는 자동으로 빈으로 등록됨.(@Component 어노테이션 사용 불필요)
 * - 이 클래스 안에 정의된 함수 중 @Bean 어노테이션이 붙은 메소드들로 부터 반환되는 객체들은 스프링 빈으로 등록된다. ( 함수 반환 객체가 빈으로 등록됨에 유의 )
 * */
@Configuration
/*
* [ EnableJpaAuditing 설명 ]
* 테이블의 공통 Auditing 기능 구현시 사용하며
* auditorAwareRef 속성을 통해 AuditorAware인터페이스의 구현체를 지정하며 이 때
* AuditorAware인터페이스의 구현체를 반환하는 빈의 이름을 문자열로 지정함
* */
@EnableJpaAuditing(auditorAwareRef = "auditorProviderMadeByKyj")
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorProviderMadeByKyj(){

        /*
        * 엔티티 생성자와 수정자 정보를 자동 관리를 위해서는 현재 사용자 정보를 알아야 하기 때문에
        * AuditorAware 인터페이스의 getCurrentAuditor함수를 재정의하여 현재 사용자 id를 가져오게 만듦
         * */
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor()
            {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated())
                {
                    return Optional.empty();
                }
                return Optional.of(authentication.getName()); // 현재 로그인한 사용자의 이름을 반환
            }
        };
    }
}
