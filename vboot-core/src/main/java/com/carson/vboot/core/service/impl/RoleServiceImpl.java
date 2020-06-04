package com.carson.vboot.core.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.enums.CommonEnums;
import com.carson.vboot.core.common.enums.ExceptionEnums;
import com.carson.vboot.core.config.security.permission.MySecurityMetadataSource;
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
import org.springframework.cache.annotation.*;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MySecurityMetadataSource mySecurityMetadataSource;

    @Override
    public VbootBaseDao<Role> getBaseDao() {
        return roleDao;
    }


    /**
     * 分页查询
     *
     * @param pageBo
     * @param role
     * @return
     */
    @Override
    public IPage<Role> getRoleByPage(PageBo pageBo, Role role) {

        Page<Role> page = new Page<>(pageBo.getPageIndex(), pageBo.getPageSize());
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(role.getName())) {
            roleQueryWrapper.eq("name", role.getName());
        }

        if (StrUtil.isNotBlank(pageBo.getOrder())) {
            String name = StrUtil.toUnderlineCase(pageBo.getSort());
            if ("asc".equals(pageBo.getOrder())) {
                roleQueryWrapper.orderByAsc(name);
            } else {
                roleQueryWrapper.orderByDesc(name);
            }
        }
        // 根据时间倒序
        roleQueryWrapper.orderByDesc(true, "create_time");
        Page<Role> rolePage = roleDao.selectPage(page, roleQueryWrapper);

        return rolePage;
    }

    /**
     * 根据ID获取实体类数据
     *
     * @param roleId
     * @return
     */
    @Cacheable(cacheNames = "role", key = "roleId", condition = "#result!=null")
    @Override
    public Role getId(String roleId) {
        return roleDao.selectById(roleId);
    }

    @Cacheable(cacheNames = "vboot::roles", key = "'getall'")
    @Override
    public List<Role> getAll() {
        return roleDao.selectList(null);
    }


    @Cacheable(cacheNames = {"vboot::roles"}, key = "'getall'") // 删除所有角色
    @Override
    public Role save(Role role) {
        int num = roleDao.insert(role);
        if (num > 0) {
            return role;
        }
        return null;
    }

    @Caching(
            put = {
                    @CachePut(cacheNames = "role", key = "#result.id", condition = "#result!=null") // 更新根据id的缓存
            },
            evict = {
                    @CacheEvict(cacheNames = {"vboot::roles"}, key = "'getall'"), // 删除所有角色
            }
    )
    @Override
    public Role update(Role role) {
        int num = roleDao.updateById(role);
        if (num > 0) {
            return role;
        }
        return null;
    }

    /**
     * 批量id删除
     *
     * @param idList
     */
    @CacheEvict(cacheNames = {"user", "vboot::user", "user::role", "vboot::roles", "role::permission", "role::dep"}, allEntries = true)
    // 删除用户和用户关联表所有缓存
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

            // 删除角色单个缓存
            redisTemplate.delete("role::" + roleId);

        }
        return i;
    }

    /**
     * 根据角色id获取菜单权限id
     *
     * @param roleId
     * @return
     */
    @Cacheable(cacheNames = "role::permission", key = "#roleId")
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
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "vboot::user",allEntries = true),// 删除用户所有数据
                    @CacheEvict(cacheNames = "role::permission", key = "#roleId")
            }
    )
    @Transactional
    @Override
    public void setPermissionByRoleId(String roleId, String[] permissionIds) {

        Role role = this.getId(roleId);
        if (role == null) {
            throw new VbootException(ExceptionEnums.ROLE_NO_EXIST);
        }
        // 删除以前的角色关联的权限
        QueryWrapper<RolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.eq("role_id", role.getId());
        rolePermissionDao.delete(rolePermissionQueryWrapper);


        if (ArrayUtil.isNotEmpty(permissionIds)) {
            // 新增新的角色关联的权限
            for (String permissionId : permissionIds) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                rolePermissionDao.insert(rolePermission);
            }
        }
        // 重新加载权限
        mySecurityMetadataSource.loadResourceDefine();

    }


    /**
     * 根据角色id获取部门id
     */
    @Cacheable(cacheNames = "role::dep", key = "#roleId")
    @Override
    public List<String> getDepartmentByRoleId(String roleId) {
        Role role = this.getId(roleId);
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
    @CacheEvict(cacheNames = "role::dep", key = "#roleId")
    @Override
    public void setDepartmentByRoleId(String roleId, Integer dataType, String[] depIds) {

        Role role = this.getId(roleId);
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
                roleDepartmentDao.insert(roleDepartment);
            }
        }
    }
}
