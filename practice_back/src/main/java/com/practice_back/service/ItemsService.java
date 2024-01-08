package com.practice_back.service;
import com.practice_back.dto.ItemsDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface ItemsService {
    public ResponseEntity<?> getItemsByCatagory(String category);
}
