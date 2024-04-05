package com.practice_back.service.impl;

import com.practice_back.entity.Member;
import com.practice_back.repository.MemberRepository;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import com.practice_back.service.EmailAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailAuthServiceImpl implements EmailAuthService {

    private final JavaMailSenderImpl    mailSender;        // Spring Framework에서 제공하는 메일 발송 객체
    private final MemberRepository      memberRepository;  // 변경된 비밀번호를 저장하기 위해 사용
    private final PasswordEncoder       passwordEncoder;   // 비밀번호 저장시 암호화를 위해 사용
    private final ArrayList<String> charList = new ArrayList<String>(){{
        add("abcdefghijklmnopqrstuvwxyz");
        add("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        add("0123456789");
        add("!@#$%^&*()_+");
    }};
    private static final SecureRandom random = new SecureRandom();
    /**
     * 임시 비밀번호를 생성하고 사용자에게 이메일로 발송합니다.
     * @param email 수신자 이메일 주소
     */
    @Override
    public ResponseEntity<Object> sendNewPassword(String email){
        if(!memberRepository.existsByEmail(email)){
            return ResponseEntity.badRequest()
                    .body(new Message(ErrorType.EMAIL_NOT_FOUND, "해당하는 이메일이 없습니다.", email));
        }

        String newPassword = generateRandomPassword();
        String content = buildEmailContent(newPassword);
        return sendEmail(email, "임시 비밀번호 발급 이메일 입니다.", content, newPassword);
    }

    /**
     * 임시 비밀번호를 생성합니다.
     * @return 생성된 임시 비밀번호 문자열
     */
    @Override
    public String generateRandomPassword() {
        StringBuffer newPassword = new StringBuffer();
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 12;
        for(String str : charList)
        {
            newPassword.append( str.charAt( random.nextInt(str.length()) ) );
        }
        newPassword.append(
            random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString()
        );
        return newPassword.toString();
    }
    /**
     * 메일 내용을 구성합니다.
     * @param password 발급된 임시 비밀번호
     * @return 메일 본문
     */
    @Override
    public String buildEmailContent(String password) {
        return String.format(
                "<div style='font-family: Arial, sans-serif; text-align: center; color: #333;  padding: 40px;'>" +
                        "<h1>임시 비밀번호 안내</h1>" +
                        "<hr style='border: none; height: 1px; background-color: #ddd; margin: 20px 0;'>" + // Horizontal line
                        "<div style='background-color: #fff; padding: 20px;'>" +
                        "<h2 style='color: #5D76A9;'>임시 비밀번호</h2>" +
                        "<p style='font-size: 24px; color: #FF9900; margin: 20px 0;'>%s</p>" + // Password highlight
                        "<p style='font-size: 16px; color: #666;'>위 비밀번호로 로그인 후 비밀번호를 변경해주세요.</p>" +
                        "</div>" +
                        "</div>",
                password
        );
    }

    /**
     * 이메일을 발송합니다.
     * @param to 수신자 이메일 주소
     * @param subject 메일 제목
     * @param content 메일 내용
     */
    @Override
    public ResponseEntity<Object> sendEmail(String email, String subject, String content, String newPassword) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(email);                  // 보내는사람 생략하면 정상작동을 하지 않는 경우가 있음 아무 메일을 적어도 구글 계정으로 발신자 셋팅
            helper.setTo(email);                    // 받는사람 이메일
            helper.setSubject(subject);             // 메일제목은 생략이 가능
            helper.setText(content, true);     // 파라미터순서 : (1)이메일 본문의 내용을 나타내는 문자열 , (2) boolean 타입으로 html 형식 인지 아닌지

            mailSender.send(message);

            // email로 맴버를 찾음 -> entity비번 변경 -> save
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("사용자 정보가 잘못되었습니다."));
            member.changePassword( passwordEncoder.encode(newPassword) );
            memberRepository.save(member);
            return ResponseEntity.ok()
                    .body(new Message(ErrorType.OK, "이메일을 확인하세요!", email));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(new Message(ErrorType.INTERNAL_SERER_ERROR, "발송 실패", email));
        }
    }
}
