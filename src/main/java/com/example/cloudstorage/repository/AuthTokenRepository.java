package com.example.cloudstorage.repository;

import com.example.cloudstorage.model.AuthToken;
import com.example.cloudstorage.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    Optional<AuthToken> findByAuthToken(String authToken);

    @Transactional
    void deleteAuthTokensByAuthToken(String authToken);

    AuthToken findUserIdByAuthToken(String authToken);
}