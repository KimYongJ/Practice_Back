package com.practice_back.service.impl;

import com.practice_back.dto.LottoNumberDTO;
import com.practice_back.repository.LottoNumberRepository;
import com.practice_back.service.LottoNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.Random;
@Service
@RequiredArgsConstructor
@Transactional
public class LottoNumberServiceImpl implements LottoNumberService {

    private final LottoNumberRepository LnumRepo;
    @Override
    public LottoNumberDTO getNumber(){
        int[] arr = randomArray();
        LottoNumberDTO dto = LottoNumberDTO.builder()
                // .lottoNumberId(lottoNumber.getLottoNumberId())
                .insertId("MASTER TEST")
                .nOne(arr[0])
                .nTwo(arr[1])
                .nThree(arr[2])
                .nFour(arr[3])
                .nFive(arr[4])
                .nSix(arr[5])
                .build();
        LnumRepo.save(dto.toLottoNumberEntity(dto));
        return dto;
    }



    public int[] randomArray(){
        int[] arr = new int[6];
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        for(int i=0; i<6; i++){
            arr[i] = random.nextInt(45)+1;
            for(int j=0; j<i;j++){
                if(arr[j]==arr[i]){
                    i--;
                    break;
                }
            }
        }
        Arrays.sort(arr);
        return arr;
    }
}
