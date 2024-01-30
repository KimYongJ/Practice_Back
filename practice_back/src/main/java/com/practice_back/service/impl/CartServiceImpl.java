package com.practice_back.service.impl;

import com.practice_back.dto.CartDTO;
import com.practice_back.dto.CartItemDTO;
import com.practice_back.entity.Cart;
import com.practice_back.entity.CartItem;
import com.practice_back.entity.Items;
import com.practice_back.repository.CartItemRepository;
import com.practice_back.repository.CartRepository;
import com.practice_back.repository.ItemsRepository;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.practice_back.jwt.TokenProvider.getCurrentMemberInfo;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl {

    private final CartRepository cartRepository;
    private final ItemsRepository itemsRepository;
    private final CartItemRepository cartItemRepository;
    public ResponseEntity<Object> getCartByEmail(){
        String email = getCurrentMemberInfo();
        CartDTO cartDTO = cartRepository.findByMemberEmail(email).map(Cart::toDTO)
                .orElseThrow(()-> new UsernameNotFoundException(email + " 을 DB에서 찾을 수 없습니다"));
        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK,"성공", cartDTO ));
    }

    public ResponseEntity<Object> insertCartItem(Integer quantity, Long itemId){
        String email = getCurrentMemberInfo();
        // Member의 email을 기준으로 Cart 찾기
        Cart cart = cartRepository.findByMemberEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보가 잘못되었습니다."));


        Items item = itemsRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("상품 정보가 없습니다."));
        // 상품이 이미 담겼는지 체크
        boolean bool = cartItemRepository.existsByCartIdAndItemsItemId(cart.getId(), item.getItemId());
        if(bool){
            return ResponseEntity.badRequest()
                    .body(new Message(ErrorType.DUPLICATE_ITEM,"이미 카트에 추가된 상품 입니다.",null ));
        }


        cart.getCartItems().add(new CartItem(null, cart, item, quantity));

        Cart updatedCart = cartRepository.save(cart); // 업데이트된 Cart 저장

        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK,"카트에 추가하였습니다.", Cart.toDTO(updatedCart) ));
    }
    public ResponseEntity<Object> updateCartItem(Integer quantity, Long itemId) {
        String email = getCurrentMemberInfo();

        Cart cart = cartRepository.findByMemberEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보가 잘못되었습니다."));

        // 업데이트 부분은 jpa로 구현시 삭제 후 다시 넣어야 하여 성능상 JPQL로 작성
        int updateRow = cartItemRepository.updateQuantityByCartIdAndItemId(quantity, cart.getId(), itemId);

        boolean bool = updateRow > 0;

        ErrorType error = bool ? ErrorType.OK : ErrorType.BAD_REQUEST;

        return ResponseEntity.ok()
                .body(new Message(error
                        ,error.getErrStr(),null));
    }

    public ResponseEntity<Object> deleteCartItem(Long itemId) {
        String email = getCurrentMemberInfo();

        Cart cart = cartRepository.findByMemberEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("사용자 정보가 잘못되었습니다."));

        boolean bool = cart.getCartItems()
                .removeIf(item -> item.getItems().getItemId().equals(itemId));

        ErrorType error = bool ? ErrorType.OK : ErrorType.BAD_REQUEST;

        return ResponseEntity.ok()
                .body(new Message(error
                        ,error.getErrStr(),null));
    }


}
