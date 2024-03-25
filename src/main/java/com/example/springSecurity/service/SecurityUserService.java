package com.example.springSecurity.service;

import com.example.springSecurity.entity.SecurityUser;

import java.util.List;

public interface SecurityUserService {
    public static final int COUNT_PER_PAGE = 10;

    SecurityUser getUserByUid(String uid);

    List<SecurityUser> getSecurityUserList(int page);

    int getSecurityUserCount();

    void insertSecurityUser(SecurityUser user);

    void updateSecurityUser(SecurityUser user);

    void deleteSecurityUser(String uid);

}
