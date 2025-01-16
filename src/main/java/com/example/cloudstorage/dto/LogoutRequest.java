package com.example.cloudstorage.dto;

import org.springframework.stereotype.Component;

public class LogoutRequest {
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}