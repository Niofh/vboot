package com.carson.vboot.core.service;

import com.carson.vboot.core.base.VbootService;
import com.carson.vboot.core.entity.Permission;

import java.util.List;


public interface PermissionService extends VbootService<Permission> {


    // 获取正常启用按钮权限
    List<Permission> getPermissionBtnAll();
}
