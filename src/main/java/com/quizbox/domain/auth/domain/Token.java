package com.quizbox.domain.auth.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "username")
    private String username;

    @Column(name = "expiration")
    private Long expiration;


    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }


    @Builder
    public Token(String refreshToken, String username, Long expiration) {
        this.refreshToken = refreshToken;
        this.username = username;
        this.expiration = expiration;
    }
}
