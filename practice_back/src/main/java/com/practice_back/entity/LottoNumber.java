package com.practice_back.entity;
import com.practice_back.dto.LottoNumberDTO;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter // 갯터
@Entity // 엔티티임을 알리는 어노테이션
@Builder // 빌더
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 만들어줌
@NoArgsConstructor // 파라미터가 없는 기본 생성자 생성
@DynamicInsert // 쿼리실행시 값있는 컬럼만 포함해 insert(널 제외 동적insert)
@EntityListeners(AuditingEntityListener.class)
public class LottoNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lotto_number_id")
    private Long lottoNumberId;

    @CreatedDate
    @Column(name = "insert_date",updatable = false)
    private Date insertDate;

    @Column(name = "inset_id",nullable=false)
    private String insertId;

    @Column(name = "n_one",nullable = false)
    private int nOne;
    @Column(name = "n_two",nullable = false)
    private int nTwo;
    @Column(name = "n_three",nullable = false)
    private int nThree;
    @Column(name = "n_four",nullable = false)
    private int nFour;
    @Column(name = "n_five",nullable = false)
    private int nFive;
    @Column(name = "n_six",nullable = false)
    private int nSix;

    public LottoNumberDTO toLottoNumberDTO(LottoNumber lottoNumber){
        return LottoNumberDTO.builder()
                .lottoNumberId(lottoNumber.getLottoNumberId())
                .insertDate(lottoNumber.getInsertDate())
                .insertId(lottoNumber.getInsertId())
                .nOne(lottoNumber.getNOne())
                .nTwo(lottoNumber.getNTwo())
                .nThree(lottoNumber.getNThree())
                .nFour(lottoNumber.getNFour())
                .nFive(lottoNumber.getNFive())
                .nSix(lottoNumber.getNSix())
                .build();
    }

}
