package com.carson.vboot.core.service;

import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.entity.RoleDepartment;

import java.util.List;

public interface RoleDepartmentService extends VbootService<RoleDepartment> {
    /**
     * 根据角色id获取自定义部门
     * @param roleId
     * @return
     */
    List<String> findDepIdsByRoleId(String roleId);
}
