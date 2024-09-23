package com.practice_back.dto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class CartItemDTOTest {
    @DisplayName("빌더를 통해 CartItemDTO 객체를 생성할 수 있다.")
    @Test
    void createCartItemDTO(){
        // Given
        Long id                    = 100L;
        int quantity            = 10;
        long totalPrice         = 1000L;
        Long cartId             = 99L;
        Long itemId             = 10L;
        String imgUrl           = "IMG_URL";
        String itemTitle        = "아이템이름";
        Long itemPrice          = 1000L;
        Long categoryId         = 11L;
        String categoryTitle    = "카테고리명";
        ItemsDTO itemsDTO = createItemsDTO(itemId,imgUrl,itemTitle,itemPrice,categoryId,categoryTitle);

        // When
        CartItemDTO cartItemDTO = CartItemDTO.builder()
                .id(id)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .cartId(cartId)
                .itemsDTO(itemsDTO)
                .build();

        // Then
        assertEquals(cartItemDTO.getId(), id);
        assertEquals(cartItemDTO.getQuantity(), quantity);
        assertEquals(cartItemDTO.getTotalPrice(), totalPrice);
        assertEquals(cartItemDTO.getCartId(), cartId);
        assertThat(cartItemDTO.getItemsDTO())
                .satisfies(itemDTO ->{
                    assertEquals(itemDTO.getItemId(), itemId);
                    assertEquals(itemDTO.getImgUrl(), imgUrl);
                    assertEquals(itemDTO.getItemPrice(), itemPrice);
                    assertEquals(itemDTO.getItemTitle(), itemTitle);
                    assertEquals(itemDTO.getCategoryDTO().getCategoryId(), categoryId);
                    assertEquals(itemDTO.getCategoryDTO().getCategoryTitle(), categoryTitle);
                });
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