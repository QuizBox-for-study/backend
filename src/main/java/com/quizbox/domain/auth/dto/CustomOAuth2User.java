package com.quizbox.domain.auth.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public record CustomOAuth2User(
        UserDTO userDTO
) implements OAuth2User {
    public CustomOAuth2User(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDTO.role();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return userDTO().name();
    }

    public String getUsername() {
        return userDTO.username();
    }
}
