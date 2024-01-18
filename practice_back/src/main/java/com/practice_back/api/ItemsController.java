package com.practice_back.api;

import com.practice_back.dto.ItemsDTO;
import com.practice_back.response.Message;
import com.practice_back.response.StatusEnum;
import com.practice_back.service.impl.ItemsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping(value = "/api/user/items")
public class ItemsController {
    @Autowired
    ItemsServiceImpl itemsServiceImpl;

    /**
     * 조건별 아이템을 조회합니다.
     * @param itemId 아이템 고유번호(하나의 상품 조회시 사용)
     * @param itemTitle 품목명( 아이템 검색시 사용 )
     * @param category 카테고리
     * @param startPrice 카테고리 검색시 최소 가격
     * @param endPrice 카테고리 검색시 최대 가격
     * @param sortField 정렬하려는 필드( itemPrice )
     * @param sortDir 정렬 방향을 의미 'asc' , 'desc'
     * @param pageable 아이템 페이지 번호, 최대 표시할 아이템수 ex) ?page=0&size=5
     * @return 아이템 조회 결과를 담은 ResponseEntity
     */
    @GetMapping()
    public ResponseEntity<Object> getItems(
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) String itemTitle,
            @RequestParam(required = false) List<Long> category,
            @RequestParam(required = false) Long startPrice,
            @RequestParam(required = false) Long endPrice,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDir,
            Pageable pageable)
    {
        Pageable sortedPageable;
        if (sortField != null && sortDir != null) {// 정렬 조건이 있는 경우
            sortedPageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.fromString(sortDir), sortField));
        } else {
            // 정렬 조건이 없는 경우 기본 Pageable 사용
            sortedPageable = pageable;
        }

        Page<ItemsDTO> items =  itemsServiceImpl.getItems(itemId, category, itemTitle, startPrice, endPrice, sortedPageable);
        Message message = new Message();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        message.setMessage("조회 성공");
        message.setStatus(StatusEnum.OK);
        message.setData(items);
        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }
    /**
     * 아이템 고유 식별자(Item ID)를 사용하여 해당 아이템을 조회합니다.
     *
     * @param item_id 조회할 아이템의 식별자
     * @return 아이템 조회 결과를 담은 ResponseEntity
     */
    @GetMapping("/{item_id}")
    public ResponseEntity<Object> productPage(@PathVariable Long item_id)
    {
        List<ItemsDTO> item = itemsServiceImpl.getItemsByItemId(item_id);
        return ResponseEntity.ok(item);
    }

}
