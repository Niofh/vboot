package com.carson.vboot.core.handler;

import com.carson.vboot.core.common.utils.ResultUtil;
import com.carson.vboot.core.exception.VbootException;
import com.carson.vboot.core.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * created by Nicofh on 2020-03-09
 * 全局异常
 */
@ControllerAdvice
@Slf4j
public class GlobalException {

    /**
     * 业务异常处理
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = VbootException.class)
    public Result<Object> vbootException(VbootException e) {
        return ResultUtil.error(e.getCode(), e.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result<Object> exception(HttpServletRequest request, Exception e) {

        if (e instanceof BindException) {
            // 表单验证
            List<FieldError> fieldErrors = ((BindException) e).getBindingResult().getFieldErrors();
            //多个错误，取第一个
            FieldError error = fieldErrors.get(0);
            String msg = error.getDefaultMessage();
            return ResultUtil.error(400, msg);
        } else {
            log.error("【系统异常】 {}", e);
            return ResultUtil.error(500, "系统异常");
        }

    }
}
