package com.quizbox.domain.auth.dto;

import java.util.Map;

public record GoogleRes(
        Map<String, Object> attribute
) implements OAuth2Res {
    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
