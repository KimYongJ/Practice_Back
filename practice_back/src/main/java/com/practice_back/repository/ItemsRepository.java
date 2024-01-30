package com.practice_back.repository;

import com.practice_back.entity.Items;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //인터페이스가 저장소(Repository) 역할을 하며, 스프링에 의해 관리되는 빈임을 나타냄
public interface ItemsRepository extends JpaRepository<Items, Long>  {


    // @Query : JPQL(Java Persistence Query Language) 쿼리를 정의하는 어노테이션
    // value: JPQL 쿼리 문자열을 정의
    // nativeQuery = false 기본값, false는 이 쿼리가 네이티브 SQL이 아닌 JPQL임을 나타냄
    // @Query 어노테이션 뒤에  nativeQuery = true를 사용하지 않을 경우 jpql문법만을 사용해 쿼리를 만들어야 한다.
    // jpql문법 : 기타 특징으로 select할 때 엔티티 이름( as i )을 사용해야함, 필드 작성시 실제 테이블명이 아니라 엔티티 변수를 써야함
    @Query(value = "select i from Items i "
            +" left join i.category c " // c는 category엔티티의 별칭이다. 엔티티 내에 정의된관계(@ManyToOne)을 통해 해당필드가 어떤 엔티티와 조인되는지 알고 있음으로 c만 적어도됨
            +" where 1=1"
            +" and (:itemId IS NULL OR i.itemId = :itemId)"
            +" and c.categoryId IN (:categories)" // categories는 컬랙션이기 때문에 null일 경우 에러가 뜬다. ex) :categories IS NULL OR 문법은 사용할 수 없다.
            +" and (:startPrice IS NULL OR i.itemPrice >= :startPrice)"
            +" and (:endPrice IS NULL OR i.itemPrice <= :endPrice)"
            +" and (:itemTitle IS NULL OR i.itemTitle LIKE CONCAT('%', :itemTitle, '%'))"
            )
    Page<Items> findItemsByDynamicCondition(@Param("itemId")Long itemId,
                                            @Param("categories") List<Long> categories,
                                            @Param("itemTitle")String itemTitle,
                                            @Param("startPrice")Long startPrice,
                                            @Param("endPrice")Long endPrice,
                                            Pageable pageable);


    public Items findByItemId(Long itemId);

}
