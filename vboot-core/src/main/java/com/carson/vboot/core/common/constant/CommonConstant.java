package com.carson.vboot.core.common.constant;

import org.springframework.stereotype.Component;

/**
 * created by Nicofh on 2020-03-08
 */
@Component
public interface CommonConstant {

    /**
     * todo 一个新的项目都需要修改这个名称，不然会redis重复替换
     */
    String APP_NAME = "VBOOT";

    /**
     * 用户默认头像
     */
    String USER_DEFAULT_AVATAR = "https://i.loli.net/2019/04/28/5cc5a71a6e3b6.png";


    /**
     * 如果使用JWT，加密前缀就要加上 `Bearer `
     */
    String TOKEN_SPLIT = "Bearer ";


    /**
     * 权限参数头
     */
    String AUTHORITIES = "authorities";

    /**
     * token参数头
     */
    String HEADER = "accessToken";


    /**
     * JWT签名加密key
     */
    String JWT_SIGN_KEY = "vboot";


    // ===== 存到redis里面 ====

    String USER_TOKEN = APP_NAME + "_USER_TOKEN";

    String TOKEN_PRE = APP_NAME + "_TOKEN_PRE";

    /**
     * 登录失败的标志
     */
    String LOGIN_FAILFLAG = APP_NAME+"LOGIN_FAILFLAG::";


    /**
     * 根据用户名获取部门权限
     */
    String USER_DEPID = APP_NAME + "_USER_DEPID";

    // ===== 存到redis里面 ====


}
