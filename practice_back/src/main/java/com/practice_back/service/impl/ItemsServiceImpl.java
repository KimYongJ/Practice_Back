package com.practice_back.service.impl;
import com.practice_back.dto.ItemsDTO;
import com.practice_back.entity.Items;
import com.practice_back.repository.ItemsRepository;
import com.practice_back.service.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemsServiceImpl implements ItemsService {

    @Autowired
    ItemsRepository itemsRepository;

    // 모든 아이템 조회 함수
    @Override
    public Page<ItemsDTO> getItems(String category, String itemTitle, Long startPrice, Long endPrice, Pageable pageable)
    {
        if (itemTitle != null) {
            return itemsRepository.findByItemTitleContaining(itemTitle, pageable)
                    .map(Items::toItemsDTO);
        } else if (category != null && startPrice != null && endPrice != null) {
            return itemsRepository.findByCategoryAndItemPriceRange(category, startPrice, endPrice, pageable)
                    .map(Items::toItemsDTO);
        }
        // 기본적으로 모든 아이템을 반환하거나, 다른 기본 동작을 정의할 수 있음
        return itemsRepository.findAll(pageable)
                .map(Items::toItemsDTO);
    }

    // 아이템 PK값으로 조회하는 함수
    @Override
    public List<ItemsDTO> getItemsByItemId(Long itemId){
        List<ItemsDTO> list = itemsRepository.findByItemId(itemId).stream().map(Items::toItemsDTO)
                .collect(Collectors.toList());
        return list;
    }

}
