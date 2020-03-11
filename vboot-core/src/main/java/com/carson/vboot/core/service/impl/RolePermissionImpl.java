package com.carson.vboot.core.service.impl;

import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.dao.mapper.RolePermissionDao;
import com.carson.vboot.core.entity.RolePermission;
import com.carson.vboot.core.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author oufuhua
 * 角色权限逻辑
 */
@Service
@Slf4j
public class RolePermissionImpl implements RolePermissionService {

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    public VbootBaseDao<RolePermission> getBaseDao() {
        return rolePermissionDao;
    }
}
