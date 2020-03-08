package com.carson.vboot.core.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    @ResponseBody
    @ApiOperation(value = "分页获取")
    public Result<IPage<User>> getUserByPage(PageBo pageBo, User user) {

        Page<User> users = new Page<>(pageBo.getPageIndex(), pageBo.getPageSize());


        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(user.getUsername())) {
            userQueryWrapper.like("username", user.getUsername());
        }

        if (StrUtil.isNotBlank(user.getMobile())) {
            userQueryWrapper.like("mobile", user.getMobile());
        }


        // 根据时间倒序
        userQueryWrapper.orderByDesc(true, "create_time");


        IPage<User> userIPage = userService.selectPage(users, userQueryWrapper);

        return new ResultUtil<IPage<User>>().setData(userIPage);
    }


}
