package com.practice_back.repository;

import com.practice_back.entity.Items;
import com.practice_back.entity.LottoNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {
//    public List<Items> findAllByCategory(String category);

    public List<Items> findByItemId(Long itemId);

    Page<Items> findByItemTitleContaining(String title, Pageable pageable);


//    @Query("SELECT * FROM Items WHERE category = :category AND item_price BETWEEN :startPrice AND :endPrice")
    @Query(value = "select * from Items where category = :category and item_price >= :startPrice and item_price <= :endPrice", nativeQuery = true)
    Page<Items> findByCategoryAndItemPriceRange(@Param("category") String category,
                                            @Param("startPrice") Long startPrice,
                                            @Param("endPrice") Long endPrice,
                                            Pageable pageable);
}
