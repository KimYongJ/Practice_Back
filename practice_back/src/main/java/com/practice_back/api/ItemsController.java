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
     * 모든 아이템을 조회합니다.
     *
     * @return 아이템 조회 결과를 담은 ResponseEntity
     */
    @GetMapping()
    public ResponseEntity<Object> getItems()
    {
        // ResponseEntity를 사용하여 응답을 구성
        return ResponseEntity.ok()
                .header(null)
                .body(itemsServiceImpl.getItems());
    }

    /**
     * 아이템 이름을 LIKE 연산자를 사용하여 조회합니다.
     *
     * @param itemTitle 조회할 아이템의 이름 또는 부분 이름
     * @return 아이템 조회 결과를 담은 ResponseEntity
     */
    @GetMapping("/searchProduct")
    public ResponseEntity<Object> getItemsByTitle(@RequestParam String itemTitle)
    {
        // ResponseEntity를 사용하여 응답을 구성
        return ResponseEntity.ok()
                .header(null)
                .body(itemsServiceImpl.findAllByItemTitleLike(itemTitle));
    }


    /**
     * 아이템 식별자(Item ID)를 사용하여 해당 아이템을 조회합니다.
     *
     * @param itemId 조회할 아이템의 식별자
     * @return 아이템 조회 결과를 담은 ResponseEntity
     */
    @GetMapping("/productPage")
    public ResponseEntity<Object> productPage(@RequestParam Long itemId)
    {
        // ResponseEntity를 사용하여 응답을 구성
        return ResponseEntity.ok()
                .header(null)
                .body(itemsServiceImpl.getItemsByItemId(itemId));
    }

    /**
     * 카테고리별로 지정된 금액 범위에 속하는 아이템을 조회합니다.
     *
     * @param category   조회할 아이템의 카테고리
     * @param startPrice 조회할 아이템의 최소 금액
     * @param endPrice   조회할 아이템의 최대 금액
     * @return 아이템 조회 결과를 담은 ResponseEntity
     */
    @GetMapping(value = "/category") // 다양한 카테고리에 대한 아이템 조회를 지원하기 위해 카테고리를 변수로 받습니다.
    public ResponseEntity<Object> getItemsByPrice(@RequestParam String category, @RequestParam Long startPrice, @RequestParam Long endPrice)
    {
        // ResponseEntity를 사용하여 응답을 구성
        return ResponseEntity.ok()
                .header(null)
                .body(itemsServiceImpl.getItemsByPrice( category, startPrice, endPrice ));
    }

}
