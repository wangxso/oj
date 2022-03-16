package com.wangx.oj.security;

import com.alibaba.fastjson.JSON;
import com.wangx.oj.common.CodeMsg;
import com.wangx.oj.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 401异常处理
 * 用户未登录
 */
@Component
@Slf4j
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info("no login");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.fail(CodeMsg.NOT_LOGIN)));
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
    }
}
