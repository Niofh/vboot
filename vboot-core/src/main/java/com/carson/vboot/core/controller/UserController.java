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

        Page<User> users = new Page<>(pageBo.getPageIndex(), pageBo.getPageSize());

        // 用户管理的搜索条件
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(user.getUsername())) {
            userQueryWrapper.like("username", user.getUsername());
        }

        if (StrUtil.isNotBlank(user.getMobile())) {
            userQueryWrapper.like("mobile", user.getMobile());
        }

        if (StrUtil.isNotBlank(user.getEmail())) {
            userQueryWrapper.like("email", user.getEmail());
        }

        // 部门搜索
        if (StrUtil.isNotBlank(user.getDepartmentId())) {
            userQueryWrapper.eq("departmentId", user.getDepartmentId());
        }

        if (user.getType() != null) {
            userQueryWrapper.eq("type", user.getType());
        }

        if (user.getStatus() != null) {
            userQueryWrapper.eq("status", user.getStatus());
        }

        //创建时间
        if (StrUtil.isNotBlank(pageBo.getCreateDate()) && StrUtil.isNotBlank(pageBo.getEndDate())) {
            Date start = DateUtil.parse(pageBo.getCreateDate()); // 字符串转换date
            Date end = DateUtil.parse(pageBo.getEndDate());

            // DateUtil.endOfDay(end) 补充23:59:59
            userQueryWrapper.between("create_time", start, DateUtil.endOfDay(end));
        }


        // 根据时间倒序
        userQueryWrapper.orderByDesc(true, "create_time");

        IPage<User> userIPage = userService.selectPage(users, userQueryWrapper);

        return ResultUtil.data(userIPage);
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

}
