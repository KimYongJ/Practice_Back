package com.practice_back.service.impl;

import com.practice_back.dto.CategoryDTO;
import com.practice_back.entity.Category;
import com.practice_back.repository.CategoryRepository;
import com.practice_back.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    /*
    * 모든 카테고리를 조회
    *
    * @return List<CategoryDTO>
    * */
    @Override
    public List<CategoryDTO> getCategories()
    {
        return categoryRepository.findAll()
                .stream().map(Category::toDTO)
                .collect(Collectors.toList());
    }
}
