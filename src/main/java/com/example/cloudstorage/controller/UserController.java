package com.example.cloudstorage.controller;

import com.example.cloudstorage.dto.*;
import com.example.cloudstorage.model.User;
import com.example.cloudstorage.service.AuthService;
import com.example.cloudstorage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.findUserByLogin(loginRequest.getLogin());
        if (user == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Invalid login or password");
            errorResponse.setId(400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } else {
            String authToken = authService.generateAuthToken(user);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setAuthToken(authToken);
            return ResponseEntity.ok(loginResponse);
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