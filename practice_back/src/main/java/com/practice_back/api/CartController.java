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

@RestController // @Controller와 @ResponseBody 어노테이션의 기능 결합
@RequestMapping("/api/user/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceImpl cartServiceImpl;
    @GetMapping()
    public ResponseEntity<Object> getCart(){
        return cartServiceImpl.getCartByEmail();
    }

    @PostMapping()
    public ResponseEntity<Object> insertCartItem(@Valid @RequestBody CartRequestDTO cartRequestDTO) {// @RequestBody 어노테이션은 요청 본문의 데이터를 자바 객체로 자동 변환해줌
        return cartServiceImpl.insertCartItem(cartRequestDTO.getQuantity(), cartRequestDTO.getItemId());
    }

    @PatchMapping() // 수량만 업데이트 하는 것이기 때문에 Patch맵핑 사용, 전체 교체는 Put맵핑
    public ResponseEntity<Object> patchCartItem(@Valid @RequestBody CartRequestDTO cartRequestDTO){
        return cartServiceImpl.updateCartItem(cartRequestDTO.getQuantity(), cartRequestDTO.getItemId());
    }


    @DeleteMapping()
    public ResponseEntity<Object> deleteCartItem(@RequestBody CartRequestDTO cartRequestDTO){
        return cartServiceImpl.deleteCartItem(cartRequestDTO.getItemId());
    }



}
