package com.carson.vboot.core.config.security;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.carson.vboot.core.common.constant.CommonConstant;
import com.carson.vboot.core.common.enums.CommonEnums;
import com.carson.vboot.core.dao.mapper.RoleDepartmentDao;
import com.carson.vboot.core.entity.Department;
import com.carson.vboot.core.entity.Permission;
import com.carson.vboot.core.entity.Role;
import com.carson.vboot.core.entity.User;
import com.carson.vboot.core.properties.TokenProperties;
import com.carson.vboot.core.service.DepartmentService;
import com.carson.vboot.core.service.RoleDepartmentService;
import com.carson.vboot.core.service.UserService;
import com.carson.vboot.core.vo.TokenUser;
import com.carson.vboot.core.vo.UserVO;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * created by Nicofh on 2020-03-14
 */

@Component
@Slf4j
public class SecurityUtil {

    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleDepartmentService roleDepartmentService;

    @Autowired
    private DepartmentService departmentService;

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public UserVO getCurrUser() {

        // 获取当前缓存的信息
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("【Security保存的用户信息】{}", user);
        return userService.findByUsername(user.getUsername());
    }

    /**
     * 通过用户名获取用户拥有权限
     *
     * @param username
     */
    public List<GrantedAuthority> getCurrUserPerms(String username) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Permission p : userService.findByUsername(username).getPermissions()) {
            authorities.add(new SimpleGrantedAuthority(p.getTitle()));
        }
        return authorities;
    }

    /**
     * 登录成功接口生成token,并且设置了token在缓存中，而且token对应权限也设置缓存里面
     * 通过用户名生成token并且设置授权按钮名称
     *
     * @param username
     * @param list     授权按钮名称数组
     * @return
     */
    public String getTokenAndSetAuthority(String username, List<String> list) {

        if (CollUtil.isEmpty(list)) {
            // 如果数组为空，那么就从数据库获取
            UserVO u = userService.findByUsername(username);
            list = new ArrayList<>();
            // 缓存权限
            if (tokenProperties.getStorePerms()) {
                for (Permission p : u.getPermissions()) {
                    if (CommonEnums.PERMISSION_OPERATION.getId().equals(p.getType())
                            && StrUtil.isNotBlank(p.getTitle())
                            && StrUtil.isNotBlank(p.getPath())) {
                        list.add(p.getTitle());
                    }
                }
                for (Role r : u.getRoles()) {
                    list.add(r.getName());
                }
            }
        }


        // 登陆成功生成token
        String token;
        // 如果存放在redis
        if (tokenProperties.getRedis()) {
            // redis
            token = UUID.randomUUID().toString().replace("-", "");

            TokenUser user = new TokenUser(username, list);
            // 不缓存权限
            if (!tokenProperties.getStorePerms()) {
                user.setPermissions(null);
            }
            // 单设备登录 之前的token失效
            if (tokenProperties.getSdl()) {
                // 获取以前的token
                String oldToken = (String) redisTemplate.opsForValue().get(CommonConstant.USER_TOKEN + username);
                if (StrUtil.isNotBlank(oldToken)) {
                    // 如果以前token存在，删除原来用户权限信息
                    redisTemplate.delete(CommonConstant.TOKEN_PRE + oldToken);
                }
            }
            // 保存token
            redisTemplate.opsForValue().set(CommonConstant.USER_TOKEN + username, token, tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);

            // 根据token保存用户权限信息
            redisTemplate.opsForValue().set(CommonConstant.TOKEN_PRE + token, user, tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
        } else {
            // 存放在jwt

            // 不缓存权限
            if (!tokenProperties.getStorePerms()) {
                list = null;
            }
            token = CommonConstant.TOKEN_SPLIT + Jwts.builder()
                    //主题 放入用户名
                    .setSubject(username)
                    //自定义属性 放入用户拥有请求权限
                    .claim(CommonConstant.AUTHORITIES, new Gson().toJson(list))
                    //失效时间
                    .setExpiration(new Date(System.currentTimeMillis() + tokenProperties.getTokenExpireTime() * 60 * 1000))
                    //签名算法和密钥
                    .signWith(SignatureAlgorithm.HS512, CommonConstant.JWT_SIGN_KEY)
                    .compact();
        }

        return token;
    }


    /**
     * 获取部门查看数据权限
     *
     * @return 空[]代表只能本人看，[id,id]就要在select * from xxxx where depId in [ids,ids]
     */
    public List<String> getDepIds() {

        UserVO currUser = this.getCurrUser();
        List<String> depIds = new ArrayList<>();

        // 如果当前没有部门，只能本人数据
        if (StrUtil.isEmpty(currUser.getDepartmentId())) {
            return depIds;
        }

        List<Role> roles = currUser.getRoles();

        // 因为一个用户多个角色，数据权限会有重复，所以建立一个Set数组去重
        HashSet<String> depIdsSet = new HashSet<>();

        // 判断有无全部数据的角色
        Boolean flagAll = false;
        if (CollUtil.isNotEmpty(roles)) {
            for (Role r : roles) {
                // 如果查看数据权限，直接返回null
                if (r.getDataType().equals(CommonEnums.DATA_TYPE_ALL.getId())) {
                    flagAll = true;
                    break;
                }
            }
            // 包含全部权限返回null
            if (flagAll) {
                return null;
            }


            for (Role role : roles) {

                if (role.getDataType().equals(CommonEnums.DATA_TYPE_CUSTOM.getId())) {

                    // 自定义数据权限
                    List<String> depId = roleDepartmentService.findDepIdsByRoleId(role.getId());
                    depIdsSet.addAll(depId);

                } else if (role.getDataType().equals(CommonEnums.DATA_TYPE_UNDER.getId())) {
                    //本部门及以下

                    // 本部门
                    depIdsSet.add(currUser.getDepartmentId());
                    //以下部门
                    this.getChildDep(depIdsSet, currUser.getDepartmentId());

                } else if (role.getDataType().equals(CommonEnums.DATA_TYPE_SAME.getId())) {
                    // 本部门数据
                    depIdsSet.add(currUser.getDepartmentId());
                } else {
                    // 没有设置数据权限，只能看自己本人
                }
            }

        }

        depIds.addAll(depIdsSet);

        // 存入缓存里面

        return depIds;

    }

    // 递归获取子部门
    private void getChildDep(HashSet<String> depIdSet, String depId) {

        List<String> depIds = departmentService.findChildByParentId(depId);
        depIdSet.addAll(depIds);
        if (CollUtil.isNotEmpty(depIds)) {
            for (String dId : depIds) {
                this.getChildDep(depIdSet, dId);
            }
        }
    }

}
