package com.quizbox.domain.auth.dto;

public record UserDTO(
        String role,
        String name,
        String username
) {
}
