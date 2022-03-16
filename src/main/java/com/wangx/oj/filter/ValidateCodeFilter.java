package com.wangx.oj.filter;

import com.alibaba.druid.util.StringUtils;
import com.wangx.oj.common.ValidateCodeException;
import com.wangx.oj.utils.RedisUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@Data
public class ValidateCodeFilter extends OncePerRequestFilter {
    @Autowired
    RedisUtils redisUtils;

    private AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(StringUtils.equals("/user/login", httpServletRequest.getRequestURI())
                && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(), "post")) {

            String code = httpServletRequest.getParameter("code");
            logger.info("进入验证验证码逻辑" + code);

            try {
                checkCode(code, httpServletRequest);
            } catch (ValidateCodeException e){
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }

        }
        // 3. 校验通过，就放行
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    void checkCode(String code, HttpServletRequest req) throws ValidateCodeException {
        String ip = req.getRemoteAddr();
        String verificationCodeIn = (String) redisUtils.get(ip+"_captcha_code");
        redisUtils.remove(ip+"_captcha_code");
        if (StringUtils.isEmpty(verificationCodeIn) || !verificationCodeIn.equals(code)) {
            throw  new ValidateCodeException("验证码错误");
        }
    }
}
