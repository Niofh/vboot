package com.carson.vboot.core.service.impl;

import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.dao.mapper.DepartmentDao;
import com.carson.vboot.core.entity.Department;
import com.carson.vboot.core.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * created by Nicofh on 2020-03-09
 */
@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {


    @Autowired
    private DepartmentDao departmentDao;

    @Override
    public VbootBaseDao<Department> getBaseDao() {
        return departmentDao;
    }

    @Override
    public List<Department> getAll() {
        return null;
    }

    @Override
    public Integer save(Department entity) {
        return null;
    }

    @Override
    public Integer update(Department entity) {
        return null;
    }

    @Override
    public Integer delete(Collection<? extends Serializable> idList) {
        return null;
    }
}
