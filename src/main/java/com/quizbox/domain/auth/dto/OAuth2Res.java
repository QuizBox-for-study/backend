package com.quizbox.domain.auth.dto;

public interface OAuth2Res{
    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();

}
