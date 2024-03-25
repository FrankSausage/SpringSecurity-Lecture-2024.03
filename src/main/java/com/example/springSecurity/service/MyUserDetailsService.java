package com.example.springSecurity.service;

import com.example.springSecurity.entity.MyUserDetails;
import com.example.springSecurity.entity.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final SecurityUserService sSvc;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser user = sSvc.getUserByUid(username);

        if(user != null){
            log.info("Login : " + user.getUid() + ", " + user.getUname());
            return new MyUserDetails(user);
        }
        return null;
    }
}
