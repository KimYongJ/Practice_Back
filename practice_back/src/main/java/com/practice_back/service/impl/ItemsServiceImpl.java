package com.practice_back.service.impl;

import com.practice_back.dto.ItemsDTO;
import com.practice_back.entity.Items;
import com.practice_back.repository.ItemsRepository;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import com.practice_back.service.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ItemsServiceImpl implements ItemsService {


    private final ItemsRepository itemsRepository;

    // 모든 아이템 조회 함수
    @Override
    public ResponseEntity<Object> getItems(Long itemId, List<Long> category, String itemTitle, Long startPrice, Long endPrice, Pageable pageable)
    {
        Page<ItemsDTO> items = itemsRepository.findItemsByDynamicCondition(itemId, category, itemTitle, startPrice, endPrice, pageable)
                .map(Items::toDTO);
        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK, "조회 성공", items));
    }

    // 아이템 PK값으로 조회하는 함수
    @Override
    public ResponseEntity<Object> getItemsByItemId(Long itemId){
        ItemsDTO itemDTO = Items.toDTO(itemsRepository.findByItemId(itemId));
        return ResponseEntity.ok(itemDTO);
    }

}
