package com.carson.vboot.core.service;

import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.entity.User;
import com.carson.vboot.core.vo.Result;

/**
 * created by Nicofh on 2020-03-08
 */
public interface UserService extends VbootService<User> {

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    Result<Object> saveUser(User user);
}
