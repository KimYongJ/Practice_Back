package com.practice_back.api;

import com.practice_back.dto.LottoNumberDTO;
import com.practice_back.service.impl.LottoNumberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor // 클래스 내에 final 키워드가 붙거나 @NonNull 어노테이션이 붙은 필드들을 인자로 하는 생성자를 자동으로 생성
@RequestMapping(value = "/api/user/number")
public class LottoNumberController {
    private final LottoNumberServiceImpl LnumServiceImpl;

    /**
     * 로또 번호를 조회하는 엔드포인트입니다.
     *
     * @return 조회된 로또 번호를 담은 ResponseEntity
     */
    @GetMapping()
    public ResponseEntity<Object> getNumber() {
        return ResponseEntity.ok()
                .body(LnumServiceImpl.getNumber());
    }

}
