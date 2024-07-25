package com.practice_back.repository;

import com.practice_back.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository  extends JpaRepository<CartItem, Long> {
    boolean existsByCartIdAndItemsItemId(Long cartId, Long ItemId);
    // clearAutomatically : JPQL사용시 영속성 컨텍스트와 실제 DB가 차이가 나기 때문에 테스크코드 작성시 실제 반영된 부분을 못불러와서, 해당 코드를 추가함으로 영속성 컨텍스트를 비워 DB에 접근해 가져오게 함
    @Modifying(clearAutomatically = true) // UPDATE, DELETE, INSERT와 같은 변경 쿼리에 사용되는 어노테이션
    @Query("UPDATE CartItem ci SET ci.totalPrice = :totalPrice , ci.quantity = :quantity WHERE ci.cart.id = :cartId AND ci.items.itemId = :itemId")
    int updateTotalPriceQuantityByCartIdAndItemId(@Param("totalPrice") long totalPrice, @Param("quantity") int quantity, @Param("cartId") Long cartId, @Param("itemId") Long itemId);

}
