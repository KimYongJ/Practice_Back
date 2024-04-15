package com.practice_back.config;

import com.practice_back.entity.Authority;
import com.practice_back.entity.Cart;
import com.practice_back.entity.Member;
import com.practice_back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class DataInitializationConfig {
    private final PasswordEncoder   passwordEncoder;
    private final MemberRepository  memberRepository;

    @Value("${app.masterid}")
    private String masterid;
    @Value("${app.masterpwd}")
    private String masterpwd;
    @PostConstruct
    public void initData() {
        if (!memberRepository.existsById(masterid)) {
            Member master = Member.builder()
                    .id(masterid)
                    .email(masterid)
                    .password(passwordEncoder.encode( masterpwd ))
                    .authority(Authority.ROLE_ADMIN)
                    .cart(new Cart())
                    .build();
            master.getCart()
                    .setMember(master);
            memberRepository.save(master);
        }
    }
}

