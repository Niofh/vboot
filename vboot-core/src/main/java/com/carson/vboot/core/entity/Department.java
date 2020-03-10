package com.carson.vboot.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.carson.vboot.core.base.VbootBaseEntity;
import com.carson.vboot.core.common.enums.CommonEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Exrick
 */
@Data
@TableName("t_department")
@ApiModel(value = "部门")
public class Department extends VbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门名称")
    @NotNull(message = "部门名称不能为空")
    private String title;

    @ApiModelProperty(value = "父id，parentId=0为第一级")
    private String parentId;

    @ApiModelProperty(value = "排序值")
    private BigDecimal sort;

    @ApiModelProperty(value = "是否启用 0启用 -1禁用")
    private Integer status = CommonEnums.STATUS_NORMAL.getId();

    @TableField(exist=false)
    @ApiModelProperty(value = "父节点名称")
    private String parentTitle;

    @TableField(exist=false)
    @ApiModelProperty(value = "主负责人")
    private List<String> mainHeader;

    @TableField(exist=false)
    @ApiModelProperty(value = "副负责人")
    private List<String> viceHeader;
}