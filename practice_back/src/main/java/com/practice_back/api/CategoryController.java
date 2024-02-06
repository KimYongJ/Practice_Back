package com.practice_back.api;

import com.practice_back.dto.CategoryDTO;
import com.practice_back.response.ErrorType;
import com.practice_back.response.Message;
import com.practice_back.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;




@RestController
@RequiredArgsConstructor // 클래스 내에 final 키워드가 붙거나 @NonNull 어노테이션이 붙은 필드들을 인자로 하는 생성자를 자동으로 생성
@RequestMapping(value = "/api/user/category")
public class CategoryController {

    private final CategoryServiceImpl categoryServiceImpl;
    /*
     * 모든 카테고리 받아오기
     *
     * @return ResponseEntity<Object>
     * */
    @GetMapping()
    public ResponseEntity<Object> getAllCategory()
    {
        List<CategoryDTO> categories = categoryServiceImpl.getCategories();
        return ResponseEntity.ok()
                .body(new Message(ErrorType.OK, "성공",categories));
    }
}
