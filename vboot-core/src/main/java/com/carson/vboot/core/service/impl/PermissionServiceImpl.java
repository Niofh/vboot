package com.carson.vboot.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.common.enums.CommonEnums;
import com.carson.vboot.core.config.security.permission.MySecurityMetadataSource;
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

    @Autowired
    private MySecurityMetadataSource mySecurityMetadataSource;


    @Override
    public VbootBaseDao<Permission> getBaseDao() {
        return permissionDao;
    }

    @Override
    public List<Permission> getPermissionBtnAll() {

        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", CommonEnums.STATUS_NORMAL.getId()).eq("type", CommonEnums.PERMISSION_OPERATION.getId());
        List<Permission> permissions = permissionDao.selectList(queryWrapper);
        return permissions;
    }

    /**
     * 保存
     *
     * @param entity
     * @return
     */
    @Override
    public Permission save(Permission entity) {
        int insert = permissionDao.insert(entity);
        if (insert > 0) {
            // 重新加载权限
            mySecurityMetadataSource.loadResourceDefine();
            return entity;
        }
        return null;
    }

    /**
     * 修改
     *
     * @param entity
     * @return
     */
    @Override
    public Permission update(Permission entity) {
        int num = permissionDao.updateById(entity);
        if (num > 0) {
            // 重新加载权限
            mySecurityMetadataSource.loadResourceDefine();
            return entity;
        }
        return null;
    }

    /**
     * 批量id删除
     *
     * @param idList
     */
    @Override
    public Integer delete(Collection<String> idList) {
        int num = permissionDao.deleteBatchIds(idList);
        if (num > 0) {
            // 重新加载权限
            mySecurityMetadataSource.loadResourceDefine();
            return num;
        }
        return null;
    }
}
