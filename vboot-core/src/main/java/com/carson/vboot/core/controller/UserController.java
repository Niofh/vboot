package com.carson.vboot.core.controller;

import cn.hutool.core.date.DateUtil;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

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
    public Result<IPage<User>> getUserByPage(@Valid PageBo pageBo, User user) {

        return ResultUtil.data(userService.getUserByPage(pageBo, user));
    }

    @PostMapping(value = "/save")
    @ResponseBody
    @ApiOperation(value = "保存数据")
    @Override
    public Result<Object> save(@Valid User user) {
        Integer save = userService.save(user);
        if (save > 0) {
            return ResultUtil.success("添加成功");
        } else {
            return ResultUtil.error("添加失败");
        }
    }

    @Override
    public Result<User> update(@Valid User user) {
        Integer save = userService.save(user);
        if (save > 0) {
            return ResultUtil.success("更新成功");
        } else {
            return ResultUtil.error("更新失败");
        }
    }
}
