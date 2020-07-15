package com.example.redisdemo;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * created by Nicofh on 2020-03-09
 * 全局异常
 */
@ControllerAdvice
@Slf4j
public class GlobalException {

    @ResponseBody
    public Result<Object>  exception(HttpServletRequest request, Exception e) {
        // 日志不要打印e.printStackTrace()，不然内存增大，文件增大，线程阻塞导致锁死
        log.error("【系统异常】：", e);
        return ResultUtil.error(500, "系统异常："+e.getMessage());
    }
}
