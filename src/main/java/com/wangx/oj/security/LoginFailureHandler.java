package com.wangx.oj.security;

import com.alibaba.fastjson.JSON;
import com.wangx.oj.common.CodeMsg;
import com.wangx.oj.common.Result;
import com.wangx.oj.common.ValidateCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter out = response.getWriter();
        Result result;
        log.info("login fail");
        if (ex instanceof UsernameNotFoundException || ex instanceof BadCredentialsException) {
            result = Result.fail(CodeMsg.PASSWORD_ERROR);
        } else if (ex instanceof DisabledException) {
            result = Result.fail(CodeMsg.ACCOUNT_LOCKED);
        }
        else if (ex instanceof ValidateCodeException){
          result = Result.fail(CodeMsg.VERITY_CODE_ERROR);
        } else {
            result = Result.success(ex);
        }
        out.write(JSON.toJSONString(result));
        out.flush();
        out.close();
    }
}
