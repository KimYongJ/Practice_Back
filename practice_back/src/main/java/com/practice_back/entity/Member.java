package com.practice_back.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.practice_back.dto.MemberDTO;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Entity                                     // 엔티티임을 알리는 어노테이션
@Builder                                    // 빌더
@AllArgsConstructor                         // 모든 필드 값을 파라미터로 받는 생성자를 만들어줌
@NoArgsConstructor                          // 파라미터가 없는 기본 생성자 생성
public class Member extends BaseAudit{

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    /*
    * Cart와 Memeber 엔티티는 1:1 관계이며 Member가 삭제될 때 Cart도 삭제되도록 cascade옵션 활성화(member 엔티티의 모든 변경사항(crud)이 Cart엔티티에도 적용됨
    * orphanRemoval = true 설정 이유 : Member엔티티가 삭제되어 Cart 엔티티가 더이상 참조되지 않는 상태가 되면 자동으로 삭제 하는 코드
    * mappedBy : 속성은 JPA에서 양뱡향 관계 설정시 사용하고 이 속성은 관계의 소유주가 아닌 쪽에 사용되고 소유주 엔티티의 필드 이름을 갖는다.
    *            mappedBy를 사용해 어느 쪽이 관계를 소유하는지 명시하여 두 엔티티 간의 중복 매핑을 방지한다.
    *           mappedBy = "member"를 적은 이유는 Cart엔티티 내의 member 필드가 관계의 소유주임을 의미하기 때문이다.
    *           Cart 엔티티에서는 member와 연결을 위해 JoinColumn를 사용한다.
     * */
    @OneToOne(  mappedBy = "member",
                cascade = CascadeType.ALL,
                orphanRemoval = true)
    @JsonIgnoreProperties("member") // Cart 엔티티 내의 member 필드를 직렬화에서 제외(순환 참조 방지로 Member엔티티가 Cart를 불러올 때 Cart안에 Member엔티티는 불러오지 않음.)
    private Cart cart;

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

    public static MemberDTO toDTO(Member member){
        return MemberDTO.builder()
                .email(member.email)
                .password(member.password)
                .authority(member.authority)
                .build();
    }
    public MemberDTO of(Member member){
        return MemberDTO.builder()
                .email(member.getEmail())
                .authority(member.getAuthority())
                .build();
    }
}
