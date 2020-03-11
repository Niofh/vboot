package com.carson.vboot.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.entity.User;
import com.carson.vboot.core.vo.UserVO;

/**
 * created by Nicofh on 2020-03-08
 */
public interface UserService extends VbootService<User> {

    IPage<User> getUserByPage(PageBo pageBo, User user);

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 根据id查询用户信息
     * @param userId
     * @return
     */
    UserVO getUserById(String userId);
}
