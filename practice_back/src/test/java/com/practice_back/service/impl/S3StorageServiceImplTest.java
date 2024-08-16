package com.practice_back.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class S3StorageServiceImplTest{
    @Autowired
    S3StorageServiceImpl s3StorageService;
    @DisplayName("파일과 파일 이름을 전달하면 S3에 저장이되고 해당 URL이 반환된다.")
    @Test
    void uploadFileAndgetFileUrl(){
        // Given
        File file = new File("C:\\Users\\UserKYJ\\Downloads\\picture.jpeg");
        // When
        String fileName = "test-kyj";
        String str1 = s3StorageService.uploadFile(file, fileName);
        String str2 = s3StorageService.getFileUrl(fileName);
        // Then
        assertThat(str1).isNotNull();
        assertEquals(str1,str2);
        assertEquals(str1.endsWith(fileName), true);
    }
    @DisplayName("파일 이름 전달시 해당 데이터를 삭제할 수 있다.")
    @Test
    void deleteFile(){
        // Given
        File file = new File("C:\\Users\\UserKYJ\\Downloads\\picture.jpeg");
        String fileName = "test-kyj";
        String str1 = s3StorageService.uploadFile(file, fileName);
        // When
        s3StorageService.deleteFile(fileName);

        String str2 = s3StorageService.getFileUrl(fileName);
        // Then
        assertEquals(str1.endsWith(fileName),true);
        assertEquals(str2, null);
    }
}