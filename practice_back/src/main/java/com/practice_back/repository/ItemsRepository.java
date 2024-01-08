package com.practice_back.repository;

import com.practice_back.entity.Items;
import com.practice_back.entity.LottoNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {
    public List<Items> findAllByCategory(String category);
}
