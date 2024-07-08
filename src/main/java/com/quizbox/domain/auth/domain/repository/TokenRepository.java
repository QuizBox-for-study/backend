package com.quizbox.domain.auth.domain.repository;

import com.quizbox.domain.auth.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Boolean existsByRefreshToken(String refreshToken);

    @Transactional
    void deleteByRefreshToken(String refreshToken);
}
