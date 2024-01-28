package com.practice_back.entity;


import com.practice_back.dto.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    /*
    * [@Enumerated]
    * - JPA사용시 엔티티의 필드가 열거 타입임을 지정할 때 사용함, 해당 어노테이션 사용시 지정된 열거형의 이름을 데이터베이스에 문자열로 저장할 수 있다.
    * - EnumType.STRING 사용시 Authority필드는 Authority enum의 이름("ROLE_USER" 또는 "ROLE_ADMIN")을 문자열로 저장함
    * - 데이터베이스 엔티티 검색시 저장된 문자열을 다시 해당 enum 값으로 변환함.
    * - EnumType.ORDINAL 사용시 enum값이 데이터베이스에 정수 인덱스 값을 조정됨, enum에서 ROLE_USER가 첫 번째 값이라면 0으로, ROLE_ADMIN이 두 번째 값이라면 1로 저장되어 가독성이 안좋아짐
    * */
    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Authority authority;

    public static MemberDTO toMemberDTO(Member member){
        return MemberDTO.builder()
                .email(member.email)
                .password(member.password)
                .authority(member.authority)
                .build();
    }
    public MemberDTO of(Member member){
        return MemberDTO.builder()
                .email(email)
                .authority(authority)
                .build();
    }
}
