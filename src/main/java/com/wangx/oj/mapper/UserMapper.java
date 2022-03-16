package com.wangx.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangx.oj.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<User> findAll();
    void InsertOne(User User);
    User findUserByUserName(String username);
    User findUserById(User User);

}
