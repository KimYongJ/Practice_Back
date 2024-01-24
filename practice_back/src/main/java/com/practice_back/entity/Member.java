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

    @Column(name = "authority")
    private String authority;

    public static MemberDTO toMemberDTO(Member member){
        return MemberDTO.builder()
                .email(member.email)
                .password(member.password)
                .authority(member.authority)
                .build();
    }
}
