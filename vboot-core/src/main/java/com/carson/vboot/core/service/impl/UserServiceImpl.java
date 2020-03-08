package com.carson.vboot.core.service.impl;

import com.carson.vboot.core.base.VbootBaseDao;
import com.carson.vboot.core.dao.mapper.UserDao;
import com.carson.vboot.core.entity.User;
import com.carson.vboot.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by Nicofh on 2020-03-08
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public VbootBaseDao<User> getBaseDao() {
        return userDao;
    }
}
