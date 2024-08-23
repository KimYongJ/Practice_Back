package com.practice_back.factory;

import com.practice_back.entity.Oauth2.*;
import com.practice_back.response.ProviderType;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * 로그인 루트에 따른 UserInfo 생성 클래스,
 * 팩토리 메소드 패턴으로 하위 타입으로 변환( ex) Oauth2UserInfo -> NaverUserInfo )
 * */
@Component
public class FactoryUserInfo {
    /**
     * 하위 타입 생성 메소드와 열거타입 매핑
     * */
    private final static Map<ProviderType, Function<OAuth2User, Oauth2UserInfo>> factoryUserInfoMap;
    static{
        factoryUserInfoMap = new EnumMap<>(ProviderType.class);
        factoryUserInfoMap.put(ProviderType.GOOGLE, GoogleUserInfo::new);
        factoryUserInfoMap.put(ProviderType.NAVER,  NaverUserInfo::new);
        factoryUserInfoMap.put(ProviderType.GITHUB, GithubUserInfo::new);
        factoryUserInfoMap.put(ProviderType.KAKAO,  KakaoUserInfo::new);
    }

    /**
     * 키 : String, value : ProviderType
     * */
    private static final Map<String, ProviderType> providerEnum;
    static{
        providerEnum = Stream.of(ProviderType.values())
                        .collect(Collectors.toMap(ProviderType::getValue, providerType -> providerType));
    }
    /**
     * String 키를 기준으로 열거타입으로 변환
     */
    private Optional<ProviderType> geteProviderType(String provider){
        return Optional.ofNullable(providerEnum.get(provider));
    }

    /**
     * 팩토리 함수
     * */
    public Oauth2UserInfo makeOauth2Userinfo(String provider, OAuth2User oAuth2User) {
        Optional<ProviderType> providerType = geteProviderType(provider);
        return factoryUserInfoMap
                .get(providerType.orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 로그인 API 제공자입니다.")))
                .apply(oAuth2User);
    }

}
