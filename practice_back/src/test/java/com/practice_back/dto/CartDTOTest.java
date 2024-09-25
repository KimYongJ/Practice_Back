package com.practice_back.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class CartDTOTest {
    @DisplayName("빌더를 통해 CartDTO객체를 생성할 수 있다.")
    @Test
    void createCartDTO(){
        // Given
        Long id                 = 100L;
        int quantity            = 10;
        long totalPrice         = 1000L;
        Long cartId             = 99L;
        Long itemId             = 10L;
        String imgUrl           = "IMG_URL";
        String itemTitle        = "아이템이름";
        Long itemPrice          = 1000L;
        Long categoryId         = 11L;
        String categoryTitle    = "카테고리명";
        CartItemDTO cartItemDTO1 = createCartItemDTO(id, quantity, totalPrice, cartId, itemId, imgUrl, itemTitle, itemPrice, categoryId, categoryTitle);
        CartItemDTO cartItemDTO2 = createCartItemDTO(id, quantity, totalPrice, cartId, itemId, imgUrl, itemTitle, itemPrice, categoryId, categoryTitle);
        List<CartItemDTO> cartItemsDTO = List.of(cartItemDTO1, cartItemDTO2);

        // When
        CartDTO cartDTO = CartDTO.builder()
                .cartItemsDTO(cartItemsDTO)
                .build();

        // Then
        assertThat(cartDTO)
                .satisfies(dto->{
                    List<CartItemDTO> ctItemsDTO = dto.getCartItemsDTO();
                    assertEquals(ctItemsDTO.get(0).getId(), id);
                    assertEquals(ctItemsDTO.get(0).getQuantity(), quantity);
                    assertEquals(ctItemsDTO.get(0).getTotalPrice(), totalPrice);
                    assertEquals(ctItemsDTO.get(0).getCartId(), cartId);
                    assertThat(ctItemsDTO.get(0).getItemsDTO())
                            .satisfies(itemDTO ->{
                                assertEquals(itemDTO.getItemId(), itemId);
                                assertEquals(itemDTO.getImgUrl(), imgUrl);
                                assertEquals(itemDTO.getItemPrice(), itemPrice);
                                assertEquals(itemDTO.getItemTitle(), itemTitle);
                                assertEquals(itemDTO.getCategoryDTO().getCategoryId(), categoryId);
                                assertEquals(itemDTO.getCategoryDTO().getCategoryTitle(), categoryTitle);
                            });

                    assertEquals(ctItemsDTO.get(1).getId(), id);
                    assertEquals(ctItemsDTO.get(1).getQuantity(), quantity);
                    assertEquals(ctItemsDTO.get(1).getTotalPrice(), totalPrice);
                    assertEquals(ctItemsDTO.get(1).getCartId(), cartId);
                    assertThat(ctItemsDTO.get(1).getItemsDTO())
                            .satisfies(itemDTO ->{
                                assertEquals(itemDTO.getItemId(), itemId);
                                assertEquals(itemDTO.getImgUrl(), imgUrl);
                                assertEquals(itemDTO.getItemPrice(), itemPrice);
                                assertEquals(itemDTO.getItemTitle(), itemTitle);
                                assertEquals(itemDTO.getCategoryDTO().getCategoryId(), categoryId);
                                assertEquals(itemDTO.getCategoryDTO().getCategoryTitle(), categoryTitle);
                            });
                });
    }

    public CartItemDTO createCartItemDTO(Long id, int quantity, long totalPrice, Long cartId, Long itemId, String imgUrl, String itemTitle, Long itemPrice, Long categoryId, String categoryTitle){
        ItemsDTO itemsDTO = createItemsDTO(itemId,imgUrl,itemTitle,itemPrice,categoryId,categoryTitle);

        return CartItemDTO.builder()
                .id(id)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .cartId(cartId)
                .itemsDTO(itemsDTO)
                .build();

    }
    public ItemsDTO createItemsDTO(Long itemId,String imgUrl,String itemTitle,Long itemPrice,Long categoryId,String categoryTitle)
    {
        CategoryDTO categoryDTO = CategoryDTO
                .builder()
                .categoryId(categoryId)
                .categoryTitle(categoryTitle)
                .build();

        return ItemsDTO.builder()
                .itemId(itemId)
                .imgUrl(imgUrl)
                .itemTitle(itemTitle)
                .itemPrice(itemPrice)
                .categoryDTO(categoryDTO)
                .build();
    }
}