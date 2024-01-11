package com.practice_back.repository;

import com.practice_back.entity.Items;
import com.practice_back.entity.LottoNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {
    public List<Items> findAllByCategory(String category);
    public List<Items> findAllByItemTitleLike(String itemTitle);
    public List<Items> findByItemId(Long itemId);
    @Query(value = "select * from Items where category = :category and item_price >= :startprice and item_price <= :endprice", nativeQuery = true)// between 은 성능상 쓰지 않음
    public List<Items> findAllByItemPrice(@Param("category")String category, @Param("startprice")Long startprice, @Param("endprice")Long endprice);


}
