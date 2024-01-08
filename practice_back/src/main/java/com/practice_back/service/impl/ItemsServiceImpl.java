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

    @Override
    public ResponseEntity<?> getItemsByCatagory(String category)
    {
        List<ItemsDTO> list = itemsRepository.findAllByCategory(category).stream().map(Items::toItemsDTO)
                .collect(Collectors.toList());

        return null;
    }



}
