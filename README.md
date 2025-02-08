# 차량 용품 판매

## 개발환경


- IntelliJ
- Postman
- GitHub
- Mysql Workbench
- Visual Studio Code

## 사용 기술



### 백엔드

**주요 프레임워크 / 라이브러리**

- Java 11 openjdk
- SpringBoot 2.7.7
- SpringBoot Security
- Spring Data JPA

**Build tool**

- Gradle

**DataBase**

- MySql

**Infra**

- AWS EC2
- AWS S3
- AWS RDS

### 프론트엔드

- React
- MUI
- Redux

### 기타 주요 라이브러리

- Lombok

## 핵심 키워드



- 스프링 부트, 스프링 시큐리티를 사용하여 웹 애플리케이션 생애 주기 기획부터 배포 유지 보수까지 전과정 개발
- JPA, Hibernate를 사용한 도메인 설계
- MVC 프레임워크 기반 백엔드 서버 구축
- 계층형 아키텍처

## 시스템 아키텍처
<img src="Docs/image/SystemArchitecture.png" width="80%">

## ERD
<img src="Docs/image/ERD.png" width="80%" height="400px">

## 프로젝트 목적

### 차량용품 판매 사이트를 기획한 이유?
웹 개발에 대한 기본적인 이해와 실력을 쌓은 후, 머릿속에는 다양한 프로젝트 아이디어들이 떠올랐습니다.

여러 가지 사업 아이디어와 서비스 구상을 하면서, 개인적으로 가장 끌린 것은 "실제 시장에서 필요로 하는 서비스를 개발하는 것"이었습니다.

초기에는 개인적인 취미나 관심사를 바탕으로 프로젝트를 생각했지만, 차츰 웹 개발자로서 더 실용적이고, 실제로 사람들이 사용하는 서비스를 만들어보고 싶다는 목표가 커졌습니다.

이 프로젝트의 핵심은, 단순히 차량용품을 판매하는 온라인 쇼핑몰을 넘어서, 시스템의 안정성, 성능 등을 종합적으로 고려하여 개발하는 것이었습니다.

이를 통해 웹 애플리케이션 개발의 전반적인 흐름을 경험하고, 실제 서비스 구축에 대한 실력을 키우는 것이 제 목표였습니다.

웹 개발자로서 성장하려면 다양한 기술 스택을 활용한 실무 경험이 필수적이라고 생각했습니다. 

이 프로젝트를 통해, Front-end, Back-end, 데이터베이스 설계, 보안, 테스트 케이스 등 웹 애플리케이션에서 중요한 부분들을 체계적으로 학습하고, 실제 서비스에 적용해보았습니다.

## 주요 기능


### 소셜 로그인

소셜 로그인 구현을 위해 Spring Security와 OAuth2 인증 방식을 사용했으며,

소셜 인증 제공자 추가로 인한 확장을 대비하여 엑세스 토큰으로 받아오는 유저 정보를 OAuth2UserInfo 인터페이스로 추상화 하여 파싱하도록 설계했습니다.

[[Oauth2UserInfo인터페이스]](https://github.com/KimYongJ/Practice_Back/blob/master/practice_back/src/main/java/com/practice_back/entity/Oauth2/Oauth2UserInfo.java)

또한 확장성 있는 객체 생성을 위해, 객체 생성을 담당하는 클래스는 익명 인터페이스를 사용한 팩토리 메서드 패턴으로 구현하였습니다 .

[[UserInfoFactory 클래스]](https://github.com/KimYongJ/Practice_Back/blob/master/practice_back/src/main/java/com/practice_back/factory/FactoryUserInfo.java)

![sociallogin](/Docs/gif/sociallogin.gif)

##

### 비밀번호 찾기

JavaMailSender를 활용하여, 임시 비밀번호 발급 및 이메일 전송 기능을 구현했습니다.

[[JavaMailSender 코드]](https://github.com/KimYongJ/Practice_Back/blob/master/practice_back/src/main/java/com/practice_back/service/impl/EmailAuthServiceImpl.java)

![findpwd](/Docs/gif/findpwd.gif)

##

### 마스터 계정과 일반 사용자 계정 분리

Spring Security를 사용하여, 관리자와 일반 사용자의 권한을 구분하고 이에 따른 API 접근 제어를 설정했습니다. 이를 위해 filterChain을 커스터마이즈하여, 사용자 유형에 따라 접근 권한을 다르게 처리하도록 구현하였습니다.

- WebSecurityConfig에서 filterChain을 설정하여, Role-based Authorization을 구현했습니다.
- 관리자는 전체 API에 접근할 수 있도록 설정하고, 일반 사용자는 일부 제한된 API만 접근할 수 있도록 구성했습니다.


자세한 구현 내용은 아래 링크에서 확인하실 수 있습니다.

[[SecurityFilterChain 코드]](https://github.com/KimYongJ/Practice_Back/blob/master/practice_back/src/main/java/com/practice_back/config/WebSecurityConfig.java)

![master](/Docs/gif/master.gif)

##

### S3를 활용한 이미지 업로드 및 관리 기능 구현

AWS S3를 사용하여 이미지 업로드 기능을 구현하였습니다. 이 과정에서 S3의 설정을 분리하여 관리하기 쉽게 구성하였습니다.

[[S3Config 코드]](https://github.com/KimYongJ/Practice_Back/blob/master/practice_back/src/main/java/com/practice_back/config/S3Config.java)


계층형 아키텍처를 기반으로 시스템을 설계하였으며, 각 기능의 구현을 Impl 클래스로 분리하여 확장성을 고려하고, 구현체를 독립적으로 관리할 수 있는 구조로 구성하였습니다.

[[S3StorageServiceImpl 코드]](https://github.com/KimYongJ/Practice_Back/blob/master/practice_back/src/main/java/com/practice_back/service/impl/S3StorageServiceImpl.java)



![upload](/Docs/gif/upload.gif)

##

### 단위 테스트 및 통합 테스트 케이스를 작성해 자동화된 테스트 수행

프로젝트의 모든 계층(service, repository, entity등)에 대해 철저한 테스트 케이스를 작성해 실제 운영 환경에서 발생할 수 있는 다양한 시나오리에 대해 테스트를 진행했습니다. 

서비스단 테스트시 실제 인증 및 권한을 관리하기 위해 커스텀 어노테이션을 만들어 테스트 환경에서 모의 사용자 정보를 설정할 수 있도록 했습니다. 

[[어노테이션 코드]](https://github.com/KimYongJ/Practice_Back/blob/master/practice_back/src/test/java/com/practice_back/annotation/withMockUser/WithMockCustomUserSecurityContextFactory.java)

소셜 로그인 기능을 테스트하기 위해 Oauth2UserInfo 인터페이스를 구현한 모킹 클래스를 작성하여, 소셜 로그인 시 서버에서 반환되는 사용자 정보를 시뮬레이션했습니다.
이를 통해 실제 외부 시스템과의 상호작용 없이 테스트 환경에서 소셜 로그인 로직의 정확성과 데이터베이스 저장 과정을 검증할 수 있었습니다.

[[Mock객체 코드]](https://github.com/KimYongJ/Practice_Back/blob/master/practice_back/src/test/java/com/practice_back/mockClass/MockOauth2UserInfo.java)


##

### 카트 추가
![addcart](/Docs/gif/addcart.gif)

