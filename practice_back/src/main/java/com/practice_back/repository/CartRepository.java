package com.practice_back.repository;

import com.practice_back.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByMemberId(String Id);
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.cart.member.id = :id")
    long countItemsByMemberId(@Param("id") String id);
}
