package com.carson.vboot.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.carson.vboot.core.base.VbootBaseEntity;
import com.carson.vboot.core.common.enums.CommonEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author carson
 */
@Data
@TableName("t_permission")
@ApiModel(value = "菜单权限")
public class Permission extends VbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜单名称/权限名称 英文")
    private String name;

    @ApiModelProperty(value = "是否显示,1时显示，0不显示")
    private Integer show;

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "类型 -1顶部菜单 0页面 1具体操作")
    private Integer type;

    @ApiModelProperty(value = "菜单名称/权限名称")
    private String title;

    @ApiModelProperty(value = "页面路径/资源链接path")
    @NotNull(message = "请求路径不能为空")
    private String path;

    @ApiModelProperty(value = "前端组件")
    private String component;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "按钮权限类型")
    private String buttonType;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "说明备注")
    private String description;

    @ApiModelProperty(value = "排序值")
    private BigDecimal sort;

    @ApiModelProperty(value = "是否启用 0启用 -1禁用")
    private Integer status = CommonEnums.STATUS_NORMAL.getId();

    @ApiModelProperty(value = "网页链接")
    private String url;


}