package com.quizbox.domain.auth.service;

import com.quizbox.domain.auth.dto.GoogleRes;
import com.quizbox.domain.auth.dto.NaverRes;
import com.quizbox.domain.auth.dto.OAuth2Res;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("oAuth2User = " + oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Res oAuth2Res = null;

        if (registrationId.equals("naver")) {

            oAuth2Res = new NaverRes(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {

            oAuth2Res = new GoogleRes(oAuth2User.getAttributes());
        } else {

            return null;
        }

        // 로그인 완료 후 처리할 로직은 추후 작성
    }
}
