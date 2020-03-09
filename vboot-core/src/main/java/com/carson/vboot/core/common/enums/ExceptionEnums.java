package com.carson.vboot.core.common.enums;

import lombok.Getter;

/**
 * created by Nicofh on 2020-03-09
 * 异常枚举类
 */
@Getter
public enum ExceptionEnums {
    USER_NAME_EXIST(10000,"用户名已存在"),
    USER_MOBILE_EXIST(10001,"手机号已存在"),
    USER_EMAIL_EXIST(10002,"邮箱已存在"),
    ;
    private Integer id;
    private String message;

    ExceptionEnums(Integer id, String message) {
        this.id = id;
        this.message = message;
    }
}