package com.practice_back.service;
import com.practice_back.dto.ItemsDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface ItemsService {

    public List<ItemsDTO> getItems();
    public List<ItemsDTO> getItemsByTitle(String category);
    public List<ItemsDTO> getItemsByItemId(Long itemId);
    public List<ItemsDTO> getItemsByCatagory(String category);
    public List<ItemsDTO> getItemsByPrice(String category, Long  startprice, Long endprice);
}
