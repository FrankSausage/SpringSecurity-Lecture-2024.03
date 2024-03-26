package com.example.springSecurity.config;

import com.example.springSecurity.service.MyOAuth2UserService;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired private MyOAuth2UserService myOAuth2UserService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(auth -> auth.disable())             // 괄호 안에 람다함수를 사용해야 함
                .headers(x -> x.frameOptions(y -> y.disable()))     //CK Editor image upload
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()     // redirect 전부 접근 허용
                        .requestMatchers("/user/register",
                                "/img/**", "/css/**", "/js/**", "/error/**").permitAll()    // 이하 요청 전부 접근 허용
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")     // "ADMIN" 권한 소유자만 허용
                        .anyRequest().authenticated()
                )
                .formLogin(auth -> auth
                        // login Form - 로그인 화면 제공
                        .loginPage("/user/login")
                        // Spring Security 가 값을 받아감, UserDetailsService 구현 객체에서 값 처리
                        .loginProcessingUrl("/user/login")
                        .usernameParameter("uid")
                        .passwordParameter("pwd")
                        // 로그인 후 해야 할 일 처리, ex) Session 설정, 오늘의 메세지, etc...
                        .defaultSuccessUrl("/user/loginSuccess", true)
                        .permitAll()
                )
                .oauth2Login(auth -> auth
                        .loginPage("/user/login")
                        // 소셜 로그인이 완룐된 이후의 후처리
                        // 1. 코드받기(인증), 2. 액세스 토큰(권한), 3. 사용자 프로필 정보를 가져옴
                        // 4. 3에서 받은 정보를 토대로 DB에 없으면 가입을 시켜줌
                        // 5. 프로바이더가 제공하는 정보 + 추가정보 수집 (주소, etc...)
                        .userInfoEndpoint(user -> user.userService(myOAuth2UserService))
                        .defaultSuccessUrl("/user/loginSuccess", true)
                )
                .logout(auth -> auth
                        .logoutUrl("/user/logout")
                        .invalidateHttpSession(true)                         // 로그아웃 시 세션 초기화
                        .deleteCookies("JSESSIONID")    // 로그아웃 시 쿠키 삭제
                        .logoutSuccessUrl("/user/login")                     // 로그아웃 후 보낼 페이지
                )
        ;
        return http.build();
    }
}
