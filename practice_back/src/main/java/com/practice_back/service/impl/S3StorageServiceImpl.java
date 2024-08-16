package com.practice_back.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.practice_back.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
@RequiredArgsConstructor
public class S3StorageServiceImpl implements StorageService {
    @Value("${cloud.aws.s3.bucketname}")
    private String bucketName;
    private final AmazonS3Client amazonS3Client;

    @Override
    public String uploadFile(File file, String fileName) {
        try{
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
            return getFileUrl(fileName);
        }catch(Exception e){
            throw new RuntimeException("Failed to upload file to S3: " + e.getMessage(), e);
        }
    }
    @Override
    public String getFileUrl(String fileName) {
        try{
            boolean doesExist = amazonS3Client.doesObjectExist(bucketName, fileName);
            if(!doesExist){
                return null;
            }
            return amazonS3Client.getUrl(bucketName, fileName).toString();
        } catch (AmazonServiceException e) {
            // 서비스 측 오류 처리: 예를 들어 잘못된 버킷 이름, 권한 문제 등
            throw new RuntimeException("Amazon service error occurred: " + e.getErrorMessage(), e);
        } catch (SdkClientException e) {
            // 클라이언트 측 오류 처리: 예를 들어 네트워크 연결 문제 등
            throw new RuntimeException("Client error occurred: Unable to retrieve file URL.", e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try{
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        }catch (Exception e) {
            throw new RuntimeException("Failed to delete file from S3: " + e.getMessage(), e);
        }
    }


}
