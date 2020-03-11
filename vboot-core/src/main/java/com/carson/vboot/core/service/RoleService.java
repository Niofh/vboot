package com.carson.vboot.core.service;

import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.entity.Role;


public interface RoleService extends VbootService<Role> {

    /**
     * 通过角色id赋值权限
     */
    public void setPermissionByRoleId(String roleId, String[] permissionIds);


    /**
     * 通过角色id赋值部门数据权限
     * @param roleId
     * @param dataType 权限类型
     * @param depIds
     */
    public void departmentByRoleId(String roleId, Integer dataType, String[] depIds);
}
