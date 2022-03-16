package com.wangx.oj.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangx.oj.common.Result;
import com.wangx.oj.entity.User;
import com.wangx.oj.security.MyUserDetails;
import com.wangx.oj.utils.IPUtils;
import com.wangx.oj.utils.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;



    public JWTAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 从输入流中获取到登录信息
        try {
            User loginUser = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(),
                    loginUser.getPassword()));
        } catch (JsonParseException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // 成功验证后调用的方法
    // 如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        MyUserDetails jwtUser = (MyUserDetails) authResult.getPrincipal();
        String ip = IPUtils.getIpAddress(request);
        Map<String, Object> map = new HashMap<>();
        map.put("ip", ip);
        // 因为在JwtUser中存了权限信息，可以直接获取，由于只有一个角色就这么干了
        Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();

        /*
         查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
         所以就是JwtUser啦
         返回创建成功的token
         但是这里创建的token只是单纯的token
         按照jwt的规定，最后请求的格式应该是 `Bearer token`
        */
        // 根据用户名，角色创建token
        logger.info(authorities.toString());
        Set authoritiesSet = JwtTokenUtils.authoritiesToArray(authorities);
        HashMap<String, Object> authorMap = new HashMap<>();
        log.info(JSON.toJSONString(authoritiesSet));

        authorMap.put("rol", JSON.toJSONString(authoritiesSet));

        String token = JwtTokenUtils.createToken(jwtUser.getUsername(), authorMap, true);
        response.setContentType("application/json;charset=utf-8");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("token", "Bearer "+token);
        log.info("用户{}登录成功，信息已保存至redis", jwtUser.getUsername());
        response.getWriter().write(JSON.toJSONString(Result.success(map1)));
    }
}
