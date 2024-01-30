package com.practice_back.repository;

import com.practice_back.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //인터페이스가 저장소(Repository) 역할을 하며, 스프링에 의해 관리되는 빈임을 나타냄
public interface MemberRepository extends JpaRepository<Member, Long> {
    /*
    *  [ Optional ]
    *  - Optional은 요소의 존재 자체가 불확실한 상황에서 사용 할 수 있다. 객체를 포장(wrap)하는데 사용된다.
    *  - Optional은 예상치 못한 NullPointerException을 피하게 하며 메소드의 실행 결과값이 없을 수 있음을 명시적으로 표현하는 역할을 함
    *  - Optional 사용시 특정 동작을 수행하는 함수를 전용으로 제공한다( ifPresent(), orElse(), orElseThrow() 등 . )
    * */
    Optional<Member> findByEmail(String email); // 이메일을 기준으로 Member 조회
    boolean existsByEmail(String email);
    Member save(Member member);
    int deleteByEmail(String email);

}
