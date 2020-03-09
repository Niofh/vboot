package com.carson.vboot.core.service;

import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.entity.User;
import com.carson.vboot.core.entity.UserRole;

import java.util.List;

public interface UserRoleService extends VbootService<UserRole> {

    /**
     * 根据角色查询数据
     * @param roleId
     * @return
     */
    List<UserRole> findByRoleId(String roleId);



    /**
     * 根据角色id查询用户
     * @param roleId
     * @return
     */
    List<User> findUserByRoleId(String roleId);


    /**
     * 根据用户id删除角色
     * @param userId
     */
    void deleteByUserId(String userId);

}
