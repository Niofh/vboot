package com.carson.vboot.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.dao.mapper.RoleDepartmentDao;
import com.carson.vboot.core.entity.RoleDepartment;
import com.carson.vboot.core.service.RoleDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author oufuhua
 * 角色数据逻辑
 */
@Slf4j
@Service
public class RoleDepartmentServiceImpl implements RoleDepartmentService {

    @Autowired
    private RoleDepartmentDao roleDepartmentDao;

    @Override
    public VbootBaseDao<RoleDepartment> getBaseDao() {
        return roleDepartmentDao;
    }


    /**
     * 根据角色id获取自定义部门
     * @param roleId
     * @return
     */
    @Override
    public List<String> findDepIdsByRoleId(String roleId) {

        ArrayList<String> depIds = new ArrayList<>();
        QueryWrapper<RoleDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        List<RoleDepartment> roleDepartments = roleDepartmentDao.selectList(queryWrapper);

        if (CollUtil.isNotEmpty(roleDepartments)) {
            for (RoleDepartment roleDepartment : roleDepartments) {
                depIds.add(roleDepartment.getDepartmentId());
            }
        }
        return depIds;
    }

}
