package com.rentit.controller;

import com.rentit.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ImageController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> uploadImages(
            @RequestParam("images") MultipartFile[] files
    ) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            List<Map<String, String>> imageUrls = new ArrayList<>();

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue;
                }

                String originalFilename = file.getOriginalFilename();
                String filename = UUID.randomUUID().toString() + "_" + originalFilename;
                Path filePath = uploadPath.resolve(filename);

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                Map<String, String> imageInfo = new HashMap<>();
                imageInfo.put("filename", filename);
                imageInfo.put("url", "/uploads/" + filename);
                imageInfo.put("size", String.valueOf(file.getSize()));
                imageInfo.put("mimetype", file.getContentType());
                imageUrls.add(imageInfo);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Images uploaded successfully");
            response.put("images", imageUrls);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload images: " + e.getMessage());
        }
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Image not found");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading image: " + e.getMessage());
        }
    }

    @DeleteMapping("/{filename:.+}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponse> deleteImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Files.deleteIfExists(filePath);
            return ResponseEntity.ok(new MessageResponse("Image deleted successfully"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image: " + e.getMessage());
        }
    }
}
