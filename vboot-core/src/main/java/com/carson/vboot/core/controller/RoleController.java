package com.carson.vboot.core.controller;

import com.carson.vboot.core.base.VBootController;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.entity.Role;
import com.carson.vboot.core.service.RoleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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
}
