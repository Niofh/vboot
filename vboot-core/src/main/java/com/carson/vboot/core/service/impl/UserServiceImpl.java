package com.carson.vboot.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.enums.CommonEnums;
import com.carson.vboot.core.common.enums.ExceptionEnums;
import com.carson.vboot.core.dao.mapper.RoleDao;
import com.carson.vboot.core.dao.mapper.UserDao;
import com.carson.vboot.core.dao.mapper.UserRoleDao;
import com.carson.vboot.core.entity.Role;
import com.carson.vboot.core.entity.User;
import com.carson.vboot.core.entity.UserRole;
import com.carson.vboot.core.exception.VbootException;
import com.carson.vboot.core.service.UserService;
import com.carson.vboot.core.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private RoleDao roleDao;

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
    @Transactional
    @Override
    public Integer save(User user) {
        try {
            String username = user.getUsername();
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            User userObj = userDao.selectOne(queryWrapper);

            if (userObj != null) {
                throw new VbootException(ExceptionEnums.USER_NAME_EXIST);
            }
            User u = this.commonUser(user);
            return userDao.insert(u);
        } catch (VbootException e) {
            throw new VbootException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            throw new VbootException(ExceptionEnums.ADD_ERROR);
        }

    }

    /**
     * / 修改用户
     *
     * @param user
     * @return
     */
    @Override
    public Integer update(User user) {
        try {
            user.setUsername(null);
            User u = this.commonUser(user);
            return userDao.updateById(u);
        } catch (VbootException e) {
            throw new VbootException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            throw new VbootException(ExceptionEnums.UPDATE_ERROR);
        }

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
     * 根据id查询用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public UserVO getUserById(String userId) {
        User user = userDao.selectById(userId);

        if (null == user) {
            throw new VbootException(ExceptionEnums.NO_SEARCH);
        }

        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);

        ArrayList<Role> roleArrayList = new ArrayList<>();
        // 获取用户角色列表
        List<UserRole> userRoleList = userRoleDao.selectList(queryWrapper);

        for (UserRole userRole : userRoleList) {
            String roleId = userRole.getRoleId();
            // 获取角色
            Role role = roleDao.selectById(roleId);
            // 放入集合里
            roleArrayList.add(role);
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setRoleList(roleArrayList);
        return userVO;
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

    /**
     * 新增和修改公共方法
     *
     * @param user
     * @return
     */
    @Transactional
    public User commonUser(User user) {
        // 获取用户id
        String userId = user.getId();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

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

        if (null != userId) {
            // ussrId存在代表是修改，先清空对应角色id
            QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            userRoleDao.delete(wrapper);
        }

        // 创建一个空数组
        List<String> roleList = new ArrayList<>();

        // 获取默认角色
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("default_role", CommonEnums.ROLE_DEFAULT.getId());
        List<Role> defaultRoleList = roleDao.selectList(roleQueryWrapper);

        // 获取手动添加角色
        List<String> roleIds = user.getRoleIds();

        // 如果添加或者注册时候没有添加角色，那么添加默认角色
        if (CollUtil.isEmpty(roleIds)) {
            for (Role role : defaultRoleList) {
                roleList.add(role.getId());
            }
        } else {
            // 否则添加自己选的角色
            roleList = roleIds;
        }

        // 如果角色为空，就不添加在用户角色关联表
        if (CollUtil.isNotEmpty(roleList)) {
            for (String roleId : roleList) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(user.getId());
                userRoleDao.insert(userRole);
            }
        }
        return user;
    }


}
