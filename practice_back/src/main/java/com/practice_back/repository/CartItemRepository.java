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
    @Modifying // UPDATE, DELETE, INSERT와 같은 변경 쿼리에 사용되는 어노테이션
    @Query("UPDATE CartItem ci SET ci.totalPrice = :totalPrice , ci.quantity = :quantity WHERE ci.cart.id = :cartId AND ci.items.itemId = :itemId")
    int updateTotalPriceQuantityByCartIdAndItemId(@Param("totalPrice") long totalPrice, @Param("quantity") int quantity, @Param("cartId") Long cartId, @Param("itemId") Long itemId);

}
