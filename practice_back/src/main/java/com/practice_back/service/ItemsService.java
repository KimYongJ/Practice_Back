package com.practice_back.service;
import com.practice_back.dto.ItemsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;


public interface ItemsService {

    public Page<ItemsDTO> getItems(Long itemId, List<Long> category, String itemTitle, Long startPrice, Long endPrice, Pageable pageable);
    public List<ItemsDTO> getItemsByItemId(Long itemId);
}
