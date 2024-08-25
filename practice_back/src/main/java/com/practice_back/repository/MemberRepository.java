package com.practice_back.repository;

import com.practice_back.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    /**
    *  [ Optional ]
    *  - Optional은 요소의 존재 자체가 불확실한 상황에서 사용 할 수 있다. 객체를 포장(wrap)하는데 사용된다.
    *  - Optional은 예상치 못한 NullPointerException을 피하게 하며 메소드의 실행 결과값이 없을 수 있음을 명시적으로 표현하는 역할을 함
    *  - Optional 사용시 특정 동작을 수행하는 함수를 전용으로 제공한다( ifPresent(), orElse(), orElseThrow() 등 . )
    * */
    Optional<Member> findById(String id);

    List<Member> findAllByEmailIn(List<String> email);
    boolean existsById(String Id);
    boolean existsByEmail(String email);
    Member save(Member member);
    int deleteById(String Id);
    @Query("SELECT m.picture from Member m WHERE m.id =:id")
    String findPictureById(@Param("id")String id); // ClassCastException 오류를 막기위해 JPA 작명 규칙이 아닌 어노테이션으로 사용
}
