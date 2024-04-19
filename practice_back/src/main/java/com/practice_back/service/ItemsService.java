package com.practice_back.service;

import com.practice_back.dto.ItemsDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface ItemsService {

    public ResponseEntity<Object> getItems(Long itemId, List<Long> category, String itemTitle, Long startPrice, Long endPrice, Pageable pageable);
    public ResponseEntity<Object> getItemsByItemId(Long itemId);
    public ResponseEntity<Object> insertItem(
            MultipartFile file,
            String title,
            String price,
            String categoryId
    )throws IOException;
    public ResponseEntity<Object> updateItem(
            MultipartFile file,
            String title,
            String price,
            String itemid,
            String imgurl
    )throws IOException;
    public ResponseEntity<Object> deleteItem(Long itemId);
}
