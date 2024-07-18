package com.practice_back.repository;

import com.practice_back.entity.DeliveryAddress;
import com.practice_back.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@SpringBootTest()
@Transactional
class DeliveryAddressRepositoryTest {

    @Autowired
    DeliveryAddressRepository deliveryAddressRepository;
    @Autowired
    MemberRepository memberRepository;
    @AfterEach
    void tearDown(){
        deliveryAddressRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
    @DisplayName("사용자 ID로 주소 정보를 가져올 수 있다.")
    @Test
    void findByMemberId(){
        // Given
        Member member = createMember("dummy1@naver.com","123");
        memberRepository.save(member);
        DeliveryAddress del1 = createDeliveryAddres("부산시","문현동 3-3","고길동","010-1234-5678","55555",member);
        DeliveryAddress del2 = createDeliveryAddres("성남시","신흥동3-3","둘리","010-1234-5678","55555",member);
        deliveryAddressRepository.saveAll(List.of(del1, del2));
        // When
        List<DeliveryAddress> result = deliveryAddressRepository.findByMemberId(member.getId());
        // Then
        assertThat(result)
                .hasSize(2)
                .extracting("address","addressDetail","recipient","contactNumber","postalCode")
                .containsExactlyInAnyOrder(
                        tuple("부산시","문현동 3-3","고길동","010-1234-5678","55555"),
                        tuple("성남시","신흥동3-3","둘리","010-1234-5678","55555")
                );
    }
    @DisplayName("배송주소 아이디로 객체를 가져올 수 있다.")
    @Test
    void findByDeliveryAddressId(){
        // Given
        Member member = createMember("dummy1@naver.com","123");
        memberRepository.save(member);
        DeliveryAddress del1 = createDeliveryAddres("부산시","문현동 3-3","고길동","010-1234-5678","55555",member);
        DeliveryAddress del2 = createDeliveryAddres("성남시","신흥동3-3","둘리","010-1234-5678","55555",member);
        deliveryAddressRepository.saveAll(List.of(del1, del2));
        // When
        Optional<DeliveryAddress> result = deliveryAddressRepository.findByDeliveryAddressId(2L);
        // Then
        assertThat(result)
                .isPresent()
                .get()
                .extracting("address","addressDetail","recipient","contactNumber","postalCode")
                .containsExactly("성남시","신흥동3-3","둘리","010-1234-5678","55555");
    }
    public DeliveryAddress createDeliveryAddres(String address,String adDetail, String recipient,
                                                String contactNum, String postCd, Member member){
        return DeliveryAddress.builder()
                .address(address)
                .addressDetail(adDetail)
                .recipient(recipient)
                .contactNumber(contactNum)
                .postalCode(postCd)
                .member(member)
                .isPrimary(false)
                .build();
    }
    public Member createMember(String email, String pwd){
        return Member.builder()
                .id(email)
                .email(email)
                .password(pwd)
                .build();
    }
}