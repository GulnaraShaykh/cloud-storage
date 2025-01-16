package com.example.cloudstorage.service;

import com.example.cloudstorage.model.AuthToken;
import com.example.cloudstorage.model.User;
import com.example.cloudstorage.repository.AuthTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;
    @Autowired
    private final AuthTokenRepository authTokenRepository;

    public AuthService(AuthTokenRepository authTokenRepository) {
        this.authTokenRepository = authTokenRepository;
    }

    // Генерация JWT токена
    public String generateAuthToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        String authToken = Jwts.builder()
                .setSubject(user.getUserName())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();

        AuthToken token = new AuthToken(authToken,user,now, expiryDate);
        authTokenRepository.save(token);
        return authToken;
    }

        private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

        public void logout(String token) {
        authTokenRepository.deleteAuthTokensByAuthToken(token);
    }

        public boolean isActiveToken(String token) {
        return authTokenRepository.findByAuthToken(token).isPresent();
    }
}