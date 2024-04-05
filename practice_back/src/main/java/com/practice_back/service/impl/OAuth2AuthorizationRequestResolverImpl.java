package com.practice_back.service.impl;

import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class OAuth2AuthorizationRequestResolverImpl implements OAuth2AuthorizationRequestResolver{
    private final OAuth2AuthorizationRequestResolver defaultResolver;

    public OAuth2AuthorizationRequestResolverImpl(OAuth2AuthorizationRequestResolver defaultResolver) {
        this.defaultResolver = defaultResolver;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        return customizeAuthorizationRequest(defaultResolver.resolve(request));
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        return customizeAuthorizationRequest(defaultResolver.resolve(request, clientRegistrationId));
    }

    private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest request) {
        if (request == null) {
            return null;
        }

        Map<String, Object> additionalParameters = new HashMap<>(request.getAdditionalParameters());
        additionalParameters.put("prompt", "select_account");

        return OAuth2AuthorizationRequest.from(request)
                .additionalParameters(additionalParameters)
                .build();
    }
}


/**
 * [ OAuth2AuthorizationRequestResolverImpl  클래스 ]
 * - 스프링 시큐리티 OAuth2 인증 요청을 커스터마이징하기 위한 클래스입니다.
 *
 * [ OAuth2AuthorizationRequestResolver ]
 * - 기본 인증 요청 리졸버
 *
 * [ resolve 함수 오버라이드 이유 ]
 * - 인증 요청을 커스터마이징하기 위해 customizeAuthorizationRequest 함수를 만들고,
 * - 원래의 인증 요청에 추가 파라미터를 넣어 새로운 요청을 만들어 반환했습니다.
 * - 이렇게 함으로 써 Google 로그인 시에 계정 선택을 강제하는 'prompt' 파라미터를 추가하였습니다.
 * */