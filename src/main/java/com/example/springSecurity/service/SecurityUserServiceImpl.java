package com.example.springSecurity.service;

import com.example.springSecurity.dao.SecurityUserDao;
import com.example.springSecurity.entity.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityUserServiceImpl implements SecurityUserService {
    //    private SecurityUserDao suDao;
//    @Autowired
//    public SecurityUserServiceImpl (SecurityUserDao suDao){
//        this.suDao = suDao;
//    }
    private final SecurityUserDao suDao;

    @Override
    public SecurityUser getUserByUid(String uid) {
        return suDao.getUserByUid(uid);
    }

    @Override
    public List<SecurityUser> getSecurityUserList(int page) {
        int offset = (page - 1) * COUNT_PER_PAGE;
        return suDao.getSecurityUserList(COUNT_PER_PAGE, offset);
    }

    @Override
    public int getSecurityUserCount() {
        return suDao.getSecurityUserCount();
    }

    @Override
    public void insertSecurityUser(SecurityUser user) {
        suDao.insertSecurityUser(user);
    }

    @Override
    public void updateSecurityUser(SecurityUser user) {
        suDao.updateSecurityUser(user);
    }

    @Override
    public void deleteSecurityUser(String uid) {
        suDao.deletedSecurityUser(uid);
    }
}
