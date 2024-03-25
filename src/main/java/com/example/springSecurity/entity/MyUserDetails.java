package com.example.springSecurity.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// Spring Security가 로그인 POST 요청을 낚아채서 로그인을 진행시키는 코드
// 로컬 로그인 - UserDetails 구현
// 소셜 로그인 - OAuth2User 구현
@RequiredArgsConstructor
public class MyUserDetails implements UserDetails {
    private final SecurityUser user;

    // 사용자의 권한을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPwd();
    }

    @Override
    public String getUsername() {
        return user.getUid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getIsDeleted() == 0;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
