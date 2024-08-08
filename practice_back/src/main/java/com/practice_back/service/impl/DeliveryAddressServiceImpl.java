package com.practice_back.service.impl;
import com.practice_back.dto.DeliveryAddressDTO;
import com.practice_back.entity.DeliveryAddress;
import com.practice_back.entity.Member;
import com.practice_back.repository.DeliveryAddressRepository;
import com.practice_back.repository.MemberRepository;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import com.practice_back.service.DeliveryAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import static com.practice_back.jwt.TokenProvider.getCurrentMemberInfo;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryAddressServiceImpl implements DeliveryAddressService {
    private final DeliveryAddressRepository delRepo;
    private final MemberRepository memberRepo;
    @Override
    public ResponseEntity<Object> getAllDeliveryInfo() {
        String Id = getCurrentMemberInfo();
        List<DeliveryAddressDTO> listDTO = delRepo.findByMemberId(Id).stream()
                .map(DeliveryAddress::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK,"성공", listDTO ));
    }
    @Override
    public ResponseEntity<Object> insertDeliveryInfo(DeliveryAddressDTO deliveryAddressDTO) {
        String ID = getCurrentMemberInfo();
        Member member = memberRepo.findById(ID)
                .orElseThrow(()-> new UsernameNotFoundException(ID + " 을 DB에서 찾을 수 없습니다"));

        int size = member.getDeliveryAddresses().size();

        if(size > 4){
            return ResponseEntity.badRequest()
                    .body(new Message(ErrorType.ADDRESS_LIMIT_EXCEEDED,"주소 목록이 최대 한도에 도달했습니다. 더 이상 추가할 수 없습니다.", null ));
        }

        // DTO로부터 DeliveryAddress 엔티티
        DeliveryAddress deliveryAddress = DeliveryAddress.builder()
                .recipient(deliveryAddressDTO.getRecipient())
                .contactNumber(deliveryAddressDTO.getContactNumber())
                .postalCode(deliveryAddressDTO.getPostalCode())
                .address(deliveryAddressDTO.getAddress())
                .addressDetail(deliveryAddressDTO.getAddressDetail())
                .isPrimary(false)
                // Member와 DeliveryAddress엔티티에서 주인이 DeliveryAddress엔티티 이기 때문에 단순히 member를 셋팅하고 추후 DeliveryAddress엔티티를 저장만해도 관계정립 후 저장된다.
                .member(member)
                .build();

        DeliveryAddressDTO delDTO = DeliveryAddress.toDTO( delRepo.save(deliveryAddress) );

        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK,"저장했습니다.", delDTO ));
    }
    @Override
    public ResponseEntity<Object> patchDeliveryInfo(DeliveryAddressDTO deliveryAddressDTO) {
        String Id = getCurrentMemberInfo();
        List<DeliveryAddress> addresses = delRepo.findByMemberId(Id);

        // 전달된 정보에 대해 기본 배송지 설정
        DeliveryAddress deliveryAddress = addresses.stream()
                .filter(address -> address.getDeliveryAddressId().equals(deliveryAddressDTO.getDeliveryAddressId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("배송지를 찾을 수 없습니다."));

        deliveryAddress.update(deliveryAddressDTO);

        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK,"수정되었습니다.", deliveryAddressDTO ));
    }

    @Override
    public ResponseEntity<Object> patchDeliveryPrimary(DeliveryAddressDTO deliveryAddressDTO) {
        String Id = getCurrentMemberInfo();
        List<DeliveryAddress> addresses = delRepo.findByMemberId(Id);

        // 기존 배송지 비활성화
        addresses.stream()
                .filter(DeliveryAddress::getIsPrimary)
                .forEach(DeliveryAddress::makeNotPrimary);

        // 전달된 정보에 대해 기본 배송지 설정
        DeliveryAddress newPrimary = addresses.stream()
                .filter(address -> address.getDeliveryAddressId().equals(deliveryAddressDTO.getDeliveryAddressId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("배송지를 찾을 수 없습니다."));

        newPrimary.makePrimary();

        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK,"수정되었습니다.", deliveryAddressDTO ));
    }

    @Override
    public ResponseEntity<Object> deleteDeliveryInfo(Long deliveryAddressId) {
        String Id = getCurrentMemberInfo();

        List<DeliveryAddress> addresses = delRepo.findByMemberId(Id);

        Iterator<DeliveryAddress> ite = addresses.iterator();
        while(ite.hasNext()){
            DeliveryAddress ds = ite.next();
            if(deliveryAddressId == ds.getDeliveryAddressId()){
                delRepo.delete(ds);
                break;
            }
        }

        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK,"삭제되었습니다.", null ));
    }


}
