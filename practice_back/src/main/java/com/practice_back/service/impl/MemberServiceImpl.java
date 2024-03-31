package com.practice_back.service.impl;

import com.practice_back.dto.MemberDTO;
import com.practice_back.dto.PasswordDTO;
import com.practice_back.dto.UserProfileDTO;
import com.practice_back.entity.DeliveryAddress;
import com.practice_back.entity.Member;
import com.practice_back.jwt.TokenProvider;
import com.practice_back.repository.MemberRepository;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import com.practice_back.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.practice_back.jwt.TokenProvider.contextReset;
import static com.practice_back.jwt.TokenProvider.getCurrentMemberInfo;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;      // 비밀번호 저장시 암호화를 위해 사용
    /**
     * 회원 정보 조회
     *
     * @return ResponseEntity<Object>
     * */
    public ResponseEntity<Object> getUserProfile(){
        String email = getCurrentMemberInfo();
        UserProfileDTO userProfileDTO = memberRepository.findByEmail(email).map(Member::toDTO)
                .orElseThrow(()-> new UsernameNotFoundException(email + " 을 DB에서 찾을 수 없습니다"));

        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK,"조회 완료",userProfileDTO));
    }
    /**
     * 회원 정보 업데이트
     *
     * @return ResponseEntity<Object>
     * */
    public ResponseEntity<Object> updateProfile(HttpServletRequest request, UserProfileDTO userProfileDTO){
        String tmpToken = tokenProvider.getToken(request, "tempToken");
        if(tmpToken == null){
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                    .body(new Message(ErrorType.MOVED_PERMANENTLY, "Temp 유효시간 만료","checkuser"));
        }
        String email = getCurrentMemberInfo();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email + " 을 DB에서 찾을 수 없습니다"));

        String phoneNumber = userProfileDTO.getPhoneNumber();
        member.changePhoneNumber(phoneNumber);
        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK,"변경되었습니다.", phoneNumber));
    }
    /**
     * 비밀번호 변경
     *
     * @return ResponseEntity<Object>
     * */
    public ResponseEntity<Object> updatePassword(HttpServletRequest request,PasswordDTO passwordDTO){
        String tmpToken = tokenProvider.getToken(request, "tempToken");
        if(tmpToken == null){
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                    .body(new Message(ErrorType.MOVED_PERMANENTLY, "Temp 유효시간 만료","checkuser"));
        }
        String email = getCurrentMemberInfo();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException(email + " 을 DB에서 찾을 수 없습니다"));

        String newPwd = passwordDTO.getNewPassword();
        String comPwd = passwordDTO.getNewPasswordConfirm();
        if(!newPwd.equals(comPwd))
        {
            return ResponseEntity.badRequest()
                    .body(new Message(ErrorType.INVALID_PASSWORD, "유효하지 않은 비밀번호입니다.", null));
        }else if(passwordEncoder.matches(newPwd, member.getPassword()) ){
            return ResponseEntity.badRequest()
                    .body(new Message(ErrorType.PASSWORD_NO_CHANGE, "기존 비밀번호와 같습니다. 다른 비밀번호를 사용해주세요.", null));
        }
        else {
            member.changePassword(passwordEncoder.encode(newPwd));
            memberRepository.save(member);
            return ResponseEntity.ok()
                    .body(new Message(ErrorType.OK, "변경되었습니다.", null));
        }
    }
    /**
    * 회원 가입시 이메일이 존재하는지 확인하는 함수
    *
    * @return boolean
    * */
    @Override
    public boolean existsByEmail(String email){
        return memberRepository.existsByEmail(email);
    }

    /**
    *  이메일로 member 객체 조회
    *
    * @return Optional<Member>
    * */
    @Override
    public Optional<Member> findByEmail(String email){
        return memberRepository.findByEmail(email);
    }
    /**
    * 회원 저장 함수
    *
    * @return Member
    * */
    @Override
    public Member save(Member memeber) {
        return memberRepository.save(memeber);
    }

    /**
     * 회원 탈퇴 함수
     *
     * @return ResponseEntity<Object>
     * */
    @Override
    public ResponseEntity<Object> deleteByEmail(HttpServletRequest request, HttpServletResponse response){
        String email = getCurrentMemberInfo();
        int num =  memberRepository.deleteByEmail(email);
        boolean bool = num > 0;

        ErrorType error = bool ? ErrorType.ACCOUNT_DELETION_SUCCESS : ErrorType.BAD_REQUEST;

        tokenProvider.cookieReset(response,"accessToken"); // 쿠키 리셋
        contextReset(); // 인증 리셋

        return ResponseEntity.ok()
                .body(new Message(error,error.getErrStr(),num));
    }
}
