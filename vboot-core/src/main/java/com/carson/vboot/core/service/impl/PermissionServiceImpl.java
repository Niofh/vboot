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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    /**
     * 获取所有列表
     *
     * @return
     */
    @Cacheable(value = "vboot::permiss",key = "'getall'") // 缓存所有权限按钮
    @Override
    public List<Permission> getAll() {
        return permissionDao.selectList(null);
    }

    @Cacheable(value = "vboot::permiss",key = "'getallbtn'") // 缓存所有权限按钮
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
    @CacheEvict(cacheNames = "vboot::permiss",allEntries = true)
    @Override
    public Permission save(Permission entity) {
        int insert = permissionDao.insert(entity);
        if (insert > 0) {
            //如果更新了按钮， 重新加载权限
            if(entity.getType().equals(CommonEnums.PERMISSION_OPERATION.getId())){
                mySecurityMetadataSource.loadResourceDefine();
            }
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
    @CacheEvict(cacheNames = {"vboot::permiss"},allEntries = true)
    @Override
    public Permission update(Permission entity) {
        int num = permissionDao.updateById(entity);
        if (num > 0) {
            //如果更新了按钮， 重新加载权限
            if(entity.getType().equals(CommonEnums.PERMISSION_OPERATION.getId())){
                mySecurityMetadataSource.loadResourceDefine();
            }
            return entity;
        }
        return null;
    }

    /**
     * 批量id删除
     *
     * @param idList
     */
    @CacheEvict(cacheNames = {"vboot::permiss","vboot::user","user","vboot::roles","roles","role::permission"},allEntries = true)
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
