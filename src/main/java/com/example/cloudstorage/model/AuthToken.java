package com.example.cloudstorage.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auth_token", nullable = false)
    private String authToken;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "issued_at")
    private Date issuedAt;

    @Column(name = "expires_at")
    private Date expiresAt;

    public AuthToken(String authToken, User user, Date issuedAt, Date expiresAt) {
        this.authToken = authToken;
        this.user = user;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }
}