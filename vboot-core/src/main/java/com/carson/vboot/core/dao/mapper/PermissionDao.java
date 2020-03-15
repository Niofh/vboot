package com.carson.vboot.core.dao.mapper;

import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.entity.Permission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionDao extends VbootBaseDao<Permission> {

    /**
     * 根据用户id查询权限
     *
     * @param userId
     * @return
     */
    List<Permission> findByUserId(@Param("userId") String userId);
}
