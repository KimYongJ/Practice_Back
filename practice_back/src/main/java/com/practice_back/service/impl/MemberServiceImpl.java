package com.practice_back.service.impl;

import com.practice_back.dto.MemberDTO;
import com.practice_back.entity.Member;
import com.practice_back.repository.MemberRepository;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import com.practice_back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.practice_back.jwt.TokenProvider.getCurrentMemberInfo;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    /*
    * 회원 가입시 이메일이 존재하는지 확인하는 함수
    *
    * @return boolean
    * */
    @Override
    public boolean existsByEmail(String email){
        return memberRepository.existsByEmail(email);
    }

    /*
    *
    * */
    @Override
    public Optional<Member> findByEmail(String email){
        return memberRepository.findByEmail(email);
    }
    /*
    * 회원 저장 함수
    *
    * @return Member
    * */
    @Override
    public Member save(Member memeber) {
        return memberRepository.save(memeber);
    }

    @Override
    public ResponseEntity<Object> deleteByEmail(){
        String email = getCurrentMemberInfo();
        int num =  memberRepository.deleteByEmail(email);
        boolean bool = num > 0;
        ErrorType error = bool ? ErrorType.ACCOUNT_DELETION_SUCCESS : ErrorType.BAD_REQUEST;
        return ResponseEntity.ok()
                .body(new Message(error,error.getErrStr(),num));
    }
}
