package com.example.cloudstorage.dto;


public class ErrorResponse {
    private String message;
    private int id;

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId(int id) {
        this.id = id;
    }
}