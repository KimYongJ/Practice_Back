package com.practice_back.api;

import com.practice_back.dto.ItemsDTO;
import com.practice_back.service.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor // 클래스 내에 final 키워드가 붙거나 @NonNull 어노테이션이 붙은 필드들을 인자로 하는 생성자를 자동으로 생성
@RequestMapping(value = "/api/user/items")
public class ItemsController {
    private final ItemsService itemsService;

    /**
     * 조건별 아이템을 조회
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
        if (sortField != null && sortDir != null)// 정렬 조건이 있는 경우
        {
            sortedPageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.fromString(sortDir), sortField));
        } else
        {
            sortedPageable = pageable;// 정렬 조건이 없는 경우 기본 Pageable 사용
        }

        return itemsService.getItems(itemId, category, itemTitle, startPrice, endPrice, sortedPageable);
    }

    /**
     * 아이템 고유 식별자(Item ID)를 사용하여 해당 아이템을 조회
     *
     * @param item_id 조회할 아이템의 식별자
     * @return 아이템 조회 결과를 담은 ResponseEntity
     */
    @GetMapping("/{item_id}")
    public ResponseEntity<Object> productPage(@PathVariable Long item_id)
    {
        return itemsService.getItemsByItemId(item_id);
    }

    /**
     * 관리자 권한으로 Item 정보 Insert
     *
     * @RequestBody ItemsDTO 저장할 아이템의 상세 정보들
     * @return 저장한 아이템 결과를 담은 ResponseEntity
     */
    @PostMapping
    public ResponseEntity<Object> insertItem(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("price") String price,
            @RequestParam("categoryId") String categoryId
    )throws Exception {
        return itemsService.insertItem(file,title,price,categoryId);
    }

    /**
     * 관리자 권한으로 Item 정보 Update
     *
     * @RequestBody ItemsDTO 변경할 아이템의 상세 정보들
     * @return 변경한 아이템 결과를 담은 ResponseEntity
     */
    @PatchMapping
    public ResponseEntity<Object> updateItem(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("price") String price,
            @RequestParam("itemid") String itemid,
            @RequestParam("imgurl") String imgurl
    )throws Exception{
        return itemsService.updateItem(file, title, price, itemid, imgurl);
    }
    /**
     * 관리자 권한으로 Item 정보 삭제
     *
     * @RequestBody ItemsDTO 삭제할 아이템정보
     * @return 삭제한 아이템 결과를 담은 ResponseEntity
     */
    @DeleteMapping("/{item_id}")
    public ResponseEntity<Object> deleteItem(@PathVariable Long item_id){
        return itemsService.deleteItem(item_id);
    }

}
