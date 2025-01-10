package com.example.cloudstorage.controller;

import com.example.cloudstorage.dto.RenameRequest;
import com.example.cloudstorage.model.File;
import com.example.cloudstorage.service.AuthService;
import com.example.cloudstorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<String> uploadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String fileName,
            @RequestParam("file") MultipartFile file) {
        if (!authService.isActiveToken(authToken)) {
            return ResponseEntity.status(401).body("Unauthorized error");
        }
        try {
            fileService.uploadFile(file);
            return ResponseEntity.status(200).body("Success upload");
        } catch (IOException e) {
            return ResponseEntity.status(400).body("Error input data " + e.getMessage());
        }
    }

    @DeleteMapping
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

    @GetMapping
    public ResponseEntity<?> downloadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String fileName) {
        if (!authService.isActiveToken(authToken)) {
            return ResponseEntity.status(401).body(null);
        }
        byte[] fileData = fileService.downloadFile(fileName);
        if (fileData != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileData);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<File>> getAllFiles(@RequestHeader("auth-token") String token) {
        if (!authService.isActiveToken(token)) {
            return ResponseEntity.status(401).body(null);
        }
        List<File> files = fileService.getAllFiles();
        if (files.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(files);
    }

    @PutMapping
    public ResponseEntity<String> renameFile(
            @RequestHeader("auth-token") String authtoken,
            @RequestParam("filename") String fileName,
            @RequestBody RenameRequest renameRequest) {
        if (!authService.isActiveToken(authtoken)) {
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