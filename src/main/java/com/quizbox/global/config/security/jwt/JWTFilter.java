package com.quizbox.global.config.security.jwt;

import com.quizbox.domain.auth.dto.CustomOAuth2User;
import com.quizbox.domain.auth.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = null;
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie:
             cookies) {
            if (cookie.getName().equals("Authorization")) {
                authorization = cookie.getValue();
            }
        }

        //Authorization 헤더 검증
        if (authorization == null) {
            System.out.println("token null");
            filterChain.doFilter(request, response);

            // 조건이 해당되면 메소드 종료
            return;
        }

        // 토큰
        String token = authorization;

        if (jwtUtil.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            return;
        }

        // 토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);


        // userDTO를 생성하여 값 set
        UserDTO userDTO = new UserDTO(role, null, username);

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        //스프링 시큐리티 인증 토큰 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
