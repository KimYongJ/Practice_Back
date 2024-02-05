package com.practice_back.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class) // JPA 엔티티가 생성되거나 수정될 때 자동으로 해당 필드들을 채워주는 리스너를 등록하는 역할
@MappedSuperclass           // 이 클래스가 다른 엔티티 클래스의 기본 클래스로 사용됨을 나타내며, 이 클래스의 필드들이 테이블에 매핑될 때 포함되도록 함
public class BaseAudit {
    @CreatedDate            // 엔티티가 생성되거나 수정된 날짜를 자동으로 기록
    @Column(name = "insert_dts", updatable = false)
    private LocalDateTime insertDts;

    @LastModifiedDate       // 엔티티가 생성되거나 수정된 날짜를 자동으로 기록
    @Column(name = "update_dts")
    private LocalDateTime updateDts;

    @CreatedBy              // 엔티티가 생성되거나 수정된 사용자의 정보를 자동으로 기록
    @Column(name = "insert_by",updatable = false)
    private String insertBy;

    @LastModifiedBy         // 엔티티가 생성되거나 수정된 사용자의 정보를 자동으로 기록
    @Column(name = "update_by")
    private String updateBy;
}
