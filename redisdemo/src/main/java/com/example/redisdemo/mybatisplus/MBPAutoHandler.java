package com.example.redisdemo.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

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

        // 插入创建时间
        this.setFieldValByName("createTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
