package com.example.cloudstorage.service;

import com.example.cloudstorage.model.User;
import com.example.cloudstorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
       public User findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}