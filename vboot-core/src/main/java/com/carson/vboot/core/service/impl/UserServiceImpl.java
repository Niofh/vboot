package com.carson.vboot.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.enums.CommonEnums;
import com.carson.vboot.core.common.enums.ExceptionEnums;
import com.carson.vboot.core.config.security.SecurityUtil;
import com.carson.vboot.core.dao.mapper.*;
import com.carson.vboot.core.entity.*;
import com.carson.vboot.core.exception.VbootException;
import com.carson.vboot.core.service.PermissionService;
import com.carson.vboot.core.service.RoleService;
import com.carson.vboot.core.service.UserService;
import com.carson.vboot.core.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Value("${vboot.adminUserName}")
    private String adminUserName;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SecurityUtil securityUtil;


    @Override
    public IPage<User> getUserByPage(PageBo pageBo, User user) {
        Page<User> usersPage = new Page<>(pageBo.getPageIndex(), pageBo.getPageSize());

        // 用户管理的搜索条件
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(user.getUsername())) {
            userQueryWrapper.like("username", user.getUsername());
        }

        if (StrUtil.isNotBlank(user.getNickName())) {
            userQueryWrapper.like("nick_name", user.getNickName());
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

        // todo 如果需要部门展示数据，需要加以下判断
        List<String> depIds = securityUtil.getDepIds();
        log.info("所在部门：{}", depIds);
        if (CollUtil.isNotEmpty(depIds)) {
            userQueryWrapper.in("departmentId", depIds);
        } else {
            if (!securityUtil.getCurrUser().getUsername().equals(adminUserName)) {
                // 如果不是admin账户，是其他没有部门账户，只能看到自己数据
                userQueryWrapper.eq("username", securityUtil.getCurrUser().getUsername());
            }
        }

        // 排序字段
        if(StrUtil.isNotBlank(pageBo.getOrder())){
            if("asc".equals(pageBo.getOrder())){
                userQueryWrapper.orderByAsc(pageBo.getSort());
            }else{
                userQueryWrapper.orderByDesc(pageBo.getSort());
            }
        }

        // 根据时间倒序
        userQueryWrapper.orderByDesc(true, "create_time");

        IPage<User> userIPage = userDao.selectPage(usersPage, userQueryWrapper);
        return userIPage;
    }


    @Cacheable(cacheNames = "vboot::user", key = "'getall'")
    @Override
    public List<User> getAll() {
        return userDao.selectList(null);
    }


    /**
     * 根据id查询用户信息
     *
     * @param userId
     * @return
     */
    @Cacheable(cacheNames = "user", key = "#userId") // 根据用户id缓存
    @Override
    public User getUserById(String userId) {
        // todo
        log.info("进来了--------------------------");
        User user = userDao.selectById(userId);

        if (null == user) {
            throw new VbootException(ExceptionEnums.NO_SEARCH);
        }

        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);

        ArrayList<String> roleIds = new ArrayList<>();
        // 获取用户角色列表
        List<UserRole> userRoleList = userRoleDao.selectList(queryWrapper);

        for (UserRole userRole : userRoleList) {
            String roleId = userRole.getRoleId();
            roleIds.add(roleId);
        }
        user.setRoleIds(roleIds);
        return user;
    }

    /**
     * 添加用户,每添加一个成功，保存redis里
     *
     * @param user
     * @return
     */
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "vboot::user", key = "'getall'") // 删除用户所有数据
            }
    )
    @Transactional
    @Override
    public User insertUser(User user) {
        try {
            String username = user.getUsername();

            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            User userObj = userDao.selectOne(queryWrapper);

            if (userObj != null) {
                throw new VbootException(ExceptionEnums.USER_NAME_EXIST);
            }
            User u = this.commonUser(user, true);
            int insert = userDao.insert(u);
            if (insert > 0) {
                return u;
            }
            return null;
        } catch (VbootException e) {
            throw new VbootException(e.getCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            // 因为用户名是唯一，假删除时候会可能有2个名字相同，所以一般不要删除用户比较好
            throw new VbootException(ExceptionEnums.USER_NAME_EXIST);
        } catch (Exception e) {
            log.info("添加失败：{}", e);
            throw new VbootException(ExceptionEnums.ADD_ERROR);
        }

    }

    /**
     * / 修改用户
     *
     * @param user
     * @return
     */
    @Caching(
            put = {
                    @CachePut(cacheNames = "user", key = "#result.id", condition = "#result!=null") // 根据id缓存
            },
            evict = {
                    @CacheEvict(cacheNames = "vboot::user", key = "'getall'"), // 删除用户所有数据
            }
    )
    @Transactional
    @Override
    public User updateUser(User user) {
        try {

            User userById = this.getUserById(user.getId());

            if (userById == null) {
                throw new VbootException(ExceptionEnums.USER_NO_EXIST);
            }
            // 因为名字是不能修改的
            user.setUsername(userById.getUsername());
            User u = this.commonUser(user, false);
            int i = userDao.updateById(u);

            if (i > 0) {
                // 删除用户名缓存用户信息
                stringRedisTemplate.delete("vboot::user::" + userById.getUsername());
                return u;
            }
            return null;
        } catch (VbootException e) {
            throw new VbootException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("【添加失败】{}", e);
            throw new VbootException(ExceptionEnums.UPDATE_ERROR);
        }
    }


    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "vboot::user", key = "'getall'") // 删除用户所有数据
            }
    )
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
                    // 删除对应用户id缓存
                    stringRedisTemplate.delete("user::" + userId);
                }
            }
            return idList.size();
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
    @Cacheable(cacheNames = "vboot::user", key = "#username") // 根据用户名查询缓存
    @Override
    public UserVO findByUsername(String username) {
        if (StrUtil.isBlank(username)) {
            throw new VbootException(ExceptionEnums.USER_NO_EXIST);
        }
        // 查询用户
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User user = userDao.selectOne(userQueryWrapper);
        if (user == null) {
            throw new VbootException(ExceptionEnums.USER_NO_EXIST);
        }


        UserVO userVO = new UserVO();

        log.info("【adminUserName】 {}",adminUserName);
        if (adminUserName.equals(username)) {
            BeanUtils.copyProperties(user, userVO);
            // admin设置所有权限
            userVO.setPermissions(permissionService.getAll());
            return userVO;
        }



        // 查询用户角色表
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", user.getId());
        List<UserRole> userRoles = userRoleDao.selectList(userRoleQueryWrapper);


        BeanUtils.copyProperties(user, userVO);

        // 查询部门
        if (StrUtil.isNotBlank(userVO.getDepartmentId())) {
            Department department = departmentDao.selectById(userVO.getDepartmentId());
            if (department != null) {
                userVO.setDepartmentTitle(department.getTitle());
            }
        }

        // 添加角色
        if (CollUtil.isNotEmpty(userRoles)) {
            ArrayList<Role> roles = new ArrayList<>();
            for (UserRole userRole : userRoles) {
                Role role = roleService.getId(userRole.getRoleId());
                roles.add(role);
            }
            userVO.setRoles(roles);
        }

        // 查询权限
        List<Permission> permissions = permissionDao.findByUserId(user.getId());
        userVO.setPermissions(permissions);
        return userVO;
    }

    /**
     * 新增和修改公共方法
     *
     * @param user
     * @param addFlag 是否是添加
     * @return
     */
    // todo
    @Transactional
    public User commonUser(User user, Boolean addFlag) {
        // 获取用户id
        String userId = user.getId();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // 验证手机号唯一性
        if (StrUtil.isNotBlank(user.getMobile())) {
            queryWrapper.clear(); // 清空sql判断语句
            queryWrapper.eq("mobile", user.getMobile()).ne("username", user.getUsername());
            User mUser = userDao.selectOne(queryWrapper);
            if (null != mUser) {
                throw new VbootException(ExceptionEnums.USER_MOBILE_EXIST);
            }
        }

        // 验证邮箱唯一性
        if (StrUtil.isNotBlank(user.getEmail())) {
            queryWrapper.clear(); // 清空sql判断语句
            queryWrapper.eq("email", user.getEmail()).ne("username", user.getUsername());
            User mUser = userDao.selectOne(queryWrapper);
            if (null != mUser) {
                throw new VbootException(ExceptionEnums.USER_MOBILE_EXIST);
            }
        }

        if (!addFlag) {
            // addFlag===false代表是修改，先清空对应角色id
            User userById = this.getUserById(userId);

            // 清空用户关联角色
            QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            userRoleDao.delete(wrapper);

            // 设置他的角色
            user.setPassword(userById.getPassword());
            return this.setRoles(user);
        } else {
            // 新加用户
            if (user.getPassword() != null) {
                // 密码加密
                String encode = new BCryptPasswordEncoder().encode(user.getPassword());
                user.setPassword(encode);
            }
            return this.setRoles(user);
        }
    }

    // 设置角色
    @Transactional
    public User setRoles(User user) {
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
        user.setRoleIds(roleList);
        return user;
    }


}
