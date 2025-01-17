package com.example.cloudstorage;

import com.example.cloudstorage.controller.UserController;
import com.example.cloudstorage.dto.ErrorResponse;
import com.example.cloudstorage.dto.LoginRequest;
import com.example.cloudstorage.dto.LoginResponse;
import com.example.cloudstorage.model.User;
import com.example.cloudstorage.service.AuthService;
import com.example.cloudstorage.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSuccessfulLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("testuser");
        loginRequest.setPassword("testpassword");

        User user = new User();
        user.setLogin("testuser");
        user.setPassword("testpassword");
        user.setUserName("Ivan Ivanov");

        when(userService.findUserByLogin("testuser")).thenReturn(user);
        when(authService.generateAuthToken(user)).thenReturn("mock-token");

        ResponseEntity<?> response = userController.login(loginRequest);

        assertEquals(200, response.getStatusCode().value());
        assertInstanceOf(LoginResponse.class, response.getBody());
        assertEquals("mock-token", ((LoginResponse) response.getBody()).getAuthToken());
    }

    @Test
    void loginUser_InvalidLogin() {
        // Тест для несуществующего логина
    }

    @Test
    void loginUser_InvalidPassword() {
        // Тест для неверного пароля
    }

    @Test
    void logoutUser_Success() {
        // Тест успешного логаута
    }

    @Test
    void logoutUser_Unauthorized() {
        String authToken = "Bearer invalid-token";

        when(authService.isActiveToken("invalid-token")).thenReturn(false);

        ResponseEntity<?> response = userController.logout(authToken);

        assertEquals(401, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof ErrorResponse);
        assertEquals("Unauthorized", ((ErrorResponse) response.getBody()).getMessage());
    }

}

