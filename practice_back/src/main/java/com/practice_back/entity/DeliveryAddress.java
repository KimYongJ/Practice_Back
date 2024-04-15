package com.practice_back.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.practice_back.dto.DeliveryAddressDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity                         // 엔티티임을 알리는 어노테이션
@Builder                        // 빌더
@AllArgsConstructor             // 모든 필드 값을 파라미터로 받는 생성자 생성
@NoArgsConstructor              // 파라미터가 없는 기본 생성자 생성
public class DeliveryAddress extends BaseAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryAddressId;

    @ManyToOne
    @JoinColumn(name = "id")
    @JsonIgnoreProperties("deliveryAddresses")
    private Member member;

    @Column(length = 100, nullable = false)
    private String recipient;           // 수령인

    @Column(length = 20, nullable = false)
    private String contactNumber;       // 수령인 전화번호

    @Column(length = 10, nullable = false)
    private String postalCode;          // 우편번호

    @Column(length = 255, nullable = false)
    private String address;             // 배송지 전체 주소

    @Column(length = 255)
    private String addressDetail;       // 상세 주소

    @Column(nullable = false)
    private Boolean isPrimary = false;  // 기본 배송지 여부

    public void makePrimary(){
        this.isPrimary = true;
    }
    public void makeNotPrimary(){
        this.isPrimary = false;
    }
    // 엔티티 업데이트를 위한 메소드
    public void update(DeliveryAddressDTO dto) {
        if (dto.getRecipient()  != null)         this.recipient      = dto.getRecipient();
        if (dto.getContactNumber()  != null)     this.contactNumber  = dto.getContactNumber();
        if (dto.getPostalCode()     != null)     this.postalCode     = dto.getPostalCode();
        if (dto.getAddress()        != null)     this.address        = dto.getAddress();
        if (dto.getAddressDetail()  != null)     this.addressDetail  = dto.getAddressDetail();
        if (dto.getIsPrimary()      != null)     this.isPrimary      = dto.getIsPrimary();
    }
    public static DeliveryAddressDTO toDTO(DeliveryAddress deliveryAddress){
        return DeliveryAddressDTO.builder()
                .deliveryAddressId(deliveryAddress.getDeliveryAddressId())
                .recipient(deliveryAddress.getRecipient())
                .contactNumber(deliveryAddress.getContactNumber())
                .postalCode(deliveryAddress.getPostalCode())
                .address(deliveryAddress.getAddress())
                .addressDetail(deliveryAddress.getAddressDetail())
                .isPrimary(deliveryAddress.getIsPrimary())
                .build();
    }
}
