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

    // Получаем ключ для подписи токена
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Логаут - удаление токена из базы данных
    public void logout(String token) {
        authTokenRepository.deleteAuthTokensByAuthToken(token);
    }

    // Проверка, активен ли токен (есть ли он в базе)
    public boolean isActiveToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Убираем первые 7 символов
        }
        return authTokenRepository.findByAuthToken(token).isPresent();
    }
}