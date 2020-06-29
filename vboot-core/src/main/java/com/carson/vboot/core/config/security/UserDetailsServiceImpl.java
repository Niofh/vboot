package com.carson.vboot.core.config.security;

import com.carson.vboot.core.service.UserService;
import com.carson.vboot.core.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * created by Nicofh on 2020-03-14
 * 通过这个方法根据用户名获取用户信息
 */
@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;


    /**
     * 如果用户是null，会报异常，失败处理器FailHandler会返回对应信息
     *
     * @param  name 接受是字符串
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
//        String flagKey = "loginFailFlag:" + username;
//        String value = redisTemplate.opsForValue().get(flagKey);
//        Long timeRest = redisTemplate.getExpire(flagKey, TimeUnit.MINUTES);
//        if (StrUtil.isNotBlank(value)) {
//            //超过限制次数
//            throw new VbootException(500, "登录错误次数超过限制，请" + timeRest + "分钟后再试");
//        }
         // name 可以存放用户名+什么端登录

        // 查询当前用户是否存在
        log.info("登录username为： {}", name);
        UserVO user = userService.findByUsername(name);

        // todo 在这里增加查询会员的用户？？？

        log.info("uservo {}", user);

        return new SecurityUserDetails(user);
    }
}
