package com.wangx.oj.security;

import com.alibaba.fastjson.JSON;
import com.wangx.oj.common.CodeMsg;
import com.wangx.oj.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无权访问
 */
@Component
@Slf4j
public class LoginDenyHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        log.info("login deny");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.fail(CodeMsg.PERMIT_DENY)));
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
    }
}
