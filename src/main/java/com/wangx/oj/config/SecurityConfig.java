package com.wangx.oj.config;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.bcel.internal.classfile.Code;
import com.wangx.oj.common.CodeMsg;
import com.wangx.oj.common.Result;
import com.wangx.oj.filter.JWTAuthenticationFilter;
import com.wangx.oj.filter.JWTAuthorizationFilter;
import com.wangx.oj.filter.ValidateCodeFilter;
import com.wangx.oj.security.*;
import com.wangx.oj.service.UserService;
import com.wangx.oj.utils.MyPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

//:AOP
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // 控制权限注解
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MyPasswordEncoder myPasswordEncoder;
    @Autowired
    MyCustomUserService myCustomUserService;
    @Autowired
    ValidateCodeFilter validateCodeFilter;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private LoginDenyHandler loginDenyHandler;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private JsonAuthenticationEntryPoint jsonAuthenticationEntryPoint;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Autowired
    MyCustomUserService userService;

    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        validateCodeFilter.setAuthenticationFailureHandler(loginFailureHandler);
        http.cors().and().csrf().disable()
                .httpBasic().authenticationEntryPoint(jsonAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                // 在验证用户名密码之前添加验证验证码的拦截器
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                // 登录调用的地址
                .formLogin().loginProcessingUrl("/login")
                // 登录成功处理
                .successHandler(loginSuccessHandler)
                // 登录失败处理
                .failureHandler(loginFailureHandler)
                .and()
                // 权限不足,即403时跳转页面
                .exceptionHandling().accessDeniedHandler(loginDenyHandler).authenticationEntryPoint(new JsonAuthenticationEntryPoint())
                .and()
                //登出地址和的航处成功处理类
                .logout().logoutUrl("/logout").logoutSuccessHandler(myLogoutSuccessHandler)
                .clearAuthentication(true)
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                // 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 记住我
        http.rememberMe().rememberMeParameter("remember-me")
                .userDetailsService(userService).tokenValiditySeconds(1000);
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        //对默认的UserDetailsService进行覆盖
        authenticationProvider.setUserDetailsService(myCustomUserService);
        authenticationProvider.setPasswordEncoder(myPasswordEncoder);
        return authenticationProvider;
    }
}
