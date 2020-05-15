package com.carson.vboot.core.config.security;


import cn.hutool.core.util.StrUtil;
import com.carson.vboot.core.common.enums.CommonEnums;
import com.carson.vboot.core.entity.Permission;
import com.carson.vboot.core.entity.Role;
import com.carson.vboot.core.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Carson
 * 查询到的用户设置密码和权限,还要设置一些禁用，启用的设置
 */
@Slf4j
public class SecurityUserDetails extends UserVO implements UserDetails {

    private static final long serialVersionUID = 1L;

    public SecurityUserDetails(UserVO user) {

        if (user != null) {
            this.setUsername(user.getUsername());
            this.setPassword(user.getPassword());
            this.setStatus(user.getStatus());
            this.setRoles(user.getRoles());
            this.setPermissions(user.getPermissions());
        }
    }

    /**
     * 用户查询拥有的权限和角色
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorityList = new ArrayList<>();
        List<Permission> permissions = this.getPermissions();
        // 添加请求权限
        if (permissions != null && permissions.size() > 0) {
            for (Permission permission : permissions) {
                // 获取操作的按钮权限
                if (CommonEnums.PERMISSION_OPERATION.getId().equals(permission.getType())
                        && StrUtil.isNotBlank(permission.getTitle())
                        && StrUtil.isNotBlank(permission.getPath())) {
                    // 权限名称放入数组
                    authorityList.add(new SimpleGrantedAuthority(permission.getTitle()));
                }
            }
        }
        // 添加角色
        List<Role> roles = this.getRoles();
        if (roles != null && roles.size() > 0) {
            // lambda表达式
            roles.forEach(item -> {
                if (StrUtil.isNotBlank(item.getName())) {
                    // 角色名称放入数组
                    authorityList.add(new SimpleGrantedAuthority(item.getName()));
                }
            });
        }

        log.warn("authorityList {}", authorityList);
        return authorityList;
    }


    /**
     * 账号是否冻结
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {

        return CommonEnums.USER_STATUS_LOCK.getId().equals(this.getStatus()) ? false : true;
    }

    /**
     * 账户是否删除，删除了不能回复
     *
     * @return
     */
    @Override
    public boolean isEnabled() {

        return CommonEnums.USER_STATUS_NORMAL.getId().equals(this.getStatus()) ? true : false;
    }

    /**
     * 账户是否过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {

        return true;
    }


    /**
     * 密码是否过期
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }


}