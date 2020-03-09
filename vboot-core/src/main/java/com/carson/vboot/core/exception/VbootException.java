package com.carson.vboot.core.exception;


import com.carson.vboot.core.common.enums.ExceptionEnums;
import lombok.Data;

/**
 * created by Nicofh on 2020-03-09
 */
@Data
public class VbootException extends RuntimeException {

    private Integer code;

    public VbootException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public VbootException(ExceptionEnums exceptionEnums) {
        this(exceptionEnums.getId(), exceptionEnums.getMessage());
    }
}
