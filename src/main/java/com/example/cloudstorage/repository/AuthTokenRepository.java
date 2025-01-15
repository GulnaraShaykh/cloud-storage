package com.example.cloudstorage.repository;

import com.example.cloudstorage.model.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    Optional<AuthToken> findByAuthToken(String authToken);
    void deleteByAuthToken(String authToken);
}