package com.practice_back.dto;
import com.practice_back.entity.LottoNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LottoNumberDTO {
    private Long lottoNumberId;
    private Date insertDate;
    private String insertId;
    private int nOne;
    private int nTwo;
    private int nThree;
    private int nFour;
    private int nFive;
    private int nSix;
    public LottoNumber toLottoNumberEntity(LottoNumberDTO lottoNumberDTO){
        return LottoNumber.builder()
                .lottoNumberId(lottoNumberDTO.getLottoNumberId())
                .insertDate(lottoNumberDTO.getInsertDate())
                .insertId(lottoNumberDTO.getInsertId())
                .nOne(lottoNumberDTO.getNOne())
                .nTwo(lottoNumberDTO.getNTwo())
                .nThree(lottoNumberDTO.getNThree())
                .nFour(lottoNumberDTO.getNFour())
                .nFive(lottoNumberDTO.getNFive())
                .nSix(lottoNumberDTO.getNSix())
                .build();
    }
}
