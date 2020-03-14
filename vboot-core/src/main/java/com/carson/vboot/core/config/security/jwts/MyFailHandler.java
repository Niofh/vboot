package com.carson.vboot.core.config.security.jwts;

import cn.hutool.core.util.StrUtil;
import com.carson.vboot.core.common.constant.CommonConstant;
import com.carson.vboot.core.common.utils.ResponseUtil;
import com.carson.vboot.core.properties.TokenProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * created by Nicofh on 2020-03-14
 * 登陆失败处理
 */
@Component
@Slf4j
public class MyFailHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    // 限制次数前缀
    private String LOGIN_LIMIT = "loginTimeLimit:";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            String username = request.getParameter("username");

            this.recordLoginTime(username);

            String key = LOGIN_LIMIT + username;
            String value = (String) redisTemplate.opsForValue().get(key);
            if (StrUtil.isBlank(value)) {
                value = "0";
            }
            //获取已登录错误次数
            int loginFailTime = Integer.parseInt(value);
            int restLoginTime = tokenProperties.getLoginTimeLimit() - loginFailTime;
            log.info("用户" + username + "登录失败，还有" + restLoginTime + "次机会");
            if (restLoginTime <= 3 && restLoginTime > 0) {
                ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "用户名或密码错误，还有" + restLoginTime + "次尝试机会"));
            } else if (restLoginTime <= 0) {
                ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "登录错误次数超过限制，请" + tokenProperties.getLoginAfterTime() + "分钟后再试"));
            } else {
                ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "用户名或密码错误"));
            }
        } else if (e instanceof DisabledException) {
            ResponseUtil.out(response, ResponseUtil.resultMap(false,500,"账户被禁用，请联系管理员"));
        }else {
            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "账户异常，请联系管理员"));
        }
    }

    /**
     * 判断用户登陆错误次数
     */
    public boolean recordLoginTime(String username) {

        String key = LOGIN_LIMIT + username;
        String flagKey = CommonConstant.LOGIN_FAILFLAG + username;
        String value = (String) redisTemplate.opsForValue().get(key);
        if (StrUtil.isBlank(value)) {
            value = "0";
        }
        //获取已登录错误次数
        int loginFailTime = Integer.parseInt(value) + 1;
        redisTemplate.opsForValue().set(key, String.valueOf(loginFailTime), tokenProperties.getLoginAfterTime(), TimeUnit.MINUTES);
        if (loginFailTime >= tokenProperties.getLoginTimeLimit()) {
            // 如果超过登录密码次数，就要限制登录多少分钟
            redisTemplate.opsForValue().set(flagKey, "fail", tokenProperties.getLoginAfterTime(), TimeUnit.MINUTES);
            return false;
        }
        return true;
    }

}



