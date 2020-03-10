package com.carson.vboot.core.service.impl;

import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.dao.mapper.PermissionDao;
import com.carson.vboot.core.entity.Permission;
import com.carson.vboot.core.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author oufuhua
 */
@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;


    @Override
    public VbootBaseDao<Permission> getBaseDao() {
        return permissionDao;
    }

}
