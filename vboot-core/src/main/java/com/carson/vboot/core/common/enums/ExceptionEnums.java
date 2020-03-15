package com.carson.vboot.core.common.enums;

import lombok.Getter;

/**
 * created by Nicofh on 2020-03-09
 * 异常枚举类
 */
@Getter
public enum ExceptionEnums {


    ADD_ERROR(1000, "添加失败"),
    UPDATE_ERROR(1001, "更新失败"),
    DEL_ERROR(1002, "删除失败"),
    NO_SEARCH(1003, "该数据不存在"),


    USER_NAME_EXIST(10000, "用户名已存在"),
    USER_MOBILE_EXIST(10001, "手机号已存在"),
    USER_EMAIL_EXIST(10002, "邮箱已存在"),
    ROLE_NO_EXIST(10003, "角色不存在"),
    USER_NO_EXIST(10004, "用户名不存在"),
    ;
    private Integer id;
    private String message;

    ExceptionEnums(Integer id, String message) {
        this.id = id;
        this.message = message;
    }
}
