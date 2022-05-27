package com.mytype.douyin.dao;

import com.mytype.douyin.entity.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {

    User selectById(int id);

    User selectByName(String username);

    int insertUser(User user);

    int updateHeader(int id, String avatar);

    int updatePassword(int id, String password);

}
