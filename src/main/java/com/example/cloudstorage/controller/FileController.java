package com.example.cloudstorage.controller;

import com.example.cloudstorage.dto.RenameRequest;
import com.example.cloudstorage.model.File;
import com.example.cloudstorage.service.AuthService;
import com.example.cloudstorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private AuthService authService;

    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String fileName,
            @RequestParam("file") MultipartFile file) {
        if (!authService.isActiveToken(authToken)) {
            return ResponseEntity.status(401).body("Unauthorized error");
        }
        try {fileService.uploadFile(file, fileName);
            return ResponseEntity.status(200).body("Success upload");
        } catch (IOException e) {
            return ResponseEntity.status(400).body("Error input data " + e.getMessage());
        }
    }

    @DeleteMapping("/file")
    public ResponseEntity<String> deleteFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String fileName) {
        if (!authService.isActiveToken(authToken)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        boolean isDeleted = fileService.deleteFile(fileName);
        if (isDeleted) {
            return ResponseEntity.status(200).body("File deleted successfully");
        } else {
            return ResponseEntity.status(500).body("File not found");
        }
    }

    @GetMapping("/file")
    public ResponseEntity<?> downloadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String fileName) {

        if (!authService.isActiveToken(authToken)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        String filePath = "C:\\my_uploads\\" + fileName;  // Здесь указываете свой путь
        java.io.File file = new java.io.File(filePath);

        if (!file.exists()) {
            return ResponseEntity.status(404).body("File not found");
        }
        try {InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error reading file: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllFiles(@RequestHeader("auth-token") String token) {
        if (!authService.isActiveToken(token)) {
            return ResponseEntity.status(401).body(null);
        }

        List<File> files = fileService.getAllFiles();

        List<Map<String, Object>> fileInfoList = files.stream()
                .map(file -> {
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("filename", file.getFileName());       // Имя файла
                    fileInfo.put("size", file.getFileSize());          // Размер файла
                    fileInfo.put("lastModified", file.getUploadedAt()); // Дата изменения файла
                    return fileInfo;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(fileInfoList);
    }


    @PutMapping("/file")
    public ResponseEntity<String> renameFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String fileName,  // Параметр запроса для старого имени файла
            @RequestBody RenameRequest renameRequest) { // Тело запроса содержит новое имя файла

        if (!authService.isActiveToken(authToken)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        boolean isRenamed = fileService.renameFile(fileName, renameRequest.getNewName());
        if (isRenamed) {
            return ResponseEntity.status(200).body("File renamed successfully");
        } else {
            return ResponseEntity.status(500).body("File not found");
        }
    }
}