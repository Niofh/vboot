package com.carson.vboot.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.base.VBootController;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.entity.User;
import com.carson.vboot.core.service.UserService;
import com.carson.vboot.core.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * created by Nicofh on 2020-03-08
 */

@Api(description = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController extends VBootController<User> {
    @Autowired
    private UserService userService;


    @Override
    public VbootService<User> getService() {
        return userService;
    }


    @RequestMapping(value = "/getUserByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<User>> getUserByPage(@Valid PageBo pageBo, User user) {

        return ResultUtil.data(userService.getUserByPage(pageBo, user));
    }


    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "通过userId获取用户")
    @Override
    public Result<Object> get(String userId) {
        return ResultUtil.data(userService.getUserById(userId));
    }
}
