package com.carson.vboot.core.controller;

import cn.hutool.http.HttpUtil;
import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.dao.mapper.PermissionDao;
import com.carson.vboot.core.entity.Permission;
import com.carson.vboot.core.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by Nicofh on 2020-03-15
 */

@Api(description = "登录接口")
@RestController
@RequestMapping("/security")
public class LoginController {

    @Autowired
    private PermissionDao permissionDao;

    @GetMapping("/login/page")
    @ApiOperation(value = "没有登录")
    public Result<Object> needLogin() {

        return ResultUtil.error(401, "您还未登录");
    }


    @RequestMapping(value = "/swagger/login", method = RequestMethod.GET)
    @ApiOperation(value = "Swagger接口文档专用登录接口 方便测试")
    public Result<Object> swaggerLogin(@RequestParam String username, @RequestParam String password,
                                       @ApiParam("可自定义登录接口地址")
                                       @RequestParam(required = false, defaultValue = "http://127.0.0.1:8081/security/vboot/login")
                                               String loginUrl) {

        Map<String, Object> params = new HashMap<>(16);
        params.put("username", username);
        params.put("password", password);
//        params.put("captchaId", captchaId);
//        params.put("code", code);
        String result = HttpUtil.post(loginUrl, params);
        return ResultUtil.data(result);
    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ApiOperation(value = "test")
    public Result<Object> test() {
        List<Permission> byUserId = permissionDao.findByUserId("249024568428072960");
        return ResultUtil.data(byUserId);
    }

}
