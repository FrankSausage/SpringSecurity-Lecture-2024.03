package com.example.springSecurity.dao;

import com.example.springSecurity.entity.SecurityUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SecurityUserDao {

    @Select("select * from securityUser where uid=#{uid}")
    SecurityUser getUserByUid(String uid);

    @Select("select * from securityUser where isDeleted=0 order by regDate desc limit #{count} offset #{offset}")
    List<SecurityUser> getSecurityUserList(int count, int offset);

    @Select("select count(uid) from securityUser where isDeleted=0")
    int getSecurityUserCount();

    @Insert("insert into securityUser values(" +
            "#{uid}, #{pwd}, #{uname}, #{email}, DEFAULT, DEFAULT, #{picture}, #{provider}, DEFAULT)")
    void insertSecurityUser(SecurityUser user);

    @Update("update securityUser set " +
            "pwd=#{pwd}, uname=#{uname}, email=#{email}, picture=#{picture} where uid=#{uid}")
    void updateSecurityUser(SecurityUser user);

    @Update("update securityUser set isDeleted=1 where uid=#{uid}")
    void deletedSecurityUser(String uid);
}
