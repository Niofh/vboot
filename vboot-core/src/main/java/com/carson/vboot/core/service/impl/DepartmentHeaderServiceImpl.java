package com.carson.vboot.core.service.impl;


import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.dao.mapper.DepartmentHeaderDao;
import com.carson.vboot.core.entity.DepartmentHeader;
import com.carson.vboot.core.service.DepartmentHeaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 部门负责人接口实现
 */
@Service
@Slf4j
public class DepartmentHeaderServiceImpl implements DepartmentHeaderService {

    @Autowired
    private DepartmentHeaderDao departmentHeaderDao;


    @Override
    public VbootBaseDao<DepartmentHeader> getBaseDao() {
        return departmentHeaderDao;
    }



}
