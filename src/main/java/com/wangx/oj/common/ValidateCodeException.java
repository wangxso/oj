package com.wangx.oj.common;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码输入异常
 *
 * @author lixin
 */
public class ValidateCodeException extends AuthenticationException {
    public ValidateCodeException(String msg, Throwable t) {
        super(msg, t);
    }
    public ValidateCodeException(String msg) {
        super(msg);
    }
    public ValidateCodeException() {
        super("validate code check fail!");
    }
}
