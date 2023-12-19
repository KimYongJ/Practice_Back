package com.practice_back.repository;

import com.practice_back.entity.LottoNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LottoNumberRepository extends JpaRepository<LottoNumber, Long> {
    public List<LottoNumber> findAll();
}
