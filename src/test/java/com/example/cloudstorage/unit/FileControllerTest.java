package com.example.cloudstorage.unit;

import com.example.cloudstorage.controller.FileController;
import com.example.cloudstorage.service.AuthService;
import com.example.cloudstorage.service.FileService;
import com.example.cloudstorage.dto.RenameRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadFile_Success() throws IOException {

        String mockAuthToken = "Bearer validToken";
        String fileName = "test.txt";
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());

        when(authService.isActiveToken(mockAuthToken)).thenReturn(true);
        doNothing().when(fileService).uploadFile(mockFile, fileName);

        ResponseEntity<String> response = fileController.uploadFile(mockAuthToken, fileName, mockFile);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Success upload", response.getBody());
        verify(fileService, times(1)).uploadFile(mockFile, fileName);
    }

    @Test
    void uploadFile_Unauthorized() throws IOException {

        String mockAuthToken = "Bearer invalidToken";
        String fileName = "test.txt";
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());

        when(authService.isActiveToken(mockAuthToken)).thenReturn(false);

        ResponseEntity<String> response = fileController.uploadFile(mockAuthToken, fileName, mockFile);

        assertEquals(401, response.getStatusCode().value());
        assertEquals("Unauthorized error", response.getBody());
        verify(fileService, never()).uploadFile(any(), any());
    }

    @Test
    void deleteFile_Success() {

        String mockAuthToken = "Bearer validToken";
        String fileName = "test.txt";

        when(authService.isActiveToken(mockAuthToken)).thenReturn(true);
        when(fileService.deleteFile(fileName)).thenReturn(true);

        ResponseEntity<String> response = fileController.deleteFile(mockAuthToken, fileName);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("File deleted successfully", response.getBody());
        verify(fileService, times(1)).deleteFile(fileName);
    }

    @Test
    void deleteFile_FileNotFound() {

        String mockAuthToken = "Bearer validToken";
        String fileName = "nonexistent.txt";

        when(authService.isActiveToken(mockAuthToken)).thenReturn(true);
        when(fileService.deleteFile(fileName)).thenReturn(false);

        ResponseEntity<String> response = fileController.deleteFile(mockAuthToken, fileName);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("File not found", response.getBody());
        verify(fileService, times(1)).deleteFile(fileName);
    }

    @Test
    void getAllFiles_Success() {

        String mockAuthToken = "Bearer validToken";

        when(authService.isActiveToken(mockAuthToken)).thenReturn(true);
        when(fileService.getAllFiles()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = fileController.getAllFiles(mockAuthToken);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof List);
        verify(fileService, times(1)).getAllFiles();
    }

    @Test
    void renameFile_Success() {
        // Arrange
        String mockAuthToken = "Bearer validToken";
        String oldFileName = "old.txt";
        String newFileName = "new.txt";
        RenameRequest renameRequest = new RenameRequest();
        renameRequest.setNewName(newFileName);

        when(authService.isActiveToken(mockAuthToken)).thenReturn(true);
        when(fileService.renameFile(oldFileName, newFileName)).thenReturn(true);

        // Act
        ResponseEntity<String> response = fileController.renameFile(mockAuthToken, oldFileName, renameRequest);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("File renamed successfully", response.getBody());
        verify(fileService, times(1)).renameFile(oldFileName, newFileName);
    }

    @Test
    void renameFile_FileNotFound() {
        // Arrange
        String mockAuthToken = "Bearer validToken";
        String oldFileName = "old.txt";
        String newFileName = "new.txt";
        RenameRequest renameRequest = new RenameRequest();
        renameRequest.setNewName(newFileName);

        when(authService.isActiveToken(mockAuthToken)).thenReturn(true);
        when(fileService.renameFile(oldFileName, newFileName)).thenReturn(false);

        // Act
        ResponseEntity<String> response = fileController.renameFile(mockAuthToken, oldFileName, renameRequest);

        // Assert
        assertEquals(500, response.getStatusCode().value());
        assertEquals("File not found", response.getBody());
        verify(fileService, times(1)).renameFile(oldFileName, newFileName);
    }
}