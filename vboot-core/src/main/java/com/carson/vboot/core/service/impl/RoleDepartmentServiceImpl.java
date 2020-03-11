package com.carson.vboot.core.service.impl;

import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.dao.mapper.RoleDepartmentDao;
import com.carson.vboot.core.entity.RoleDepartment;
import com.carson.vboot.core.service.RoleDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author oufuhua
 * 角色数据逻辑
 */
@Slf4j
@Service
public class RoleDepartmentServiceImpl implements RoleDepartmentService {

    @Autowired
    private RoleDepartmentDao departmentDao;

    @Override
    public VbootBaseDao<RoleDepartment> getBaseDao() {
        return departmentDao;
    }
}
