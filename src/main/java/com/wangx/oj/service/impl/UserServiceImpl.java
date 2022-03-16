package com.wangx.oj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangx.oj.common.CodeMsg;
import com.wangx.oj.common.Result;
import com.wangx.oj.entity.Submission;
import com.wangx.oj.entity.SubmissionStatics;
import com.wangx.oj.entity.User;
import com.wangx.oj.mapper.SubmissionMapper;
import com.wangx.oj.mapper.UserMapper;
import com.wangx.oj.service.UserService;
import com.wangx.oj.utils.OSSUtils;
import com.wangx.oj.utils.RedisUtils;
import com.wangx.oj.utils.UUIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private SubmissionMapper submissionMapper;

    private static final String USER_PASS_TOTAL = "user_pass_total";

    @Override
    public List<User> findAll() {
        List<User> UserList = userMapper.findAll();
        return UserList;
    }

    @Override
    public void register(User user) {
        // 设置用户id
        user.setUid(UUIDGenerator.getUUID());
        // md5 加密
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        // 初始为普通用户
        user.setAuthority(0);
        // 设置初始分数为1500分
        user.setLevel(1500);
        user.setPassword(password);
        log.info(user.toString());
        userMapper.InsertOne(user);
        return;
    }

    @Override
    public void update(User user) {
        userMapper.updateById(user);
        return;
    }

    @Override
    public User findUserById(String uid) {
        User user = userMapper.selectById(uid);
        return user;
    }

    @Override
    public User findUserByUserName(String username) {
        User userByUserName = userMapper.findUserByUserName(username);
        return userByUserName;
    }

    @Override
    public IPage<User> findUserPagination(Integer page, Integer pageSize) {
        Page<User> userPage = new Page<>(page, pageSize);//参数一是当前页，参数二是每页个数
        IPage<User> userIPage = userMapper.selectPage(userPage, null);
        return userIPage;
    }

    @Override
    public Integer getUserCount() {
        Integer count = userMapper.selectCount(null);
        return count;
    }

    @Override
    public void deleteUser(String uid) {
        userMapper.deleteById(uid);
    }

    @Override
    public Map findUserPassAndTotalSubmission(String uid) {
        if (!redisUtils.hasKey(USER_PASS_TOTAL) || redisUtils.hmGet(USER_PASS_TOTAL, uid) == null) {
            QueryWrapper<Submission> passWrapper = new QueryWrapper<>();
            passWrapper.lambda().eq(Submission::getUid, uid).eq(Submission::getResult, 1);
            Integer pass = submissionMapper.selectCount(passWrapper);
            QueryWrapper<Submission> totalWrapper = new QueryWrapper<>();
            totalWrapper.lambda().eq(Submission::getUid, uid);
            Integer total = submissionMapper.selectCount(totalWrapper);
            Map<String, Integer> map = new HashMap<>();
            map.put("pass", pass);
            map.put("total", total);
            redisUtils.hmSet(USER_PASS_TOTAL, uid, map);
        }
        Map<String, Integer> res = (Map<String, Integer>) redisUtils.hmGet(USER_PASS_TOTAL, uid);
        return res;
    }

    @Override
    public CodeMsg changePassword(User user, String oldPassword, String newPassword) {
        User origin = userMapper.selectById(user.getUid());
        String oldPasswordMd5 = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        log.info("user: " + user.getUsername() + ", change password to :" + newPassword);
        if (oldPasswordMd5.equals(origin.getPassword())) {
            origin.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
            update(origin);
            return CodeMsg.SUCCESS;
        }
        return CodeMsg.PASSWORD_ERROR;
    }
}
