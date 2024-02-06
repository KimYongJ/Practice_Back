package com.practice_back.api;

import com.practice_back.dto.DeliveryAddressDTO;
import com.practice_back.service.impl.DeliveryAddressServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController // @Controller와 @ResponseBody 어노테이션의 기능 결합( 메서드의 반환 값에만 영향을 미침 )
@RequestMapping("/api/user/deliveryinfo")
@RequiredArgsConstructor
public class DeliveryAddressController {

    private final DeliveryAddressServiceImpl deliveryAddressServiceImpl;

    /**
    * 모든 배송 정보 받아오기
    *
    * @return ResponseEntity<Object>
    * */
    @GetMapping
    public ResponseEntity<Object> getAllDeliveryInfo(){
        return deliveryAddressServiceImpl.getAllDeliveryInfo();
    }

    /**
    * 배송 정보 신규 저장
    *
    * @return ResponseEntity<Object>
    * */
    @PostMapping
    public ResponseEntity<Object> insertDeliveryInfo(@Valid @RequestBody DeliveryAddressDTO deliveryAddressDTO){// @RequestBody 어노테이션은 요청 본문의 데이터를 자바 객체로 자동 변환함
        return deliveryAddressServiceImpl.insertDeliveryInfo(deliveryAddressDTO);
    }

    /**
    * 배송 정보 변경
    *
    * @return ResponseEntity<Object>
    * */
    @PatchMapping
    public ResponseEntity<Object> patchDeliveryInfo(@Valid @RequestBody DeliveryAddressDTO deliveryAddressDTO){
        return deliveryAddressServiceImpl.patchDeliveryInfo(deliveryAddressDTO);
    }


    /**
    * 기본 배송지 설정 변경
    *
    * @return ResponseEntity<Object>
    **/
    @PatchMapping("/updateprimary")
    public ResponseEntity<Object> patchDeliveryPrimary(@RequestBody DeliveryAddressDTO deliveryAddressDTO){
        return deliveryAddressServiceImpl.patchDeliveryPrimary(deliveryAddressDTO);
    }

    /**
    * 주소 정보 삭제
    *
    * @return ResponseEntity<Object>
    * */
    @DeleteMapping("/{deliveryAddressId}")
    public ResponseEntity<Object> deleteDeliveryInfo(@PathVariable Long deliveryAddressId){
        return deliveryAddressServiceImpl.deleteDeliveryInfo(deliveryAddressId);
    }


}
