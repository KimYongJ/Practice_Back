package com.practice_back.service;

import com.practice_back.dto.ItemsDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface ItemsService {

    public ResponseEntity<Object> getItems(Long itemId, List<Long> category, String itemTitle, Long startPrice, Long endPrice, Pageable pageable);
    public ResponseEntity<Object> getItemsByItemId(Long itemId);
    public ResponseEntity<Object> insertItem(ItemsDTO itemsDTO);
    public ResponseEntity<Object> updateItem(ItemsDTO itemsDTO);
    public ResponseEntity<Object> deleteItem(Long itemId);
}
