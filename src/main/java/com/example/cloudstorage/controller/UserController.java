package com.example.cloudstorage.controller;

import com.example.cloudstorage.dto.*;
import com.example.cloudstorage.model.User;
import com.example.cloudstorage.service.AuthService;
import com.example.cloudstorage.service.FileService;
import com.example.cloudstorage.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Received login request for user: {}", loginRequest.getLogin());
        User user = userService.findUserByLogin(loginRequest.getLogin());
        if (user == null) {
            logger.warn("User not found for login: {}", loginRequest.getLogin());
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Invalid login or password");
            errorResponse.setId(400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } else if (!loginRequest.getPassword().equals(user.getPassword()) ) {
            logger.warn("Incorrect password for user: {}", loginRequest.getLogin());
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Invalid login or password");
            errorResponse.setId(400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } else {
            logger.info("User logged in successfully: {}", loginRequest.getLogin());
            // Генерация токена
            String authToken = authService.generateAuthToken(user);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setAuthToken(authToken);
            return ResponseEntity.ok(loginResponse);  // Отправляем токен в ответе
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String authToken) {
        if (authService.isActiveToken(authToken)) {
            authService.logout(authToken);
            LogoutResponse logoutResponse = new LogoutResponse();
            logoutResponse.setMessage("Success logout");
            return ResponseEntity.ok(logoutResponse);
        } else {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Unauthorized");
            errorResponse.setId(401);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}