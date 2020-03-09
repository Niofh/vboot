package com.carson.vboot.core.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * created by Nicofh on 2020-03-08
 * 分页基础信息
 */
@Data
public class PageBo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页号")
    @Min(value = 1, message = "开始页不能小于1")
    private Integer pageIndex = 1;

    @ApiModelProperty(value = "页面大小")
    @Max(value = 100, message = "结束页不能大于100")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "排序字段")
    private String sort;

    @ApiModelProperty(value = "排序方式 asc/desc")
    private String order;

    @ApiModelProperty(value = "起始日期")
    private String createDate;

    @ApiModelProperty(value = "结束日期")
    private String endDate;
}
