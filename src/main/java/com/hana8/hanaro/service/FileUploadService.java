package com.hana8.hanaro.service;

import com.hana8.hanaro.common.exception.ApiException;
import com.hana8.hanaro.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadService {

    @Value("${file.upload.path:src/main/resources/static/upload/}")
    private String baseUploadPath;

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB

    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, HttpStatus.BAD_REQUEST, "file size exceeds 2MB limit");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, HttpStatus.BAD_REQUEST, "only image files are allowed");
        }

        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uploadDir = baseUploadPath + dateDir + "/";
        
        try {
            Path path = Paths.get(uploadDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String savedFilename = UUID.randomUUID().toString() + extension;
            
            Path filePath = path.resolve(savedFilename);
            Files.write(filePath, file.getBytes());

            return "/upload/" + dateDir + "/" + savedFilename;
        } catch (IOException e) {
            log.error("file upload error: ", e);
            throw new ApiException(ErrorCode.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, "failed to upload file");
        }
    }
}
