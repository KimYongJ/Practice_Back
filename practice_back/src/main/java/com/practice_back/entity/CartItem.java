package com.practice_back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.practice_back.dto.CartItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter                                     // 갯터
@Entity                                     // 엔티티임을 알리는 어노테이션
@Builder                                    // 빌더
@AllArgsConstructor                         // 모든 필드 값을 파라미터로 받는 생성자를 만들어줌
@NoArgsConstructor                          // 파라미터가 없는 기본 생성자 생성
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")           // cart테이블의 cart_id를 참조
    @JsonIgnore // 이 필드를 JSON 직렬화 과정에서 무시합니다.
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "item_id")           // items테이블의 item_id를 참조
    private Items items;

    private int quantity;                   // 상품 수량

    public static CartItemDTO toDTO(CartItem cartItem){
        return CartItemDTO.builder()
                .id(cartItem.getId())
                .cartId(cartItem.getCart() != null ? cartItem.getCart().getId() : null)
                .itemsDTO(Items.toDTO(cartItem.getItems()))
                .build();
    }

}
