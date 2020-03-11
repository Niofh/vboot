package com.carson.vboot.core.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.common.enums.CommonEnums;
import com.carson.vboot.core.common.enums.ExceptionEnums;
import com.carson.vboot.core.dao.mapper.RoleDao;
import com.carson.vboot.core.dao.mapper.RoleDepartmentDao;
import com.carson.vboot.core.dao.mapper.RolePermissionDao;
import com.carson.vboot.core.entity.Role;
import com.carson.vboot.core.entity.RoleDepartment;
import com.carson.vboot.core.entity.RolePermission;
import com.carson.vboot.core.exception.VbootException;
import com.carson.vboot.core.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private RoleDepartmentDao roleDepartmentDao;

    @Override
    public VbootBaseDao<Role> getBaseDao() {
        return roleDao;
    }

    /**
     * 通过角色id赋值权限
     */
    @Transactional
    @Override
    public void setPermissionByRoleId(String roleId, String[] permissionIds) {

        Role role = roleDao.selectById(roleId);
        if (role == null) {
            throw new VbootException(ExceptionEnums.ROLE_NO_EXIST);
        }
        // 删除以前的角色
        QueryWrapper<RolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.eq("role_id", role.getId());
        rolePermissionDao.delete(rolePermissionQueryWrapper);


        if (ArrayUtil.isNotEmpty(permissionIds)) {
            // 新增新的角色
            for (String permissionId : permissionIds) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                rolePermissionDao.insert(rolePermission);
            }
        }
    }

    /**
     * 通过角色id赋值部门数据权限
     *
     * @param roleId
     * @param dataType 权限类型
     * @param depIds
     */
    @Override
    public void departmentByRoleId(String roleId, Integer dataType, String[] depIds) {

        Role role = roleDao.selectById(roleId);
        if (role == null) {
            throw new VbootException(ExceptionEnums.ROLE_NO_EXIST);
        }
        // 删除以前的部门数据权限
        QueryWrapper<RoleDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", role.getId());
        roleDepartmentDao.delete(queryWrapper);

        // 更新数据类型
        Role r = new Role();
        r.setId(roleId);
        r.setDataType(dataType);
        roleDao.updateById(r);

        // 如果是自定义数据,插入角色部门关联表
        if (dataType.equals(CommonEnums.DATA_TYPE_CUSTOM.getId()) && ArrayUtil.isNotEmpty(depIds)) {

        }
    }
}
