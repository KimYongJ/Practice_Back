package com.practice_back.file;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
public class CustomMultipartFile implements MultipartFile {
    private final byte[] fileContent;
    private String fileName;
    private String contentType;

    public CustomMultipartFile(File file) throws IOException {
        this.fileName = file.getName();
        this.contentType = "image/jpeg"; // 적절한 MIME 타입 설정
        this.fileContent = new byte[(int) file.length()];
        try (FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(this.fileContent);
        }
    }

    @Override
    public String getName() {
        return this.fileName;
    }

    @Override
    public String getOriginalFilename() {
        return this.fileName;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return this.fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return this.fileContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return this.fileContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.fileContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream out = new FileOutputStream(dest)) {
            out.write(this.fileContent);
        }
    }
}