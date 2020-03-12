package com.carson.vboot.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
import java.util.Arrays;

/**
 * created by Nicofh on 2020-03-08
 */

@Api(description = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/getUserByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<User>> getUserByPage(@Valid PageBo pageBo, User user) {

        return ResultUtil.data(userService.getUserByPage(pageBo, user));
    }


    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取所有用户")
    public Result<Object> getAll() {
        return ResultUtil.data(userService.getAll());
    }

    @RequestMapping(value = "/getUser/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "通过userId获取用户")
    public Result<Object> getUserById(@PathVariable String id) {
        return ResultUtil.data(userService.getUserById(id));
    }


    @PostMapping("/addUser")
    @ResponseBody
    @ApiOperation(value = "添加用户")
    public Result<Object> insertUser(@Valid User user) {
        return ResultUtil.data(userService.insertUser(user));
    }

    @PostMapping("/deleteUser")
    @ResponseBody
    @ApiOperation(value = "批量删除用户")
    public Result<Object> delete(String[] ids) {
        return ResultUtil.data(userService.delete(Arrays.asList(ids)));
    }


    @PostMapping("/updateUser")
    @ResponseBody
    @ApiOperation(value = "更新用户")
    public Result<Object> save(@Valid User user) {
        return ResultUtil.data(userService.updateUser(user));
    }


}
