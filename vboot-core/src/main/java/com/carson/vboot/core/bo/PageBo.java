package com.carson.vboot.core.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * created by Nicofh on 2020-03-08
 * 分页基础信息
 */
@Data
public class PageBo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页号")
    private Integer pageIndex = 1;

    @ApiModelProperty(value = "页面大小")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "排序字段")
    private String sort;

    @ApiModelProperty(value = "排序方式 asc/desc")
    private String order;

    @ApiModelProperty(value = "起始日期")
    private String createTime;

    @ApiModelProperty(value = "结束日期")
    private String updateTime;
}
