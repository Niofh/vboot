package com.carson.vboot.core.common.constant;

/**
 * created by Nicofh on 2020-03-08
 */
public interface CommonConstant {

    /**
     * 用户默认头像
     */
    String USER_DEFAULT_AVATAR = "https://i.loli.net/2019/04/28/5cc5a71a6e3b6.png";

    String USER_TOKEN = "VBOOT_USER_TOKEN";

    String TOKEN_PRE = "VBOOT_TOKEN_PRE";

    String TOKEN_SPLIT = "Bearer ";


    /**
     * 权限参数头
     */
    String AUTHORITIES = "authorities";

    /**
     * 登录失败的标志
     */
    String LOGIN_FAILFLAG = "LOGIN_FAILFLAG::";


    /**
     * token参数头
     */
    String HEADER = "accessToken";


    /**
     * JWT签名加密key
     */
    String JWT_SIGN_KEY = "vboot";
}
