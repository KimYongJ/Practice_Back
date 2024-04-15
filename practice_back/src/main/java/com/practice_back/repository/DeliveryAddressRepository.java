package com.practice_back.repository;


import com.practice_back.entity.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository //인터페이스가 저장소(Repository) 역할을 하며, 스프링에 의해 관리되는 빈임을 나타냄
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
    List<DeliveryAddress> findByMemberId(String Id);
    Optional<DeliveryAddress> findByDeliveryAddressIdAndMemberId(Long deliveryAddressId, String id);
}
