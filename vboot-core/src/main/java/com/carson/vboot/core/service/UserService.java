package com.carson.vboot.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.entity.User;

import java.util.Collection;
import java.util.List;

/**
 * created by Nicofh on 2020-03-08
 */
public interface UserService {

    IPage<User> getUserByPage(PageBo pageBo, User user);

    /**
     * 根据id查询用户信息
     *
     * @param userId
     * @return
     */
    User getUserById(String userId);


    List<User> getAll();

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    User insertUser(User user);


    User updateUser(User user);


    Integer delete(Collection<String> idList);


    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    User findByUsername(String username);


}
