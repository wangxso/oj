package com.wangx.oj.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangx.oj.common.CodeMsg;
import com.wangx.oj.common.Result;
import com.wangx.oj.entity.User;
import com.wangx.oj.service.UserService;

import com.wangx.oj.utils.OSSUtils;
import com.wangx.oj.utils.RedisUtils;
import com.wangx.oj.utils.SmsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RedisUtils redisUtils;

    @Value("refreshToken-expireTime")
    String refreshTokenExpireTime;

    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private OSSUtils ossUtils;

    @RequestMapping("/findAll")
    public Result findAll(){
        return Result.success(userService.findAll());
    }



    @RequestMapping("/register")
    public Result register(@RequestBody User user) {
        if (!smsUtils.checkSmsCode(user.getCode(), user.getTel())) return Result.fail(CodeMsg.VERITY_CODE_ERROR);
        if (redisUtils.hasKey("userCount")){
            redisUtils.remove("userCount");
        }
        log.info("Register:\t" + user.toString());
        userService.register(user);
        return Result.success(null);
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public Result findUserByUsername(@PathVariable("username") String username){
        User userByUserName = userService.findUserByUserName(username);
        return Result.success(userByUserName);
    }

    @RequestMapping("/findUserById")
    public Result findUserById(@RequestBody User User) {
        return Result.success(userService.findUserById(User.getUid()));
    }


    @RequestMapping(value = "/{page}/{pageSize}", method = RequestMethod.GET)
    public Result findUserPagination(@PathVariable Integer page, @PathVariable Integer pageSize) {
        IPage<User> userPagination = userService.findUserPagination(page, pageSize);
        return Result.success(userPagination);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Result updateUser(@RequestBody User user) {
        userService.update(user);
        return Result.success("更新成功");
    }

    @RequestMapping(value = "count", method = RequestMethod.GET)
    public Result getUserCount(){
        Integer userCount = 0;
        if (redisUtils.hasKey("userCount")){
             userCount = (Integer) redisUtils.get("userCount");
        } else {
            userCount = userService.getUserCount();
            redisUtils.set("userCount", userCount);
        }
        return Result.success(userCount);
    }

    @RequestMapping(value = "/{uid}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String uid){
        userService.deleteUser(uid);
        return Result.success("success");
    }

    @RequestMapping(value = "/deletes", method = RequestMethod.POST)
    public Result deleteList(@RequestBody List<User> users){
        for (User user: users){
            userService.deleteUser(user.getUid());
        }
        return Result.success("success");
    }

    @RequestMapping(value = "/statics/submission/{uid}", method = RequestMethod.GET)
    public Result findUserSubmissionPassAndTotal(@PathVariable String uid) {
        Map res = userService.findUserPassAndTotalSubmission(uid);
        return Result.success(res);
    }

    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    public Result uploadAvatar(@RequestParam("file")MultipartFile file) {
        try {
            String url = ossUtils.uploadFile(file); //调用OSS工具类
            return Result.success(url);
        }
        catch (Exception e) {
            log.info(e.getMessage());
            return Result.fail(CodeMsg.UPLOAD_FAIL);
        }
    }

    @RequestMapping(value = "/avatar", method = RequestMethod.PUT)
    public Result updateAvatar(@RequestBody User user) {
        userService.update(user);
        return Result.success("更新成功");
    }


    @RequestMapping(value = "/change/password/{oldPassword}/{newPassword}", method = RequestMethod.PUT)
    public Result changePassword(@RequestBody User user, @PathVariable String oldPassword, @PathVariable String newPassword) {
        CodeMsg codeMsg = userService.changePassword(user, oldPassword, newPassword);
        if (codeMsg.equals(CodeMsg.SUCCESS)) {
            return Result.success(codeMsg);
        } else {
            return Result.fail(codeMsg);
        }
    }
}
