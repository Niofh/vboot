package com.carson.vboot.core.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.enums.ExceptionEnums;
import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.dao.mapper.UserDao;
import com.carson.vboot.core.entity.User;
import com.carson.vboot.core.exception.VbootException;
import com.carson.vboot.core.service.UserService;
import com.carson.vboot.core.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    public Integer save(User user) {
        String username = user.getUsername();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User userObj = userDao.selectOne(queryWrapper);

        if (userObj != null) {
            throw new VbootException(ExceptionEnums.USER_NAME_EXIST);
        }

        // 验证手机号
        if (StrUtil.isNotBlank(user.getMobile())) {
            queryWrapper.clear(); // 清空sql判断语句
            queryWrapper.eq("mobile", user.getMobile());
            User mUser = userDao.selectOne(queryWrapper);
            if (null != mUser) {
                throw new VbootException(ExceptionEnums.USER_MOBILE_EXIST);
            }
        }

        // 验证邮箱
        if (StrUtil.isNotBlank(user.getEmail())) {
            queryWrapper.clear(); // 清空sql判断语句
            queryWrapper.eq("email", user.getEmail());
            User mUser = userDao.selectOne(queryWrapper);
            if (null != mUser) {
                throw new VbootException(ExceptionEnums.USER_MOBILE_EXIST);
            }
        }


        return userDao.insert(user);
    }

    @Override
    public IPage<User> getUserByPage(PageBo pageBo, User user) {
        Page<User> usersPage = new Page<>(pageBo.getPageIndex(), pageBo.getPageSize());

        // 用户管理的搜索条件
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(user.getUsername())) {
            userQueryWrapper.like("username", user.getUsername());
        }

        if (StrUtil.isNotBlank(user.getMobile())) {
            userQueryWrapper.like("mobile", user.getMobile());
        }

        if (StrUtil.isNotBlank(user.getEmail())) {
            userQueryWrapper.like("email", user.getEmail());
        }

        // 部门搜索
        if (StrUtil.isNotBlank(user.getDepartmentId())) {
            userQueryWrapper.eq("departmentId", user.getDepartmentId());
        }

        if (user.getType() != null) {
            userQueryWrapper.eq("type", user.getType());
        }

        if (user.getStatus() != null) {
            userQueryWrapper.eq("status", user.getStatus());
        }

        //创建时间
        if (StrUtil.isNotBlank(pageBo.getCreateDate()) && StrUtil.isNotBlank(pageBo.getEndDate())) {
            Date start = DateUtil.parse(pageBo.getCreateDate()); // 字符串转换date
            Date end = DateUtil.parse(pageBo.getEndDate());

            // DateUtil.endOfDay(end) 补充23:59:59
            userQueryWrapper.between("create_time", start, DateUtil.endOfDay(end));
        }


        // 根据时间倒序
        userQueryWrapper.orderByDesc(true, "create_time");

        IPage<User> userIPage = userDao.selectPage(usersPage, userQueryWrapper);
        return userIPage;
    }
}
