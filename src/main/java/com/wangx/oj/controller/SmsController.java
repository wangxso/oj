package com.wangx.oj.controller;

import com.wangx.oj.common.CodeMsg;
import com.wangx.oj.common.Result;
import com.wangx.oj.utils.RedisUtils;
import com.wangx.oj.utils.SmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/sms")
public class SmsController {
    @Autowired
    private SmsUtils smsUtils;
    @Autowired
    private RedisUtils redisUtils;
    @RequestMapping(value = "/{phone}", method = RequestMethod.GET)
    public Result sendSms(@PathVariable String phone) {
        if (phone == null || "".equals(phone)) return Result.fail(CodeMsg.MOBILE_EMPTY);
        if (!validateMobilePhone(phone)) return Result.fail(CodeMsg.MOBILE_ERROR);
        Object sms = redisUtils.hmGet("sms", phone);
        // 验证码已发送，防止重复发生
        if (sms!=null) return Result.fail(CodeMsg.VERITY_CODE_IN_TIME);
        smsUtils.send(phone);
        return Result.success("发送成功");
    }

    /**
     *  正则：手机号（简单）, 1字头＋10位数字即可.
     * @param in
     * @return
     */
    public  boolean validateMobilePhone(String in) {
        Pattern pattern = Pattern.compile("^[1]\\d{10}$");
        return pattern.matcher(in).matches();
    }

}
