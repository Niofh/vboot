package com.carson.vboot.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.entity.Role;

import java.util.List;


public interface RoleService extends VbootService<Role> {

    IPage<Role> getRoleByPage(PageBo pageBo, Role role);

    /**
     * 根据角色id获取菜单权限
     *
     * @param roleId
     * @return
     */
    List<String> getPermissionByRoleId(String roleId);

    /**
     * 通过角色id赋值权限
     */
    void setPermissionByRoleId(String roleId, String[] permissionIds);


    /**
     * 根据角色id获取部门id
     */
    List<String> getDepartmentByRoleId(String roleId);

    /**
     * 通过角色id赋值部门数据权限
     *
     * @param roleId
     * @param dataType 权限类型
     * @param depIds
     */
    void setDepartmentByRoleId(String roleId, Integer dataType, String[] depIds);
}
