package com.practice_back.api;


import com.practice_back.dto.CartDTO;
import com.practice_back.dto.CartRequestDTO;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import com.practice_back.service.impl.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController // @Controller와 @ResponseBody 어노테이션의 기능 결합( 메서드의 반환 값에만 영향을 미침 )
@RequestMapping("/api/user/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceImpl cartServiceImpl;

    /**
     * 모든 카트 아이템 가져오기
     *
     * @return ResponseEntity<Object>
     * */
    @GetMapping
    public ResponseEntity<Object> getCart(){
        return cartServiceImpl.getCartByEmail();
    }
    /**
     * 카트에 담긴 아이템의 갯수 가져오기
     *
     * @return ResponseEntity<Object>
     * */
    @GetMapping("/countitems")
    public ResponseEntity<Object> countCartItems(){return cartServiceImpl.countCartItems();}
    /**
     * 카트에 새로운 상품 추가하기
     *
     * @return ResponseEntity<Object>
     * */
    @PostMapping
    public ResponseEntity<Object> insertCartItem(@Valid @RequestBody CartRequestDTO cartRequestDTO) {// @RequestBody 어노테이션은 요청 본문의 데이터를 자바 객체로 자동 변환함
        return cartServiceImpl.insertCartItem(cartRequestDTO.getQuantity(), cartRequestDTO.getItemId());
    }
    /**
     * 카트에 담긴 아이템의 구매 수량 변경하기
     *
     * @return ResponseEntity<Object>
     * */
    @PatchMapping // 수량만 업데이트 하는 것이기 때문에 Patch맵핑 사용, 전체 교체는 Put맵핑
    public ResponseEntity<Object> patchCartItem(@Valid @RequestBody CartRequestDTO cartRequestDTO){
        return cartServiceImpl.updateCartItem(cartRequestDTO.getQuantity(), cartRequestDTO.getItemId());
    }

    /**
     * 카트에 담긴 아이템 삭제
     *
     * @return ResponseEntity<Object>
     * */
    @DeleteMapping
    public ResponseEntity<Object> deleteCartItem(@RequestBody CartRequestDTO cartRequestDTO){
        return cartServiceImpl.deleteCartItem(cartRequestDTO.getItemId());
    }



}
