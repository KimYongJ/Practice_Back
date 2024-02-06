package com.practice_back.service;

import com.practice_back.dto.DeliveryAddressDTO;
import org.springframework.http.ResponseEntity;

public interface DeliveryAddressService {

    public ResponseEntity<Object> getAllDeliveryInfo();
    public ResponseEntity<Object> insertDeliveryInfo(DeliveryAddressDTO deliveryAddressDTO);
    public ResponseEntity<Object> patchDeliveryInfo(DeliveryAddressDTO deliveryAddressDTO);
    public ResponseEntity<Object> deleteDeliveryInfo(Long deliveryAddressId);
    public ResponseEntity<Object> patchDeliveryPrimary( DeliveryAddressDTO deliveryAddressDTO);
}
