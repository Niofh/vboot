package com.carson.vboot.core.common.enums;

import lombok.Getter;

/**
 * created by Nicofh on 2020-03-08
 * 用户权限的枚举
 */
@Getter
public enum CommonEnums {
    USER_STATUS_LOCK(-1, "用户禁用状态"),
    USER_STATUS_NORMAL(0, "用户正常状态"),

    USER_TYPE_NORMAL(0, "普通用户"),
    USER_TYPE_ADMIN(1, "管理员"),

    DATA_TYPE_ALL(0, "全部数据权限"),
    DATA_TYPE_CUSTOM(1, "自定义数据权限"),
    DATA_TYPE_UNDER(2, "本部门及以下"),
    DATA_TYPE_SAME(3, "本部门"),

    HEADER_TYPE_MAIN(0, "部门负责人类型 主负责人"),
    HEADER_TYPE_VICE(1, "部门负责人类型 主负责人"),

    /*数据库标志*/
    STATUS_DISABLE(-1, "禁用状态"),
    STATUS_NORMAL(0, "正常状态"),
    STATUS_DEL_FLAG(1, "删除标志"),


    PERMISSION_NAV(-1, "顶部菜单类型权限"),
    PERMISSION_PAGE(0, "页面类型权限"),
    PERMISSION_OPERATION(1, "操作类型权限"),


    MESSAGE_STATUS_UNREAD(0, "未读"),
    MESSAGE_STATUS_READ(1, "已读");


    private Integer id;
    private String name;

    CommonEnums(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
