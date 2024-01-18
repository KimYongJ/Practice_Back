package com.practice_back.api;

import com.practice_back.dto.CategoryDTO;
import com.practice_back.response.Message;
import com.practice_back.response.StatusEnum;
import com.practice_back.service.impl.CategoryServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping(value = "/api/user/category")
public class CategoryController {

    @Autowired
    CategoryServiceImpl categoryServiceImpl;
    @GetMapping()
    public ResponseEntity<Object> getAllCategory()
    {
        List<CategoryDTO> categories = categoryServiceImpl.getCategories();
        Message message = new Message();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        message.setMessage("성공");
        message.setStatus(StatusEnum.OK);
        message.setData(categories);
        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }
}
