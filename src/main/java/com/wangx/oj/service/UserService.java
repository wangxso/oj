package com.wangx.oj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sun.org.apache.bcel.internal.classfile.Code;
import com.wangx.oj.common.CodeMsg;
import com.wangx.oj.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<User> findAll();
    void register(User User);
    void update(User User);
    User findUserById(String uid);
    User findUserByUserName(String username);
    IPage<User> findUserPagination(Integer page, Integer pageSize);
    Integer getUserCount();
    void deleteUser(String uid);
    Map findUserPassAndTotalSubmission(String uid);
    CodeMsg changePassword(User user, String oldPassword, String newPassword);
}
