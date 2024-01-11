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
     * 모든 Item 조회
     *
     * @return ResponseEntity<Object>
     */
    @GetMapping()
    public ResponseEntity<Object> getItems()
    {
        return ResponseEntity.ok()
                .header(null)
                .body(itemsServiceImpl.getItems());
    }

    /**
     * 아이템 이름으로 조회
     *
     * @return ResponseEntity<Object>
     */
    @GetMapping("/searchProduct")
    public ResponseEntity<Object> getItemsByTitle(@RequestParam String itemTitle)
    {
        return ResponseEntity.ok()
                .header(null)
                .body(itemsServiceImpl.getItemsByTitle(itemTitle));
    }

    /**
     * 아이템 PK로 조회
     *
     * @return ResponseEntity<Object>
     */
    @GetMapping("/productPage")
    public ResponseEntity<Object> productPage(@RequestParam Long itemId)
    {
        return ResponseEntity.ok()
                .header(null)
                .body(itemsServiceImpl.getItemsByItemId(itemId));
    }

    /**
     * 카테고리별 금액에 따른 아이템 조회
     *
     * @return ResponseEntity<Object>
     */
    @GetMapping(value = "/{category}")
    public ResponseEntity<Object> getItemsByPrice(@PathVariable("category") String category, @RequestParam Long startPrice, @RequestParam Long endPrice)
    {
        return ResponseEntity.ok()
                .header(null)
                .body(itemsServiceImpl.getItemsByPrice( category, startPrice, endPrice ));
    }
}
