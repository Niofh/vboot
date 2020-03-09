package com.carson.vboot.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.dao.mapper.UserDao;
import com.carson.vboot.core.entity.User;
import com.carson.vboot.core.service.UserService;
import com.carson.vboot.core.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by Nicofh on 2020-03-08
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public VbootBaseDao<User> getBaseDao() {
        return userDao;
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @Override
    public Result<Object> saveUser(User user) {
        String username = user.getUsername();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User userObj = userDao.selectOne(queryWrapper);

        if (userObj != null) {
            return ResultUtil.error("用户名已存在");
        }


        if (StrUtil.isNotBlank(user.getMobile())) {
            queryWrapper.clear(); // 清空sql判断语句
            queryWrapper.eq("mobile", user.getMobile());
            User mUser = userDao.selectOne(queryWrapper);
            if (null != mUser) {
                return ResultUtil.error("手机号已存在!");
            }
        }


        userDao.insert(user);

        return ResultUtil.success("添加成功!");
    }
}
