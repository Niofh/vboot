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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@CacheConfig(cacheNames = "vboot::role")
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
     * 批量id删除
     *
     * @param idList
     */
    @Transactional
    @Override
    public Integer delete(Collection<String> idList) {
        int i = roleDao.deleteBatchIds(idList);

        for (String roleId : idList) {
            // 删除角色部门表
            QueryWrapper<RoleDepartment> roleDepartmentQueryWrapper = new QueryWrapper<>();
            roleDepartmentQueryWrapper.eq("role_id", roleId);
            roleDepartmentDao.delete(roleDepartmentQueryWrapper);

            // 删除角色权限表
            QueryWrapper<RolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
            rolePermissionQueryWrapper.eq("role_id", roleId);
            rolePermissionDao.delete(rolePermissionQueryWrapper);
        }
        return i;
    }

    /**
     * 根据角色id获取菜单权限id
     *
     * @param roleId
     * @return
     */
    @Override
    public List<String> getPermissionByRoleId(String roleId) {
        QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);

        List<RolePermission> rolePermissions = rolePermissionDao.selectList(queryWrapper);
        List<String> perIds = new ArrayList<>();
        for (RolePermission rolePermission : rolePermissions) {
            perIds.add(rolePermission.getPermissionId());
        }

        return perIds;
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
     * 根据角色id获取部门id
     */
    @Override
    public List<String> getDepartmentByRoleId(String roleId) {
        Role role = roleDao.selectById(roleId);
        if (role == null) {
            throw new VbootException(ExceptionEnums.ROLE_NO_EXIST);
        }
        QueryWrapper<RoleDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);

        List<RoleDepartment> roleDepartments = roleDepartmentDao.selectList(queryWrapper);
        List<String> departmentIds = new ArrayList<>();
        for (RoleDepartment roleDepartment : roleDepartments) {
            departmentIds.add(roleDepartment.getDepartmentId());
        }

        return departmentIds;
    }

    /**
     * 通过角色id赋值部门数据权限
     *
     * @param roleId
     * @param dataType 权限类型 CommonEnums.DATA_TYPE_CUSTOM
     * @param depIds
     */
    @Override
    public void setDepartmentByRoleId(String roleId, Integer dataType, String[] depIds) {

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
            for (String depId : depIds) {
                queryWrapper.clear();
                RoleDepartment roleDepartment = new RoleDepartment();
                roleDepartment.setDepartmentId(depId);
                roleDepartment.setRoleId(roleId);
            }
        }
    }
}
