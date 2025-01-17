package com.example.cloudstorage.dto;


import org.springframework.stereotype.Component;

@Component
public class LoginRequest {
    private String login;
    private String password;

    // Геттеры и сеттеры
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
