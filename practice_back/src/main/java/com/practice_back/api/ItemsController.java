package com.practice_back.api;

import com.practice_back.service.impl.ItemsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user/items")
@RequiredArgsConstructor
public class ItemsController {
    @Autowired
    ItemsServiceImpl itemsServiceImpl;

    /**
     * 카테고리별 금액에 따른 아이템 조회
     *
     * @return ResponseEntity<Object>
     */
    @GetMapping(value = "/{category}")
    public ResponseEntity<Object> getItemsUsePrice_0(@PathVariable("category") String category, @RequestParam Long startPrice, @RequestParam Long endPrice)
    {
        return ResponseEntity.ok()
                .header(null)
                .body(itemsServiceImpl.getItemsByPrice( category, startPrice, endPrice ));
    }




}
