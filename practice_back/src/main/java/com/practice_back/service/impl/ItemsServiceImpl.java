package com.practice_back.service.impl;

import com.practice_back.dto.ItemsDTO;
import com.practice_back.entity.Category;
import com.practice_back.entity.Items;
import com.practice_back.repository.ItemsRepository;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import com.practice_back.service.CategoryService;
import com.practice_back.service.ItemsService;
import com.practice_back.service.impl.ImageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ItemsServiceImpl implements ItemsService {

    private final ItemsRepository itemsRepository;
    private final ImageServiceImpl imageServiceImple;
    private final CategoryService categoryService;
    @Override
    public ResponseEntity<Object> getItems(Long itemId, List<Long> category, String itemTitle, Long startPrice, Long endPrice, Pageable pageable)
    {
        Page<ItemsDTO> items = itemsRepository.findItemsByDynamicCondition(itemId, category, itemTitle, startPrice, endPrice, pageable)
                .map(Items::toDTO);
        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK, "조회 성공", items));
    }

    @Override
    public ResponseEntity<Object> getItemsByItemId(Long itemId){
        ItemsDTO itemDTO = Items.toDTO(itemsRepository.findByItemId(itemId));
        return ResponseEntity.ok(itemDTO);
    }

    @Override
    public ResponseEntity<Object> insertItem(
            MultipartFile file,
            String title,
            String price,
            String categoryId
    )throws IOException {
        Category category = categoryService.findById(Long.parseLong(categoryId))
                .orElseThrow(()->new EntityNotFoundException("없는 카테고리 입니다."));

        String imageUrl = imageServiceImple.uploadImageToS3(file);

        Items items = Items.builder()
                .itemTitle(title)
                .imgUrl(imageUrl)
                .itemPrice(Long.parseLong(price))
                .category(category)
                .build();
        ItemsDTO itemsDTO = Items.toDTO(itemsRepository.save(items));
        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK,"저장했습니다.", itemsDTO ));
    }

    @Override
    public ResponseEntity<Object> updateItem(
            MultipartFile file,
            String title,
            String price,
            String itemid,
            String imgurl
    )throws IOException{
        ItemsDTO itemsDTO = Items.toDTO(itemsRepository.getById(Long.parseLong(itemid)));
        String newImageUrl = imgurl;
        if(file != null && !file.isEmpty()) {
            newImageUrl = imageServiceImple.updaetImageOnS3(imgurl, file);
        }
        itemsDTO.setItemTitle(title);
        itemsDTO.setItemPrice(Long.parseLong(price));
        itemsDTO.setImgUrl(newImageUrl);
        itemsDTO = Items.toDTO(itemsRepository.save(ItemsDTO.toEntity(itemsDTO)));

        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK,"저장했습니다.", itemsDTO ));
    }

    @Override
    public ResponseEntity<Object> deleteItem(Long itemId){
        String imageUrl = itemsRepository.getById(itemId).getImgUrl();
        imageServiceImple.deleteImageFromS3(imageUrl);
        itemsRepository.deleteById(itemId);
        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK, "삭제했습니다.", 1));
    }
}
