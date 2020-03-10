package com.carson.vboot.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.enums.CommonEnums;
import com.carson.vboot.core.common.enums.ExceptionEnums;
import com.carson.vboot.core.dao.mapper.UserDao;
import com.carson.vboot.core.dao.mapper.UserRoleDao;
import com.carson.vboot.core.entity.Role;
import com.carson.vboot.core.entity.User;
import com.carson.vboot.core.entity.UserRole;
import com.carson.vboot.core.exception.VbootException;
import com.carson.vboot.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * created by Nicofh on 2020-03-08
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public VbootBaseDao<User> getBaseDao() {
        return userDao;
    }

    @Override
    public User getId(String userId) {

        // todo
        User user = userDao.selectById(userId);
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);

        ArrayList<String> roleIds = new ArrayList<>();
        // 获取授权列表
        List<UserRole> userRoleList = userRoleDao.selectList(queryWrapper);

        for (UserRole userRole : userRoleList) {
            roleIds.add(userRole.getRoleId());
        }
        // user.setRoleList(roleIds);

        return user;
    }

    /**
     * 添加用户/ 修改用户
     *
     * @param user
     * @return
     */
    @Transactional
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
        String userId = user.getId();

        if (null != userId) {
            // ussrId存在代表是修改，先清空对应角色id
            QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            userRoleDao.delete(wrapper);
        }

//        // 插入关联角色id
//        List<Role> roleList = user.getRoleList();
//        if (CollUtil.isNotEmpty(roleList)) {
//            for (String roleId : roleList) {
//                UserRole userRole = new UserRole();
//                userRole.setRoleId(roleId);
//                userRole.setUserId(user.getId());
//                userRoleDao.insert(userRole);
//            }
//        }
//
//        int count;
//
//        if (null != userId) {
//            count = userDao.updateById(user);
//        } else {
//            // 密码加密 BCryptPasswordEncoder spring security 加密
//            // user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
//            count = userDao.insert(user);
//        }
//        return count;
        return null;
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

    @Transactional
    @Override
    public Integer delete(String id) {
        try {
            userDao.deleteById(id);
            QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();

            // 删除该用户的关联角色
            userRoleQueryWrapper.eq("user_id", id);
            userRoleDao.delete(userRoleQueryWrapper);
            return 1;
        } catch (Exception e) {
            throw new VbootException(ExceptionEnums.DEL_ERROR);
        }

    }

    @Transactional
    @Override
    public Integer delete(Collection<String> idList) {

        try {
            if (CollUtil.isNotEmpty(idList)) {
                for (String userId : idList) {
                    userDao.deleteById(userId);
                    // 删除该用户的关联角色
                    QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
                    userRoleQueryWrapper.eq("user_id", userId);
                    userRoleDao.delete(userRoleQueryWrapper);
                }
            }
            return 1;
        } catch (Exception e) {
            throw new VbootException(ExceptionEnums.DEL_ERROR);
        }
    }

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        return userDao.selectOne(userQueryWrapper);
    }


}
