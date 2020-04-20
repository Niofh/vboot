package com.carson.vboot.core.common.enums;

import lombok.Getter;

/**
 * created by Nicofh on 2020-03-09
 * 异常枚举类
 */
@Getter
public enum ExceptionEnums {

    SYS_ERROR(900,"连接超时，请重新操作"),
    ADD_ERROR(1000, "添加失败"),
    UPDATE_ERROR(1001, "更新失败"),
    DEL_ERROR(1002, "删除失败"),
    NO_SEARCH(1003, "该数据不存在"),


    USER_NAME_EXIST(10000, "用户名已存在"),
    USER_MOBILE_EXIST(10001, "手机号已存在"),
    USER_EMAIL_EXIST(10002, "邮箱已存在"),
    ROLE_NO_EXIST(10003, "角色不存在"),
    USER_NO_EXIST(10004, "用户不存在"),
    CODE_DETAIL_NO_EXIST(10005,"代码属性没有配置"),
    DICT_KEY_EXIST(10006,"字典key已经存在"),
    DICT_KEY_NO_EXIST(10006,"字典key不存在"),
    DICT_CODE_EXIST(10007,"字典值已经存在"),
    DATA_NO_EXIST(20000, "数据不存在"),
    ;
    private Integer id;
    private String message;

    ExceptionEnums(Integer id, String message) {
        this.id = id;
        this.message = message;
    }
}
