package com.practice_back.service.impl;

import com.practice_back.dto.ItemsDTO;
import com.practice_back.entity.Items;
import com.practice_back.repository.ItemsRepository;
import com.practice_back.service.ItemsService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public List<ItemsDTO> getItems()
    {
        List<ItemsDTO> list = itemsRepository.findAll().stream().map(Items::toItemsDTO)
                .collect(Collectors.toList());
        return list;
    }

    // 아이템 PK값으로 조회하는 함수
    public List<ItemsDTO> getItemsByItemId(Long itemId){
        List<ItemsDTO> list = itemsRepository.findByItemId(itemId).stream().map(Items::toItemsDTO)
                .collect(Collectors.toList());
        return list;
    }

    // 아이템 이름으로 조회하는 함수
    public List<ItemsDTO> findAllByItemTitleLike(String itemTitle){
        List<ItemsDTO> list = itemsRepository.findAllByItemTitleLike( "%" + itemTitle + "%").stream().map(Items::toItemsDTO)
                .collect(Collectors.toList());
        return list;
    }
    // 각 카테고리별로 모든 품목을 조회하는 함수
    @Override
    public List<ItemsDTO> getItemsByCatagory(String category)
    {
        List<ItemsDTO> list = itemsRepository.findAllByCategory(category).stream().map(Items::toItemsDTO)
                .collect(Collectors.toList());
        return list;
    }

    // 카테고리별, 아이템 가격으로 해당하는 품목을 조회해오는 함수
    @Override
    public List<ItemsDTO> getItemsByPrice(String category, Long  startprice, Long endprice){
        List<ItemsDTO> list = itemsRepository.findAllByItemPrice(category, startprice, endprice).stream().map(Items::toItemsDTO)
                .collect((Collectors.toList()));
        return list;
    }
}
