package com.example.cloudstorage.dto;

import org.springframework.stereotype.Component;


public class RenameRequest {
    private String newName;

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}