package com.practice_back.service.impl;

import com.practice_back.annotation.withMockUser.WithMockCustomUser;
import com.practice_back.dto.DeliveryAddressDTO;
import com.practice_back.entity.DeliveryAddress;
import com.practice_back.entity.Member;
import com.practice_back.repository.DeliveryAddressRepository;
import com.practice_back.repository.MemberRepository;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DeliveryAddressServiceImplTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    DeliveryAddressRepository deliveryAddressRepository;
    @Autowired
    DeliveryAddressServiceImpl deliveryAddressService;
    @BeforeEach
    void setUp(){
        Member member = createMember("kyj","1");
        memberRepository.save(member);
        DeliveryAddress del1 = createDeliveryAddress(member,"김씨","010-1234-5678","11111","서울 강남구","111-1",false);
        DeliveryAddress del2 = createDeliveryAddress(member,"박씨","010-3216-6547","22222","부산시 사하구","222-2",false);
        DeliveryAddress del3 = createDeliveryAddress(member,"정씨","010-9874-6544","33333","춘천시 온의동","333-3",false);
        deliveryAddressRepository.saveAll(List.of(del1,del2,del3));
    }

    @AfterEach
    void tearDown(){
        deliveryAddressRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("로그인 아이디로 배송정보를 모두 가져올 수 있다")
    @Test
    @WithMockCustomUser(username="kyj", password = "1")
    void getAllDeliveryInfo(){
        // Given When
        ResponseEntity<Object> result = deliveryAddressService.getAllDeliveryInfo();
        // Then
        assertThat(result).isNotNull()
                .satisfies(res->{
                    assertEquals(res.getStatusCode(), HttpStatus.OK);
                    Message msg = (Message)result.getBody();
                    assertEquals(msg.getStatus(), ErrorType.OK);
                    assertEquals(msg.getMessage(),"성공");
                    assertThat((List<DeliveryAddressDTO>)msg.getData())
                            .hasSize(3)
                            .extracting("recipient", "contactNumber", "postalCode", "address", "addressDetail","isPrimary")
                            .containsExactlyInAnyOrder(
                                    tuple("김씨","010-1234-5678","11111","서울 강남구","111-1",false),
                                    tuple("박씨","010-3216-6547","22222","부산시 사하구","222-2",false),
                                    tuple("정씨","010-9874-6544","33333","춘천시 온의동","333-3",false)
                            );
                });
    }
    @DisplayName("주소를 객체를 통해 저장할 수 있다")
    @Test
    @WithMockCustomUser(username="kyj", password = "1")
    void insertDeliveryInfo(){
        // Given
        DeliveryAddressDTO deliveryAddressDTO = createDeliveryDTO("하씨","010-9999-1111","44444","공주시 목양동","444-4",false);
        // When
        deliveryAddressService.insertDeliveryInfo(deliveryAddressDTO);
        ResponseEntity<Object> result = deliveryAddressService.getAllDeliveryInfo();
        // Then
        assertThat(result).isNotNull()
                        .satisfies(res->{
                            assertEquals(res.getStatusCode(), HttpStatus.OK);
                            Message msg = (Message)result.getBody();
                            assertEquals(msg.getStatus(), ErrorType.OK);
                            assertEquals(msg.getMessage(),"성공");
                            assertThat((List<DeliveryAddressDTO>)msg.getData())
                                    .hasSize(4)
                                    .extracting("recipient", "contactNumber", "postalCode", "address", "addressDetail","isPrimary")
                                    .containsExactlyInAnyOrder(
                                            tuple("김씨","010-1234-5678","11111","서울 강남구","111-1",false),
                                            tuple("박씨","010-3216-6547","22222","부산시 사하구","222-2",false),
                                            tuple("정씨","010-9874-6544","33333","춘천시 온의동","333-3",false),
                                            tuple("하씨","010-9999-1111","44444","공주시 목양동","444-4",false)
                                    );
                        });
    }
    @DisplayName("객체를 통해 기존 데이터를 수정할 수 있다")
    @Test
    @WithMockCustomUser(username="kyj", password = "1")
    void patchDeliveryInfo(){
        // Given
        DeliveryAddressDTO deliveryAddressDTO = createDeliveryDTO("하씨","010-9999-1111","4444","공주시 목양동","444-4",false);
        ResponseEntity<Object> response = deliveryAddressService.insertDeliveryInfo(deliveryAddressDTO);
        DeliveryAddressDTO responseDTO = (DeliveryAddressDTO)((Message)response.getBody()).getData();
        // When
        responseDTO.setRecipient("장씨");
        responseDTO.setContactNumber("010-8888-7777");
        responseDTO.setPostalCode("33333");
        responseDTO.setAddress("부산시 목동");
        responseDTO.setAddressDetail("555-5");
        responseDTO.setIsPrimary(true);

        Message returnMsg = (Message)deliveryAddressService.patchDeliveryInfo(responseDTO).getBody();
        ResponseEntity<Object> result = deliveryAddressService.getAllDeliveryInfo();

        // Then
        assertEquals(returnMsg.getMessage(),"수정되었습니다.");
        assertThat(result).isNotNull()
                        .satisfies(res->{
                            assertEquals(res.getStatusCode(), HttpStatus.OK);
                            Message msg = (Message)res.getBody();
                            assertEquals(msg.getStatus(), ErrorType.OK);
                            assertEquals(msg.getMessage(),"성공");
                            assertThat((List<DeliveryAddressDTO>)msg.getData())
                                    .hasSize(4)
                                    .extracting("recipient", "contactNumber", "postalCode", "address", "addressDetail","isPrimary")
                                    .containsExactlyInAnyOrder(
                                            tuple("김씨","010-1234-5678","11111","서울 강남구","111-1",false),
                                            tuple("박씨","010-3216-6547","22222","부산시 사하구","222-2",false),
                                            tuple("정씨","010-9874-6544","33333","춘천시 온의동","333-3",false),
                                            tuple("장씨","010-8888-7777","33333","부산시 목동","555-5",true)
                                    );
                        });
    }

    @DisplayName("배송지 객체 데이터를 통해 기본 배송지를 변경할 수 있다.")
    @Test
    @WithMockCustomUser(username="kyj", password = "1")
    void patchDeliveryPrimary(){
        // Given
        DeliveryAddressDTO deliveryAddressDTO = createDeliveryDTO("하씨","010-9999-1111","4444","공주시 목양동","444-4",false);
        ResponseEntity<Object> response = deliveryAddressService.insertDeliveryInfo(deliveryAddressDTO);
        DeliveryAddressDTO responseDTO = (DeliveryAddressDTO)((Message)response.getBody()).getData();
        // When
        responseDTO.setIsPrimary(true);
        Message returnMsg = (Message)deliveryAddressService.patchDeliveryPrimary(responseDTO).getBody();
        ResponseEntity<Object> result = deliveryAddressService.getAllDeliveryInfo();
        // Then
        assertEquals(returnMsg.getMessage(),"수정되었습니다.");
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertThat(result).isNotNull()
                        .satisfies(res->{
                            assertEquals(res.getStatusCode(),HttpStatus.OK);
                            Message msg = (Message) res.getBody();
                            assertEquals(msg.getStatus(), ErrorType.OK);
                            assertEquals(msg.getMessage(),"성공");
                            assertThat((List<DeliveryAddressDTO>)msg.getData())
                                    .hasSize(4)
                                    .extracting("recipient", "contactNumber", "postalCode", "address", "addressDetail","isPrimary")
                                    .containsExactlyInAnyOrder(
                                            tuple("김씨","010-1234-5678","11111","서울 강남구","111-1",false),
                                            tuple("박씨","010-3216-6547","22222","부산시 사하구","222-2",false),
                                            tuple("정씨","010-9874-6544","33333","춘천시 온의동","333-3",false),
                                            tuple("하씨","010-9999-1111","4444","공주시 목양동","444-4",true)
                                    );
                        });
    }
    @DisplayName("배송지 아이디만을 전달하여 배송지를 삭제할 수 있다.")
    @Test
    @WithMockCustomUser(username="kyj", password = "1")
    void deleteDeliveryInfo(){
        // Given
        DeliveryAddressDTO deliveryAddressDTO = createDeliveryDTO("하씨","010-9999-1111","4444","공주시 목양동","444-4",false);
        ResponseEntity<Object> response = deliveryAddressService.insertDeliveryInfo(deliveryAddressDTO);
        DeliveryAddressDTO responseDTO = (DeliveryAddressDTO)((Message)response.getBody()).getData();
        // When
        ResponseEntity<Object> deleteResult = deliveryAddressService.deleteDeliveryInfo(responseDTO.getDeliveryAddressId());
        ResponseEntity<Object> result = deliveryAddressService.getAllDeliveryInfo();
        // Then
        assertThat(deleteResult).isNotNull()
                        .satisfies(res->{
                            assertEquals(res.getStatusCode(),HttpStatus.OK);
                            Message msg = (Message)res.getBody();
                            assertEquals(msg.getStatus(),ErrorType.OK);
                            assertEquals(msg.getMessage(),"삭제되었습니다.");
                        });
        assertThat(result).isNotNull()
                        .satisfies(res->{
                            assertEquals(res.getStatusCode(),HttpStatus.OK);
                            assertThat(res.getBody()).isInstanceOf(Message.class);
                            Message msg = (Message)res.getBody();
                            List<DeliveryAddressDTO> listDTO = (List<DeliveryAddressDTO>)msg.getData();
                            assertEquals(msg.getStatus(), ErrorType.OK);
                            assertEquals(msg.getMessage(),"성공");
                            assertThat(listDTO)
                                    .hasSize(3)
                                    .extracting("recipient", "contactNumber", "postalCode", "address", "addressDetail","isPrimary")
                                    .containsExactlyInAnyOrder(
                                            tuple("김씨","010-1234-5678","11111","서울 강남구","111-1",false),
                                            tuple("박씨","010-3216-6547","22222","부산시 사하구","222-2",false),
                                            tuple("정씨","010-9874-6544","33333","춘천시 온의동","333-3",false)
                                    );
                        });
    }

    public DeliveryAddressDTO createDeliveryDTO(String recipient, String contactNumber, String postalCode, String address, String addressDetail,boolean isPrimary) {
        return DeliveryAddressDTO.builder()
                .recipient(recipient)
                .contactNumber(contactNumber)
                .postalCode(postalCode)
                .address(address)
                .addressDetail(addressDetail)
                .isPrimary(isPrimary)
                .build();
    }
    public DeliveryAddress createDeliveryAddress(Member member, String recipient, String contactNumber, String postalCode, String address, String addressDetail,boolean isPrimary) {
        return DeliveryAddress.builder()
                .recipient(recipient)
                .contactNumber(contactNumber)
                .postalCode(postalCode)
                .address(address)
                .addressDetail(addressDetail)
                .isPrimary(isPrimary)
                .member(member)
                .build();
    }
    public Member createMember(String email, String pwd){
        return Member.builder()
                .id(email)
                .email(email)
                .deliveryAddresses(new ArrayList<>())
                .password(pwd)
                .build();
    }
}