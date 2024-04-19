package com.practice_back.service;

import com.practice_back.dto.CategoryDTO;
import com.practice_back.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    public List<CategoryDTO> getCategories();
    public Optional<Category> findById(Long id);
}
