package com.carson.vboot.core.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/**
 * created by Nicofh on 2020-03-08
 * 配置MBP自动插入时间或者创建人
 */

@Configuration
@Slf4j
public class MBPAutoHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication) {
            // 如果用户存在，新增数据时候自动插入用户名
            UserDetails user = (UserDetails) authentication.getPrincipal();
            this.setFieldValByName("createBy", user.getUsername(), metaObject);
        }

        // 插入创建时间
        this.setFieldValByName("createTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication) {
            // 如果用户存在，更新数据时候自动插入用户名
            UserDetails user = (UserDetails) authentication.getPrincipal();
            this.setFieldValByName("updateBy", user.getUsername(), metaObject);
        }
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
