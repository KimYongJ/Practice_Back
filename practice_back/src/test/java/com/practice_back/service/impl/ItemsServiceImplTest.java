package com.practice_back.service.impl;

import com.practice_back.dto.ItemsDTO;
import com.practice_back.entity.Category;
import com.practice_back.entity.Items;
import com.practice_back.file.CustomMultipartFile;
import com.practice_back.repository.CategoryRepository;
import com.practice_back.repository.ItemsRepository;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemsServiceImplTest {
    @Autowired
    ItemsRepository itemsRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ItemsServiceImpl itemsServiceImpl;
    @AfterEach
    void tearDown(){
        itemsRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();
    }

    @DisplayName("아이템아이디, 아이템 이름, 가격의 범위를 갖고 아이템들을 갖고 올 수 있다.")
    @Test
    void getItems(){
        Category category = createTempCategory();
        Items item1 = createItem("사과",500L,"www.naver.com",category);
        Items item2 = createItem("배",1500L,"www.daum.net",category);
        Items item3 = createItem("딸기",2000L,"www.kt.com",category);
        Items item4 = createItem("바나나",1800L,"www.lg.com",category);
        itemsRepository.saveAll(List.of(item1,item2,item3,item4));
        // Given When
        Pageable pageable = PageRequest.of(0,10);
        ResponseEntity<Object> result1 = itemsServiceImpl.getItems(
                null,
                List.of(category.getCategoryId()),
                null,
                500L,
                1500L,
                pageable
        );
        ResponseEntity<Object> result2 = itemsServiceImpl.getItems(
                null,
                List.of(category.getCategoryId()),
                "딸기",
                null,
                null,
                pageable
        );
        ResponseEntity<Object> result3 = itemsServiceImpl.getItems(
                item1.getItemId(),
                List.of(category.getCategoryId()),
                null,
                null,
                null,
                pageable
        );
        // Then
        assertThat(result1).satisfies(res->{
            assertEquals(res.getStatusCode(),HttpStatus.OK);

            Message msg = (Message)res.getBody();
            assertEquals(msg.getStatus(), ErrorType.OK);
            assertEquals(msg.getMessage(),"조회 성공");

            Page<ItemsDTO> items = (Page<ItemsDTO>)msg.getData();
            assertThat(items)
                    .hasSize(2)
                    .extracting("itemTitle","itemPrice","imgUrl")
                    .containsExactlyInAnyOrder(
                            tuple("사과",500L,"www.naver.com"),
                            tuple("배",1500L,"www.daum.net")
                    );
        });
        assertThat(result2).satisfies(res->{
            assertEquals(res.getStatusCode(),HttpStatus.OK);

            Message msg = (Message)res.getBody();
            assertEquals(msg.getStatus(), ErrorType.OK);
            assertEquals(msg.getMessage(),"조회 성공");

            Page<ItemsDTO> items = (Page<ItemsDTO>)msg.getData();
            assertThat(items)
                    .hasSize(1)
                    .extracting("itemTitle","itemPrice","imgUrl")
                    .containsExactlyInAnyOrder(
                            tuple("딸기",2000L,"www.kt.com")
                    );
        });
        assertThat(result3).satisfies(res->{
            assertEquals(res.getStatusCode(),HttpStatus.OK);

            Message msg = (Message)res.getBody();
            assertEquals(msg.getStatus(), ErrorType.OK);
            assertEquals(msg.getMessage(),"조회 성공");

            Page<ItemsDTO> items = (Page<ItemsDTO>)msg.getData();
            assertThat(items)
                    .hasSize(1)
                    .extracting("itemTitle","itemPrice","imgUrl")
                    .containsExactlyInAnyOrder(
                            tuple("사과",500L,"www.naver.com")
                    );
        });
    }
    @DisplayName("아이템 고유ID로 아이템을 가져올 수 있다.")
    @Test
    void getItemsByItemId(){
        // Given
        Category category = createTempCategory();
        Items item1 = createItem("사과",500L,"www.naver.com",category);
        Items item2 = createItem("배",1500L,"www.daum.net",category);
        Items item3 = createItem("딸기",2000L,"www.kt.com",category);
        Items item4 = createItem("바나나",1800L,"www.lg.com",category);
        itemsRepository.saveAll(List.of(item1,item2,item3,item4));
        // When
        ResponseEntity<Object> result1 = itemsServiceImpl.getItemsByItemId(1L);
        ResponseEntity<Object> result2 = itemsServiceImpl.getItemsByItemId(2L);
        // Then
        assertThat(result1).satisfies(res->{
            assertEquals(res.getStatusCode(), HttpStatus.OK);
            ItemsDTO itemDTO = (ItemsDTO)res.getBody();
            assertThat(itemDTO)
                    .extracting("itemId","itemTitle","itemPrice","imgUrl")
                    .containsExactlyInAnyOrder(1L,"사과",500L,"www.naver.com");
        });
        assertThat(result2).satisfies(res->{
            assertEquals(res.getStatusCode(), HttpStatus.OK);
            ItemsDTO itemDTO = (ItemsDTO)res.getBody();
            assertThat(itemDTO)
                    .extracting("itemId","itemTitle","itemPrice","imgUrl")
                    .containsExactlyInAnyOrder(2L,"배",1500L,"www.daum.net");
        });
    }
    @DisplayName("파일정보와, 제목, 가격, 카테고리ID를 전달해 아이템 추가할 수 있다.")
    @Test
    void insertItem()throws Exception{
        // Given
        Category category = createTempCategory();
        File file = new File("C:\\Users\\UserKYJ\\Downloads\\picture.jpeg");
        CustomMultipartFile mulFile = new CustomMultipartFile(file);
        String title = "임시파일";
        String price = "100000";
        String categoryId = String.valueOf(category.getCategoryId());
        // When
        ResponseEntity<Object> result = itemsServiceImpl.insertItem(mulFile,title,price,categoryId);
        // Then
        assertThat(result).satisfies(res->{
            assertEquals(res.getStatusCode(), HttpStatus.OK);

            Message msg = (Message)res.getBody();
            assertEquals(msg.getStatus(), ErrorType.OK);
            assertEquals(msg.getMessage(),"저장했습니다.");

            ItemsDTO itemsDTO = (ItemsDTO)msg.getData();
            assertThat(itemsDTO)
                    .extracting("itemTitle","itemPrice")
                    .containsExactly("임시파일",100000L);
        });
    }
    @DisplayName("아이템 고유ID로 제목과, 가격, 이미지를 변경할 수 있다.")
    @Test
    void updateItem()throws Exception{
        // Given
        Category category = createTempCategory();

        Items item1 = createItem("사과",500L,"www.naver.com",category);

        itemsRepository.save(item1);

        File file = new File("C:\\Users\\UserKYJ\\Downloads\\picture.jpeg");
        CustomMultipartFile mulFile = new CustomMultipartFile(file);
        String title = "임시파일";
        String price = "100000";
        String itemId = String.valueOf(item1.getItemId());
        // When
        ResponseEntity<Object> result = itemsServiceImpl.updateItem(mulFile,title,price,itemId,"www.naver.com");
        // Then
        assertThat(result).satisfies(res->{
            assertEquals(res.getStatusCode(), HttpStatus.OK);

            Message msg = (Message)res.getBody();
            assertEquals(msg.getStatus(), ErrorType.OK);
            assertEquals(msg.getMessage(),"수정했습니다.");

            ItemsDTO itemsDTO = (ItemsDTO)msg.getData();
            assertThat(itemsDTO)
                    .extracting("itemId","itemTitle","itemPrice")
                    .containsExactly(item1.getItemId(),"임시파일",100000L);
            System.out.println(itemsDTO.getImgUrl());
        });
    }
    @DisplayName("아이템 고유ID로 아이템을 삭제할 수 있다.")
    @Test
    void deleteItem(){
        // Given When
        Category category = createTempCategory();

        Items originItem = createItem("사과",500L,"www.naver.com",category);
        itemsRepository.save(originItem);

        ResponseEntity<Object> result1 = itemsServiceImpl.deleteItem(originItem.getItemId());

        Items item = itemsRepository.findByItemId(originItem.getItemId());
        // Then
        assertThat(result1).satisfies(res->{
            assertEquals(res.getStatusCode(), HttpStatus.OK);
            Message msg = (Message)res.getBody();
            assertEquals(msg.getStatus(), ErrorType.OK);
            assertEquals(msg.getMessage(),"삭제했습니다.");
            assertEquals((int)msg.getData(),1);
        });
        assertEquals(item,null);
    }
    @DisplayName("존재하지 않는 고유ID를 통해 삭제요청을 하면 삭제되지 않는다.")
    @Test
    void shouldNotDeleteWithInvalidId(){
        // Given
        Category category = createTempCategory();

        Items item = createItem("사과",500L,"www.naver.com",category);

        itemsRepository.save(item);
        // When
        ResponseEntity<Object> result = itemsServiceImpl.deleteItem(999L);

        itemsServiceImpl.deleteItem(item.getItemId());

        ResponseEntity<Object> result2 = itemsServiceImpl.deleteItem(item.getItemId());

        // Then
        assertThat(result).satisfies(res->{
            assertEquals(res.getStatusCode(), HttpStatus.OK);
            Message msg = (Message)res.getBody();
            assertEquals(msg.getStatus(), ErrorType.OK);
            assertEquals(msg.getMessage(),"삭제할 데이터가 없습니다.");
            assertEquals((int)msg.getData(),0);
        });
        assertThat(result2).satisfies(res->{
            assertEquals(res.getStatusCode(), HttpStatus.OK);
            Message msg = (Message)res.getBody();
            assertEquals(msg.getStatus(), ErrorType.OK);
            assertEquals(msg.getMessage(),"삭제할 데이터가 없습니다.");
            assertEquals((int)msg.getData(),0);
        });
    }
    public Items createItem(String title, Long price, String imgUrl, Category category){
        return Items.builder()
                .itemTitle(title)
                .itemPrice(price)
                .imgUrl(imgUrl)
                .category(category)
                .build();
    }
    public Category createTempCategory(){
        Category category = Category.builder()
                .categoryTitle("임시카테고리")
                .build();
        return categoryRepository.save(category);
    }
}