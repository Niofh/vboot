package com.carson.vboot.core.service;

import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.entity.Department;

import java.util.List;

/**
 * created by Nicofh on 2020-03-09
 */

public interface DepartmentService extends VbootService<Department> {

    /**
     * 通过父部门id获取子部门id
     * @param parentId
     * @return
     */
    List<String> findChildByParentId(String parentId);
}
