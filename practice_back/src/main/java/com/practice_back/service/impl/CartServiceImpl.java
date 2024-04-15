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
import com.practice_back.service.CartService;
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
public class CartServiceImpl implements CartService {

    private final CartRepository        cartRepository;
    private final ItemsRepository       itemsRepository;
    private final CartItemRepository    cartItemRepository;

    @Override
    public ResponseEntity<Object> getCartByEmail(){
        String Id    = getCurrentMemberInfo();
        CartDTO cartDTO = cartRepository.findByMemberId(Id).map(Cart::toDTO)
                             .orElseThrow(()-> new UsernameNotFoundException(Id + " 을 DB에서 찾을 수 없습니다"));
        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK,"성공", cartDTO ));
    }
    @Override
    public ResponseEntity<Object> countCartItems() {
        String Id    = getCurrentMemberInfo();
        long cnt        = cartRepository.countItemsByMemberId(Id);
        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK,"성공", cnt ));
    }
    @Override
    public ResponseEntity<Object> insertCartItem(Integer quantity, Long itemId){
        String Id    = getCurrentMemberInfo();
        Cart cart       = cartRepository.findByMemberId(Id)
                            .orElseThrow(() -> new EntityNotFoundException("사용자 정보가 잘못되었습니다."));
        // 상품이 이미 담겼는지 체크
        boolean bool = cartItemRepository.existsByCartIdAndItemsItemId(cart.getId(), itemId);
        if(bool){
            return ResponseEntity.badRequest()
                    .body(new Message(ErrorType.DUPLICATE_ITEM,"이미 카트에 추가된 상품 입니다.",null ));
        }

        List<CartItem> cartItemList = cart.getCartItems();

        Items item = itemsRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("상품 정보가 없습니다."));


        long totalPrice = quantity * item.getItemPrice(); // 수량과 가격을 곱해 총 금액을 구함

        CartItem cartitem = CartItem.builder()
                                .cart(cart)
                                .items(item)
                                .quantity(quantity)
                                .totalPrice(totalPrice)
                                .build();

        cartItemList.add(cartitem);

        cartRepository.save(cart); // 업데이트된 Cart 저장

        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK,"카트에 추가하였습니다.", null ));
    }

    @Override
    public ResponseEntity<Object> updateCartItem(Integer quantity, Long itemId) {
        String Id = getCurrentMemberInfo();

        Cart cart = cartRepository.findByMemberId(Id)
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보가 잘못되었습니다."));

        Items item = itemsRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("상품 정보가 없습니다."));

        long totalPrice = quantity * item.getItemPrice(); // 수량과 가격을 곱해 총 금액을 구함

        // 업데이트 부분은 jpa로 구현시 삭제 후 다시 넣어야 하여 성능상 JPQL로 작성
        int updateRow = cartItemRepository.updateTotalPriceQuantityByCartIdAndItemId(totalPrice, quantity, cart.getId(), itemId);

        boolean bool = updateRow > 0;

        ErrorType error = bool ? ErrorType.OK : ErrorType.BAD_REQUEST;

        return ResponseEntity.ok()
                .body(new Message(error
                        ,error.getErrStr(),null));
    }

    @Override
    public ResponseEntity<Object> deleteCartItem(Long itemId) {
        String Id = getCurrentMemberInfo();

        Cart cart = cartRepository.findByMemberId(Id)
                .orElseThrow(()-> new EntityNotFoundException("사용자 정보가 잘못되었습니다."));

        boolean bool = cart.getCartItems()
                .removeIf(item -> item.getItems().getItemId().equals(itemId));

        ErrorType error = bool ? ErrorType.OK : ErrorType.BAD_REQUEST;

        return ResponseEntity.ok()
                .body(new Message(error,error.getErrStr(),null));
    }



}
