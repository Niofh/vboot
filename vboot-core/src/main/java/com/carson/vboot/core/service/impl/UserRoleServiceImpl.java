package com.carson.vboot.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.dao.mapper.UserDao;
import com.carson.vboot.core.dao.mapper.UserRoleDao;
import com.carson.vboot.core.entity.User;
import com.carson.vboot.core.entity.UserRole;
import com.carson.vboot.core.service.UserRoleService;
import com.carson.vboot.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户角色实现类
 */
@Slf4j
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private UserService userService;



    /**
     * 根据角色查询数据
     *
     * @param roleId
     * @return
     */
    @Cacheable(cacheNames = "user::role",key = "#roleId") // 根据角色id缓存用户和角色关联表
    @Override
    public List<UserRole> findByRoleId(String roleId) {
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();

        userRoleQueryWrapper.eq("role_id", roleId);

        List<UserRole> userRoles = userRoleDao.selectList(userRoleQueryWrapper);

        return userRoles;
    }

    /**
     * 根据角色id查询用户
     *
     * @param roleId
     * @return
     */
    @Override
    public List<User> findUserByRoleId(String roleId) {
        List<UserRole> userRoleList = this.findByRoleId(roleId);
        List<User> userList = new ArrayList<>();

        // 循环查找用户
        for (UserRole userRole : userRoleList) {
            User user = userService.getUserById(userRole.getUserId());
            userList.add(user);
        }
        return userList;
    }

    /**
     * 根据用户id删除角色
     *
     * @param userId
     */
    @CacheEvict(cacheNames = "user::role",allEntries = true) // 删除所有用户角色表
    @Override
    public void deleteByUserId(String userId) {
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", userId);
        userRoleDao.delete(userRoleQueryWrapper);
    }


    @Override
    public VbootBaseDao<UserRole> getBaseDao() {
        return userRoleDao;
    }


}
