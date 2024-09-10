package com.practice_back.entity;

import com.practice_back.annotation.withMockUser.WithMockCustomUser;
import com.practice_back.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class BaseAuditTest {
    @Autowired
    MemberRepository memberRepository;
    @AfterEach
    void tearDown(){
        memberRepository.deleteAllInBatch();
    }
    @DisplayName("엔티티 생성시 수정일, 생성일, 수정자, 생성자가 기록된다.")
    @Test
    @WithMockCustomUser(username="KKK", password="1")
    void testAuditFields()throws Exception{
        // Given
        String loginUserName = "KKK";
        String email = "test@gmail.com";
        String pwd = "123";
        Member member = createMember(email, pwd);
        // When Then
        member = saveEntity(member);

        LocalDateTime insertDts1 = member.getInsertDts();
        LocalDateTime updateDts1 = member.getUpdateDts();
        String insertBy1 = member.getInsertBy();
        String updateBy1 = member.getUpdateBy();

        assertThat(insertDts1).isNotNull();
        assertThat(updateDts1).isNotNull();
        assertThat(insertBy1).isNotNull();
        assertThat(updateBy1).isNotNull();
        assertEquals(insertBy1, loginUserName);
        assertEquals(updateBy1, loginUserName);

        member = updateEntity(member, "01099997777");

        LocalDateTime insertDts2 = member.getInsertDts();
        LocalDateTime updateDts2 = member.getUpdateDts();
        
        assertEquals(insertDts1, insertDts2);
        assertNotEquals(updateDts1, updateDts2);
    }
    @Transactional
    public Member saveEntity(Member member){
        return memberRepository.save(member);
    }
    @Transactional
    public Member updateEntity(Member member, String phoneNumber){
        member.changePhoneNumber(phoneNumber);
        return memberRepository.save(member);
    }
    public Member createMember(String email, String pwd){
        return Member.builder()
                .id(email)
                .email(email)
                .password(pwd)
                .deliveryAddresses(new ArrayList<>())
                .build();
    }
}