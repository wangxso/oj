package com.wangx.oj.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangx.oj.entity.User;
import com.wangx.oj.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class MyCustomUserService implements UserDetailsService {
    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username",username));
        log.info("user login: " + user.toString());
        assert user!= null;
        MyUserDetails myUserDetails = new MyUserDetails();
        myUserDetails.setPassword(user.getPassword());
        myUserDetails.setUsername(user.getUsername());
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getAuthority() == 0){
             authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        } else if (user.getAuthority() == 1){
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        myUserDetails.setAuthorities(authorities);
        return myUserDetails;
    }
}
