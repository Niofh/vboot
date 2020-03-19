package com.carson.vboot.core.config.security.jwts;

import com.carson.vboot.core.common.constant.CommonConstant;
import com.carson.vboot.core.common.utils.ResponseUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * 退出登录成功操作
 */
@Slf4j
@Component
public class MyLogoutSuccessHandler  implements LogoutSuccessHandler {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("退出登录成功");

        String token = httpServletRequest.getHeader(CommonConstant.HEADER);

        String s = stringRedisTemplate.opsForValue().get(CommonConstant.TOKEN_PRE + token);

        if(s!=null){
            HashMap hashMap = new Gson().fromJson(s, HashMap.class);
            String username = (String) hashMap.get("username");
            // 删除缓存
            stringRedisTemplate.delete(CommonConstant.USER_TOKEN+username);

            // 删除token
            stringRedisTemplate.delete(CommonConstant.TOKEN_PRE+token);
        }


        ResponseUtil.out(httpServletResponse, ResponseUtil.resultMap(true, 200, "退出登录成功"));
    }
}
