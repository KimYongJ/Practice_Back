package com.practice_back.repository;

import com.practice_back.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByMemberEmail(String email);
    Cart save(Cart cart);
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.cart.member.email = :email")
    long countItemsByMemberEmail(@Param("email") String email);
}
