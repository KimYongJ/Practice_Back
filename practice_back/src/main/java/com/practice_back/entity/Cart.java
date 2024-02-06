package com.practice_back.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.practice_back.dto.CartDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity                                     // 엔티티임을 알리는 어노테이션
@Builder                                    // 빌더
@AllArgsConstructor                         // 모든 필드 값을 파라미터로 받는 생성자 생성
@NoArgsConstructor                          // 파라미터가 없는 기본 생성자 생성
public class Cart extends BaseAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_email", referencedColumnName = "email")
    @JsonIgnoreProperties("cart") // Member 엔티티 내의 cart 필드를 직렬화에서 제외(순환 참조 방지로 Cart클래스가 Member를 불러올 때 Member안에 Cart엔티티는 불러오지 않음.)
    private Member member;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();// List<CartItem> cartItems = new ArrayList<>();


    public static CartDTO toDTO(Cart cart){
        return CartDTO.builder()
                .cartItemsDTO(cart.getCartItems().stream()
                        .map(CartItem::toDTO).collect(Collectors.toList()))
                .build();
    }

}
