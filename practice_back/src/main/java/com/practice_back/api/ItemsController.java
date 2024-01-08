package com.practice_back.api;

import com.practice_back.service.impl.ItemsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user/items")
@RequiredArgsConstructor
public class ItemsController {
    @Autowired
    ItemsServiceImpl itemsServiceImpl;

    /**
     * 카테고리별 아이템 조회
     *
     * @return ItemsDTO
     */
    @GetMapping(value = "/{category}")
    public ResponseEntity<?> getItemsByCatagory(@PathVariable(name = "category") String category)
    {
        return itemsServiceImpl.getItemsByCatagory(category);
    }


}
