package com.practice_back.service.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.practice_back.exception.InvalidImageFileException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl {

    private final S3StorageServiceImpl s3StorageServiceImpl;

    public String uploadImage(MultipartFile file,String uid, LocalDateTime now)  throws IOException {
        File fileObj = convertMultiPartFileToFile(file);

        String fileName = generateFileName(uid,now);

        String url = s3StorageServiceImpl.uploadFile(fileObj,fileName);

        fileObj.delete(); // 임시 파일 삭제

        return url;
    }
    public String updateImage(String beforeImgUrl, MultipartFile file,String uid, LocalDateTime now) throws IOException{
        String imageUrl = uploadImage(file,uid, now);
        if(beforeImgUrl != null) {
            deleteImage(beforeImgUrl);
        }
        return imageUrl;
    }
    public void deleteImage(String imageUrl){

        String key = imageUrl.substring(imageUrl.lastIndexOf("/")+1);

        s3StorageServiceImpl.deleteFile(key);
    }
    public File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        if( !isValidImageFile(file) ){
            throw new InvalidImageFileException("유효하지 않은 이미지 형식입니다.");
        }
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
    public String generateFileName(String UUID, LocalDateTime now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String timestamp = now.format(formatter);
        return UUID + "_" + timestamp;
    }
    public boolean isValidImageFile(MultipartFile file) {
        final long MAX_SIZE = 5 * 1024 * 1024; // 5MB
        try {
            byte[] fileBytes = file.getBytes();
            // 파일 크기 검사
            if (file.getSize() > MAX_SIZE) {
                return false;
            }
            // 이미지 정보를 가져옴
            ImageInfo imageInfo = Imaging.getImageInfo(fileBytes);
            return imageInfo.getFormat().equals(ImageFormats.PNG) ||
                    imageInfo.getFormat().equals(ImageFormats.JPEG) ||
                    imageInfo.getFormat().equals(ImageFormats.BMP);
        } catch (Exception e) {
            return false;
        }
    }
}
