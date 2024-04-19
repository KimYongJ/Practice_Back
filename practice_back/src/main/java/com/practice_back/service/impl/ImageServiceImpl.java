package com.practice_back.service.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl {
    @Value("${cloud.aws.s3.bucketname}")
    private String bucketName;
    private final AmazonS3Client amazonS3Client;

    public String uploadImageToS3(String image){
        String[] parts = image.split(",");
        byte[] data = Base64.decodeBase64( parts[1] );
        InputStream inputStream = new ByteArrayInputStream(data);

        String fileName = UUID.randomUUID().toString() + ".png";
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength( data.length );
        metadata.setContentType("image/png");

        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));

        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }
    public String updaetImageOnS3(String before, String after){
        String imageUrl = uploadImageToS3(after);
        deleteImageFromS3(before);
        return imageUrl;
    }
    public void deleteImageFromS3(String imageUrl){
        String key = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, key));
    }

}
