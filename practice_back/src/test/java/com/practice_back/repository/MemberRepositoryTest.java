package com.practice_back.repository;

import com.practice_back.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.AssertTrue;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@SpringBootTest()
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @AfterEach
    void tearDown(){
        memberRepository.deleteAllInBatch();
    }
    @DisplayName("멤버 데이터를 저장한다")
    @Test
    void save(){
        // Given
        Member member1 = createMember("dummy1@naver.com","123");
        Member member2 = createMember("dummy2@naver.com","123");
        Member member3 = createMember("dummy3@naver.com","123");
        memberRepository.saveAll(List.of(member1, member2, member3));
        // When
        List<Member> members = memberRepository.findAllByEmailIn(List.of("dummy1@naver.com","dummy2@naver.com","dummy3@naver.com"));
        // Then
        assertThat(members).hasSize(3)
                .extracting("email","password")
                .containsExactlyInAnyOrder(
                        tuple("dummy1@naver.com","123"),
                        tuple("dummy2@naver.com","123"),
                        tuple("dummy3@naver.com","123")
                );
    }

    @DisplayName("아이디를 통해 멤버를 가져올 수 있다")
    @Test
    void findById(){
        // Given
        String email = "dummy1@naver.com";
        String password = "123";
        Member member1 = createMember(email,password);
        memberRepository.save(member1);
        // When
        Optional<Member> member = memberRepository.findById(email);
        // Then
        assertThat(member)
                .isPresent() // optional이 비어있지 않은지 확인
                .get()// Optional에서 실제 member 객체 추출
                .extracting("email","password")
                .containsExactly(email, password);
    }
    @DisplayName("Email를 통해 멤버를 가져올 수 있다")
    @Test
    void findByEmail(){
        // Given
        String email = "dummy1@naver.com";
        String password = "123";
        Member member1 = createMember(email,password);
        memberRepository.save(member1);
        // When
        boolean bool1 = memberRepository.existsByEmail(email);
        boolean bool2 = memberRepository.existsByEmail("-");
        // Then
        assertTrue(bool1);
        assertFalse(bool2);
    }
    @DisplayName("멤버 아이디 존재 유무를 확인할 수 있다")
    @Test
    void existsById(){
        // Given
        Member member1 = createMember("dummy1@naver.com","123");
        memberRepository.save(member1);
        // When
        boolean isExist = memberRepository.existsById("dummy1@naver.com");
        // Then
        assertThat(isExist).isTrue();
    }
    @DisplayName("ID를 통해 데이터 삭제가 가능하다")
    @Test
    void deleteById(){
        // Given
        Member member1 = createMember("dummy1@naver.com","123");
        memberRepository.save(member1);
        // When
        boolean isExist1 = memberRepository.existsById("dummy1@naver.com");
        int cnt = memberRepository.deleteById("dummy1@naver.com");
        boolean isExist2 = memberRepository.existsById("dummy1@naver.com");
        // Then
        assertThat(isExist1).isTrue();
        assertThat(isExist2).isFalse();
        assertThat(cnt).isEqualTo(1);
    }
    @DisplayName("ID를 통해 사진을 문자열로 가져올 수 있다")
    @Test
    void findPictureById(){
        // Given
        Member member1 = Member.builder()
                .id("dummy1@naver.com")
                .email("dummy1@naver.com")
                .password("123")
                .picture("picture")
                .build();
        memberRepository.save(member1);
        // When
       String picture = memberRepository.findPictureById("dummy1@naver.com");
        // Then
        assertThat(picture).isEqualTo("picture");
    }
    public Member createMember(String email, String pwd){
        return Member.builder()
                .id(email)
                .email(email)
                .password(pwd)
                .build();
    }
}