package com.example.cloudstorage.unit;

import com.example.cloudstorage.controller.UserController;
import com.example.cloudstorage.dto.ErrorResponse;
import com.example.cloudstorage.dto.LoginRequest;
import com.example.cloudstorage.dto.LoginResponse;
import com.example.cloudstorage.dto.LogoutResponse;
import com.example.cloudstorage.model.User;
import com.example.cloudstorage.service.AuthService;
import com.example.cloudstorage.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
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

        String nonExistentLogin = "invalidUser";
        String password = "password123";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(nonExistentLogin);
        loginRequest.setPassword(password);

        when(userService.findUserByLogin(nonExistentLogin)).thenReturn(null);

        ResponseEntity<?> response = userController.login(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Invalid login or password", errorResponse.getMessage());
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void loginUser_InvalidPassword() {

        String validLogin = "validUser";
        String invalidPassword = "wrongPassword";
        String correctPassword = "correctPassword";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(validLogin);
        loginRequest.setPassword(invalidPassword);

        User mockUser = new User();
        mockUser.setLogin(validLogin);
        mockUser.setPassword(correctPassword);

        when(userService.findUserByLogin(validLogin)).thenReturn(mockUser);

        ResponseEntity<?> response = userController.login(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Статус ответа должен быть 400 BAD_REQUEST");

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Invalid login or password", errorResponse.getMessage(), "Сообщение об ошибке должно быть 'Invalid login or password'");
        assertEquals(400, response.getStatusCode().value(), "ID ошибки должен быть 400");
    }


    @Test
    void logoutUser_Success() {

        String mockToken = "Bearer validToken";

        when(authService.isActiveToken(mockToken)).thenReturn(true);

        ResponseEntity<?> response = userController.logout(mockToken);

        // Assert: проверяем, что метод вернул успешный статус и корректный ответ
        assertEquals(HttpStatus.OK, response.getStatusCode());

        LogoutResponse logoutResponse = (LogoutResponse) response.getBody();
        assertNotNull(logoutResponse);
        assertEquals("Success logout", logoutResponse.getMessage());
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