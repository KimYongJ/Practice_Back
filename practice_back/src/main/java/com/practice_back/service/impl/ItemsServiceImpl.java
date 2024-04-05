package com.practice_back.service.impl;

import com.practice_back.dto.CategoryDTO;
import com.practice_back.dto.ItemsDTO;
import com.practice_back.entity.Category;
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

    @Override
    public ResponseEntity<Object> getItems(Long itemId, List<Long> category, String itemTitle, Long startPrice, Long endPrice, Pageable pageable)
    {
        Page<ItemsDTO> items = itemsRepository.findItemsByDynamicCondition(itemId, category, itemTitle, startPrice, endPrice, pageable)
                .map(Items::toDTO);
        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK, "조회 성공", items));
    }

    @Override
    public ResponseEntity<Object> getItemsByItemId(Long itemId){
        ItemsDTO itemDTO = Items.toDTO(itemsRepository.findByItemId(itemId));
        return ResponseEntity.ok(itemDTO);
    }

    @Override
    public ResponseEntity<Object> insertItem(ItemsDTO itemsDTO){
        Category category = CategoryDTO.toEntity(itemsDTO.getCategoryDTO());
        Items items = Items.builder()
                .itemTitle(itemsDTO.getItemTitle())
                .imgUrl(itemsDTO.getImgUrl())
                .itemPrice(itemsDTO.getItemPrice())
                .category(category)
                .build();
        itemsDTO = Items.toDTO(itemsRepository.save(items));
        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK,"저장했습니다.", itemsDTO ));
    }

    @Override
    public ResponseEntity<Object> updateItem(ItemsDTO dto){
        ItemsDTO itemsDTO = Items.toDTO(itemsRepository.getById(dto.getItemId()));
        itemsDTO.setItemTitle(dto.getItemTitle());
        itemsDTO.setItemPrice(dto.getItemPrice());
        itemsDTO.setImgUrl(dto.getImgUrl());
        itemsDTO = Items.toDTO(itemsRepository.save(ItemsDTO.toEntity(itemsDTO)));

        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK,"저장했습니다.", itemsDTO ));
    }

    @Override
    public ResponseEntity<Object> deleteItem(Long itemId){
        itemsRepository.deleteById(itemId);
        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK, "삭제했습니다.", 1));
    }
}
