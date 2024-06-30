package com.quizbox.domain.auth.service;

import com.quizbox.domain.auth.dto.*;
import com.quizbox.domain.user.domain.User;
import com.quizbox.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

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


        String username = oAuth2Res.getProvider() + "." + oAuth2Res.getProviderId();

        User existUser = userRepository.findByUsername(username);

        if (existUser == null) {

            User user = User.builder()
                    .username(username)
                    .email(oAuth2Res.getEmail())
                    .name(oAuth2Res.getName())
                    .role("ROLE_USER")
                    .build();

            userRepository.save(user);

            UserDTO userDTO = new UserDTO(username, oAuth2Res.getName(), "ROLE_USER");

            return new CustomOAuth2User(userDTO);

        } else {

            existUser.updateEmail(oAuth2Res.getEmail());
            existUser.updateName(oAuth2Res.getName());

            userRepository.save(existUser);

            UserDTO userDTO = new UserDTO(username, oAuth2Res.getName(), "ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }





    }
}
