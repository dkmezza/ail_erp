package com.apeck.erp.retirement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.apeck.erp.common.exception.BadRequestException;
import com.apeck.erp.config.FileStorageConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * File Storage Service
 * Handles file upload and storage for retirement receipts
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileStorageConfig fileStorageConfig;
    
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "pdf");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    /**
     * Store uploaded file
     */
    public String storeFile(MultipartFile file) {
        // Validate file
        validateFile(file);
        
        try {
            // Get original filename
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            
            // Check for invalid characters
            if (originalFilename.contains("..")) {
                throw new BadRequestException("Filename contains invalid path sequence: " + originalFilename);
            }
            
            // Generate unique filename
            String fileExtension = getFileExtension(originalFilename);
            String newFilename = UUID.randomUUID().toString() + "." + fileExtension;
            
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(fileStorageConfig.getUploadDir());
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Copy file to target location
            Path targetLocation = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("File stored successfully: {}", newFilename);
            
            return newFilename;
            
        } catch (IOException ex) {
            throw new BadRequestException("Failed to store file: " + ex.getMessage());
        }
    }

    /**
     * Validate uploaded file
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }
        
        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds maximum limit of 5MB");
        }
        
        // Check file extension
        String filename = file.getOriginalFilename();
        String extension = getFileExtension(filename);
        
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new BadRequestException("Invalid file type. Allowed types: " + ALLOWED_EXTENSIONS);
        }
    }

    /**
     * Get file extension
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new BadRequestException("Invalid filename");
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    /**
     * Delete file
     */
    public void deleteFile(String filename) {
        try {
            Path filePath = Paths.get(fileStorageConfig.getUploadDir()).resolve(filename);
            Files.deleteIfExists(filePath);
            log.info("File deleted: {}", filename);
        } catch (IOException ex) {
            log.error("Failed to delete file: {}", filename, ex);
        }
    }
}