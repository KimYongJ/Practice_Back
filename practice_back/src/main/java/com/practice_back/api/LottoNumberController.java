package com.practice_back.api;

import com.practice_back.dto.LottoNumberDTO;
import com.practice_back.service.impl.LottoNumberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user/number")
@RequiredArgsConstructor
public class LottoNumberController {
    @Autowired
    LottoNumberServiceImpl LnumServiceImpl;
    @GetMapping(value = "/get")
    public LottoNumberDTO selectBoard() {
        LottoNumberDTO result = LnumServiceImpl.getNumber();
        return result;
    }


}
