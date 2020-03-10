package com.carson.vboot.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.carson.vboot.core.base.VbootBaseEntity;
import com.carson.vboot.core.common.enums.CommonEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author oufuhua
 */
@Data
@ApiModel(value = "部门负责人")
@TableName("t_department_header")
public class DepartmentHeader extends VbootBaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("关联部门负责人id")
    private String userId;

    @ApiModelProperty("关联部门id")
    private String departmentId;

    @ApiModelProperty(value = "负责人类型 默认0主要 1副职")
    private Integer type = CommonEnums.HEADER_TYPE_MAIN.getId();
}

