package com.practice_back.service.impl;

import com.practice_back.exception.InvalidImageFileException;
import com.practice_back.file.CustomMultipartFile;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ImageServiceImplTest {
    @Autowired
    ImageServiceImpl imageService;
    @Autowired
    S3StorageServiceImpl s3StorageService;
    @DisplayName("오늘 날짜를 전달하여 파일명을 만들 수 있다.")
    @Test
    void generateFileName(){
        // Given
        LocalDateTime now = LocalDateTime.now();
        String uid = UUID.randomUUID().toString();
        // When
        String res = imageService.generateFileName(uid, now);
        String expectedValue = uid + "_" + now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")) ;
        // Then
        assertEquals(res, expectedValue);
    }
    @DisplayName("파일 전달시 유효성을 체크한다.")
    @Test
    void isValidImageFile() throws Exception{
        // Given
        CustomMultipartFile file1 = new CustomMultipartFile(new File("C:\\Users\\UserKYJ\\Downloads\\picture.jpeg"));
        CustomMultipartFile file2 = new CustomMultipartFile(new File("C:\\Users\\UserKYJ\\Downloads\\invalid.png"));
        // When
        boolean res1 = imageService.isValidImageFile(file1);
        boolean res2 = imageService.isValidImageFile(file2);
        // Then
        assertEquals(res1, true);
        assertEquals(res2, false);
    }
    @DisplayName("MultipartFile 전달시 유효성을 체크하며 File객체로 변경 후 출력해준다.")
    @Test
    void convertMultiPartFileToFile()throws Exception{
        // Given
        CustomMultipartFile mulfile1 = new CustomMultipartFile(new File("C:\\Users\\UserKYJ\\Downloads\\picture.jpeg"));
        CustomMultipartFile mulfile2 = new CustomMultipartFile(new File("C:\\Users\\UserKYJ\\Downloads\\invalid.png"));
        // When
        File file = imageService.convertMultiPartFileToFile(mulfile1);
        File originFile = new File("C:\\Users\\UserKYJ\\Downloads\\picture.jpeg");
        // Then
        assertTrue(FileUtils.contentEquals(file,originFile));
        assertTrue(originFile.getPath().contains(file.getPath()));

        assertThatThrownBy(()->imageService.convertMultiPartFileToFile(mulfile2))
                .isInstanceOf(InvalidImageFileException.class)
                .hasMessage("유효하지 않은 이미지 형식입니다.");
    }
    @DisplayName("파일 전달시 업로드 된다.")
    @Test
    void uploadImage()throws Exception{
        // Given
        CustomMultipartFile mulfile1 = new CustomMultipartFile(new File("C:\\Users\\UserKYJ\\Downloads\\picture.jpeg"));
        String uid = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        String fileName = imageService.generateFileName(uid,now);
        // When
        String imgUrl = imageService.uploadImage(mulfile1, uid, now);
        String expectedValue = s3StorageService.getFileUrl(fileName);
        // Then
        assertEquals(imgUrl, expectedValue);
    }

    @DisplayName("기존이미지 URL과 새 파일 전달시 업데이트 된다.")
    @Test
    void updateImage()throws Exception{
        // Given
        CustomMultipartFile mulfile1 = new CustomMultipartFile(new File("C:\\Users\\UserKYJ\\Downloads\\picture.jpeg"));
        CustomMultipartFile mulfile2 = new CustomMultipartFile(new File("C:\\Users\\UserKYJ\\Downloads\\picture.jpeg"));
        String uid1 = UUID.randomUUID().toString();
        String uid2 = UUID.randomUUID().toString();
        LocalDateTime now1 = LocalDateTime.now();
        LocalDateTime now2 = LocalDateTime.now();
        String fileName1 = imageService.generateFileName(uid1,now1);
        String fileName2 = imageService.generateFileName(uid2,now2);
        // When
        String imgUrl1 = imageService.uploadImage(mulfile1, uid1, now1);
        String imgUrl2 = imageService.updateImage(imgUrl1, mulfile2, uid2, now2);
        String expectedValue1 = s3StorageService.getFileUrl(fileName1);
        String expectedValue2 = s3StorageService.getFileUrl(fileName2);
        // Then
        assertEquals(expectedValue1, null);
        assertEquals(imgUrl2,expectedValue2);
    }
    @DisplayName("이미지 URL 전달시 이미지가 삭제된다.")
    @Test
    void deleteImage()throws Exception{
        // Given
        CustomMultipartFile mulfile1 = new CustomMultipartFile(new File("C:\\Users\\UserKYJ\\Downloads\\picture.jpeg"));
        String uid1 = UUID.randomUUID().toString();
        LocalDateTime now1 = LocalDateTime.now();
        String fileName1 = imageService.generateFileName(uid1,now1);
        // When
        String imgUrl1 = imageService.uploadImage(mulfile1, uid1, now1);
        String res1 = s3StorageService.getFileUrl(fileName1);
        imageService.deleteImage(imgUrl1);
        String res2 = s3StorageService.getFileUrl(fileName1);
        // Then
        assertEquals(imgUrl1, res1);
        assertEquals(res2,null);
    }
}