package com.example.cloudstorage.model;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tokens")
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

    public AuthToken() {

    }

    public Long getId() {
        return id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public User getUser() {
        return user;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}