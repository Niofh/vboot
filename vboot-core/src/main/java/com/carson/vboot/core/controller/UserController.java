package com.carson.vboot.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carson.vboot.core.bo.PageBo;
import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.config.security.SecurityUtil;
import com.carson.vboot.core.entity.User;
import com.carson.vboot.core.service.UserService;
import com.carson.vboot.core.vo.Result;
import com.carson.vboot.core.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * created by Nicofh on 2020-03-08
 */

@Slf4j
@Api(tags = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtil securityUtil;


    @RequestMapping(value = "/getUserByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取")
    public Result<IPage<User>> getUserByPage(@Valid PageBo pageBo,  User user) {

        return ResultUtil.data(userService.getUserByPage(pageBo, user));
    }


    @RequestMapping(value = "/getAllBase", method = RequestMethod.GET)
    @ApiOperation(value = "获取所有用户")
    public Result<List<User>> getAll() {
        return ResultUtil.data(userService.getAll());
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiOperation(value = "根据Token获取用户信息")
    public Result<User> getUserInfo() {
        User user = userService.getUserById(securityUtil.getCurrUser().getId());
        user.setPassword("");
        return ResultUtil.data(user);
    }

    @RequestMapping(value = "/getUser/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过userId获取用户")
    public Result<User> getUserById(@PathVariable String id) {
        return ResultUtil.data(userService.getUserById(id));
    }


    @PostMapping("/saveBase")
    @ApiOperation(value = "添加用户")
    public Result<Object> insertUser(@Valid  User user) {
        return ResultUtil.data(userService.insertUser(user));
    }

    @PostMapping("/delByIds")
    @ApiOperation(value = "批量删除用户")
    public Result<Object> delete(String[] ids) {
        return ResultUtil.data(userService.delete(Arrays.asList(ids)));
    }


    @PostMapping("/updateBase")
    @ApiOperation(value = "更新用户")
    public Result<Object> updateUser(@Valid User user) {
        return ResultUtil.data(userService.updateUser(user));
    }

    @GetMapping("/findMenuAndPerByUsername")
    @ApiOperation(value = "根据用户名获取菜单和权限")
    public Result<UserVO> findMenuAndPerByUsername(String username){
       return  ResultUtil.data(userService.findByUsername(username));
    }

}
