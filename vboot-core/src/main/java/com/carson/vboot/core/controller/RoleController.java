package com.carson.vboot.core.controller;

import com.carson.vboot.core.base.VBootController;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.entity.Role;
import com.carson.vboot.core.service.RoleService;
import com.carson.vboot.core.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "角色接口")
@RestController
@RequestMapping("/role")
public class RoleController extends VBootController<Role> {


    @Autowired
    private RoleService roleService;


    @Override
    public VbootService<Role> getService() {
        return roleService;
    }

    @PostMapping("/setPermission")
    @ApiOperation(value = "通过roleId设置权限")
    public Result<Object> setPermission(String roleId, String[] permissionIds) {
        roleService.setPermissionByRoleId(roleId, permissionIds);
        return ResultUtil.data(null);
    }
}
