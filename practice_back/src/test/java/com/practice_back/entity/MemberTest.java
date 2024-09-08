package com.practice_back.entity;

import com.practice_back.dto.DeliveryAddressDTO;
import com.practice_back.dto.MemberDTO;
import com.practice_back.dto.UserProfileDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberTest {
    @DisplayName("Member 객체를 생성할 수 있다.")
    @Test
    void createMembmerEntity(){
        // Given
        String email = "kkk@gmail.com";
        String password = "123";
        String phoneNumber = "01055551111";
        String picture = "url";
        Authority authority = Authority.ROLE_USER;
        Cart cart = new Cart();
        cart.setId(3L);
        // When
        Member member = Member.builder()
                .email(email)
                .picture(picture)
                .password(password)
                .phoneNumber(phoneNumber)
                .authority(authority)
                .cart(cart)
                .deliveryAddresses(new ArrayList<>())
                .build();
        // Then
        assertThat(member)
                .satisfies(res->{
                    assertEquals(res.getEmail(), email);
                    assertEquals(res.getPassword(), password);
                    assertEquals(res.getPhoneNumber(),phoneNumber);
                    assertEquals(res.getPicture(), picture);
                    assertEquals(res.getAuthority(), authority);
                    assertEquals(res.getCart().getId(), 3L);
                });
    }
    @DisplayName("객체의 비밀번호와, 휴대폰번호, 사진을 함수로 변경할 수 있다.")
    @Test
    void changePasswordAndPhoneNumberAndPicture(){
        // Given
        String email = "kkk@gmail.com";
        String password = "123";
        String phoneNumber = "01055551111";
        String picture = "url";

        Member member = Member.builder()
                .email(email)
                .picture(picture)
                .password(password)
                .phoneNumber(phoneNumber)
                .build();
        // When
        String changePwd = "321";
        String changePhone = "01011112222";
        String changePicture = "LLL";
        member.changePassword(changePwd);
        member.changePhoneNumber(changePhone);
        member.changePicture(changePicture);
        // Then
        assertThat(member)
                .satisfies(res->{
                    assertEquals(res.getPassword(), changePwd);
                    assertEquals(res.getPhoneNumber(),changePhone);
                    assertEquals(res.getPicture(), changePicture);
                });
    }
    @DisplayName("Member객체를 UserProfileDTO로 변경 할 수 있다.")
    @Test
    void toDTO(){
        // Given
        String email        = "kkk@gmail.com";
        String phoneNumber  = "01055551111";
        String picture      = "url";
        String address      = "서울 강남구";
        Member member       = Member.builder()
                                .email(email)
                                .picture(picture)
                                .authority(Authority.ROLE_USER)
                                .phoneNumber(phoneNumber)
                                .deliveryAddresses(new ArrayList<>())
                                .build();

        List<DeliveryAddress> deliveryList = member.getDeliveryAddresses();
        DeliveryAddress delObj = DeliveryAddress.builder()
                .isPrimary(true)
                .address(address)
                .member(member)
                .build();

        deliveryList.add(delObj);
        // When
        UserProfileDTO userProfileDTO = Member.toDTO(member);
        // Then
        assertThat(userProfileDTO)
                .satisfies(res->{
                    assertEquals(res.getEmail(), email);
                    assertEquals(res.getPhoneNumber(), phoneNumber);
                    assertEquals(res.getPicture(), picture);
                    assertFalse(res.isMaster());

                    DeliveryAddressDTO deliveryAddressDTO = res.getDeliveryAddressDTO();
                    assertTrue(deliveryAddressDTO.getIsPrimary());
                    assertEquals(deliveryAddressDTO.getAddress(), address);
                });
    }
    @DisplayName("Member 객체를 MemberDTO로 변경할 수 있다.")
    @Test
    void of(){
        // Given
        String email    = "kkk@gmail.com";
        Authority authority = Authority.ROLE_USER;
        Member member   = Member.builder()
                            .email(email)
                            .authority(authority)
                            .build();
        // When
        MemberDTO memberDTO = member.of();
        // Then
        assertEquals(memberDTO.getEmail(), email);
        assertEquals(memberDTO.getAuthority(), authority);
    }
}