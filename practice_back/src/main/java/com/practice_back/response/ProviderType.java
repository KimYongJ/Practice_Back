package com.practice_back.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProviderType {
    GITHUB("github"),
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao");
    private final String value;
}
