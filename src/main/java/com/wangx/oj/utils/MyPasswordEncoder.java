package com.wangx.oj.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class MyPasswordEncoder implements PasswordEncoder {
    /**
     * 对密码进行加密
     * @param str 密码
     * @return 加密后的密码
     */
    public static String encrypt(String str){
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    @Override
    public String encode(CharSequence charSequence) {
        String s = charSequence.toString();
        return encrypt(s);
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return encode(charSequence).equals(s);
    }
}
