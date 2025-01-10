package com.example.cloudstorage.dto;

import org.springframework.stereotype.Component;


public class LogoutResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}