package com.carson.vboot.core.service.impl;

import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.dao.mapper.RoleDao;
import com.carson.vboot.core.entity.Role;
import com.carson.vboot.core.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public VbootBaseDao<Role> getBaseDao() {
        return roleDao;
    }
}
