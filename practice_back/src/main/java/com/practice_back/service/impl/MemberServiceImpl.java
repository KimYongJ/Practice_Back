package com.practice_back.service.impl;

import com.practice_back.dto.MemberDTO;
import com.practice_back.entity.Member;
import com.practice_back.repository.MemberRepository;
import com.practice_back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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
}
