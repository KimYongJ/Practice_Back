package com.practice_back.service;

import java.io.File;

public interface StorageService {
    String uploadFile(File file, String fileName);
    void deleteFile(String fileName);
    String getFileUrl(String fileName);
}
