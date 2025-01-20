package com.example.cloudstorage.integration;

import com.example.cloudstorage.model.User;

import com.example.cloudstorage.repository.UserRepository;
import com.example.cloudstorage.service.FileService;
import com.example.cloudstorage.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.support.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(locations = "classpath:application-test.properties")
public class FileControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withInitScript("data.sql");

    @DynamicPropertySource
    static void configureTestDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Test
    void testFileControllerEndpoints() throws IOException {
        // 1. Авторизация и получение токена
        String authToken = authService.generateAuthToken(
                new User("testuser", "test@example.com", "password"));

        HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);

        // 2. Загрузка файла
        String fileName = "testfile.txt";
        MockMultipartFile file = new MockMultipartFile("file", fileName, "text/plain", "Sample content".getBytes());
        HttpEntity<MultiValueMap<String, Object>> uploadRequest = new HttpEntity<>(createMultipartRequest(fileName, file), headers);
        ResponseEntity<String> uploadResponse = restTemplate.postForEntity("/file", uploadRequest, String.class);

        assertEquals(HttpStatus.OK, uploadResponse.getStatusCode());
        assertEquals("Success upload", uploadResponse.getBody());

        // 3. Получение списка файлов
        ResponseEntity<List> listResponse = restTemplate.exchange(
                "/list",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                List.class
        );

        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertNotNull(listResponse.getBody());

        // 4. Скачивание файла
        ResponseEntity<Resource> downloadResponse = restTemplate.exchange(
                "/file?filename=" + fileName,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Resource.class
        );

        assertEquals(HttpStatus.OK, downloadResponse.getStatusCode());
        assertNotNull(downloadResponse.getBody());

        // 5. Удаление файла
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                "/file?filename=" + fileName,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertEquals("File deleted successfully", deleteResponse.getBody());
    }

    private MultiValueMap<String, Object> createMultipartRequest(String fileName, MockMultipartFile file) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("filename", fileName);
        body.add("file", file.getResource());
        return body;
    }
}