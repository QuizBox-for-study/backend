package com.quizbox.domain.auth.presentation;

import com.quizbox.domain.auth.domain.Token;
import com.quizbox.domain.auth.domain.repository.TokenRepository;
import com.quizbox.global.config.security.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final TokenRepository tokenRepository;


    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // get refresh token from cookies
        String refresh = null;
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie :
                cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("access token expired", HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // DB에 저장되어 있는지 확인
        Boolean isExist = tokenRepository.existsByRefreshToken(refresh);
        if (!isExist) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }


        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);


        // Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        System.out.println("refresh = " + refresh);
        tokenRepository.deleteByRefreshToken(refresh);
        addRefreshEntity(username, newRefresh, 86400000L);

        response.addCookie(createCookie("access", newAccess));
        response.addCookie(createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }


    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);

//        cookie.setSecure(true);
//        cookie.setPath("/");

        cookie.setHttpOnly(true);

        return cookie;
    }


    private void addRefreshEntity(String username, String refreshToken, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Token token = Token.builder()
                .refreshToken(refreshToken)
                .username(username)
                .expiration(expiredMs)
                .build();

        tokenRepository.save(token);

    }

}
