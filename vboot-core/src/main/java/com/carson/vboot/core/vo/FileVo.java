package com.carson.vboot.core.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * created by Nicofh on 2020-06-17
 */
@Data
public class FileVo {
    @ApiModelProperty(value = "文件id")
    private String id;

    @ApiModelProperty(value = "文件地址")
    private String url;
}
