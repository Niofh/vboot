package com.carson.vboot.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.entity.User;
import com.carson.vboot.core.vo.Result;

/**
 * created by Nicofh on 2020-03-08
 */
public interface UserService extends VbootService<User> {

    IPage<User> getUserByPage(PageBo pageBo, User user);
}
