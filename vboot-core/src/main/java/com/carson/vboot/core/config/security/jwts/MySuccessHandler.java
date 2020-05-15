package com.carson.vboot.core.config.security.jwts;

import com.carson.vboot.core.common.utils.ResponseUtil;
import com.carson.vboot.core.config.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * created by Nicofh on 2020-03-14
 * 认证成功处理,生成token
 */
@Slf4j
@Component
public class MySuccessHandler implements AuthenticationSuccessHandler {


    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {


        // 获取登陆用户名
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        // 获取已授权权限
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) ((UserDetails) authentication.getPrincipal()).getAuthorities();
        List<String> list = new ArrayList<>();
        for (GrantedAuthority g : authorities) {
            list.add(g.getAuthority());
        }

        // 获取了token
        String token = securityUtil.getTokenAndSetAuthority(username, list);

        // 等路成功，返回token
        ResponseUtil.out(response, ResponseUtil.resultMap(true, 200, "登录成功", token));
    }
}

