package com.example.springSecurity.service;

import com.example.springSecurity.entity.MyUserDetails;
import com.example.springSecurity.entity.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyOAuth2UserService extends DefaultOAuth2UserService {
    private final SecurityUserService securityUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // Provider 로부터 받은 userRequest 데이터에 대해서 후처리하는 메소드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String uid, email, uname, picture;
        String hashedPwd = bCryptPasswordEncoder.encode("Social Login");
        SecurityUser securityUser = null;
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes(): " + oAuth2User.getAttributes());
        String provider = userRequest.getClientRegistration().getRegistrationId();

        switch (provider) {
            case "google": // id(숫자), name, picture, email
                String gooid = oAuth2User.getAttribute("sub");
                uid = provider + "_" + gooid;
                securityUser = securityUserService.getUserByUid(uid);
                if (securityUser == null) {         // 가입이 안되어 있으면 신규 가입
                    uname = oAuth2User.getAttribute("name");
                    uname = (uname == null) ? "google_user" : uname;
                    email = oAuth2User.getAttribute("email");
                    picture = oAuth2User.getAttribute("picture");
                    securityUser = SecurityUser.builder()
                            .uid(uid).pwd(hashedPwd).uname(uname).email(email)
                            .provider(provider).picture(picture)
                            .build();
                    securityUserService.insertSecurityUser(securityUser);
                    securityUser = securityUserService.getUserByUid(uid);
                    log.info("New Account From 'Google'");
                }
                break;
            case "github": // id(숫자), name, picture, email
                int gitid = oAuth2User.getAttribute("id");
                uid = provider + "_" + gitid;
                securityUser = securityUserService.getUserByUid(uid);
                if (securityUser == null) {         // 가입이 안되어 있으면 신규 가입
                    uname = oAuth2User.getAttribute("name");
                    uname = (uname == null) ? "github_user" : uname;
                    email = oAuth2User.getAttribute("email");
                    picture = oAuth2User.getAttribute("avatar_url" );
                    securityUser = SecurityUser.builder()
                            .uid(uid).pwd(hashedPwd).uname(uname).email(email).provider(provider).picture(picture)
                            .build();
                    securityUserService.insertSecurityUser(securityUser);
                    securityUser = securityUserService.getUserByUid(uid);
                    log.info("New Account From 'GitHub'");
                }
                break;
            case "naver": // uid(문자), nickname, picture, email
                Map<String, Object> response = (Map) oAuth2User.getAttribute("response");
                String nid = (String) response.get("id");
                uid = provider + "_" + nid;
                securityUser = securityUserService.getUserByUid(uid);
                if (securityUser == null) {         // 가입이 안되어 있으면 신규 가입
                    uname = (String) response.get("nickname");
                    uname = (uname == null) ? "naver_user" : uname;
                    email = (String) response.get("email");
                    picture = (String) response.get("profile_image");
                    securityUser = SecurityUser.builder()
                            .uid(uid).pwd(hashedPwd).uname(uname).email(email)
                            .provider(provider).picture(picture)
                            .build();
                    securityUserService.insertSecurityUser(securityUser);
                    securityUser = securityUserService.getUserByUid(uid);
                    log.info("New Account From 'Naver'");
                }
                break;
            case "kakao": // id(숫자) ,name, , picture, email
                long kid = oAuth2User.getAttribute("id");
                uid = provider + "_" + kid;
                securityUser = securityUserService.getUserByUid(uid);
                if (securityUser == null) {         // 가입이 안되어 있으면 신규 가입
                    Map<String, String> properties = (Map) oAuth2User.getAttribute("properties");
                    Map<String, Object> account = (Map) oAuth2User.getAttribute("kakao_account");
                    uname = (String) properties.get("nickname");
                    uname = (uname == null) ? "kakao_user" : uname;
                    email = (String) account.get("email");
                    email = (email == null) ? "noKakaoEmail@gmail.com" : email;
                    email = "noKakaoEmail@gmail.com";
                    picture = (String) properties.get("profile_image");
                    securityUser = SecurityUser.builder()
                            .uid(uid).pwd(hashedPwd).uname(uname).email(email)
                            .provider(provider).picture(picture)
                            .build();
                    securityUserService.insertSecurityUser(securityUser);
                    securityUser = securityUserService.getUserByUid(uid);
                    log.info("New Account From 'Kakao'");
                }
                break;
            case "facebook": // id(String), name, email
                String fid = oAuth2User.getAttribute("id");
                uid = provider + "_" + fid;
                securityUser = securityUserService.getUserByUid(uid);
                if (securityUser == null) {
                    uname = oAuth2User.getAttribute("name");
                    uname = (uname == null) ? "facebook_user" : uname;
                    email = oAuth2User.getAttribute("email");
                    securityUser = SecurityUser.builder()
                            .uid(uid).pwd(hashedPwd).uname(uname).email(email).provider(provider)
                            .build();
                    securityUserService.insertSecurityUser(securityUser);
                    securityUser = securityUserService.getUserByUid(uid);
                    log.info("New Account From 'facebook'");
                }
                break;
        }
        return new MyUserDetails(securityUser, oAuth2User.getAttributes());
    }
}
