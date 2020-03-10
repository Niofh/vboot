package com.carson.vboot.core.controller;

import com.carson.vboot.core.base.VBootController;
import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.entity.Permission;
import com.carson.vboot.core.service.PermissionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author oufuhua
 */
@Api(description = "权限管理")
@RestController
@RequestMapping("/permission")
public class PermissionController extends VBootController<Permission> {

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取service
     *
     * @return
     */
    @Override
    public VbootService<Permission> getService() {
        return permissionService;
    }
}
