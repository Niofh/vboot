package com.carson.vboot.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.carson.vboot.core.base.VbootBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * @author carson
 * 用户角色关联表
 * 一个用户有多个角色
 */
@Data
@TableName("t_user_role")
@ApiModel(value = "用户角色")
public class UserRole extends VbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户唯一id")
    private String userId;

    @ApiModelProperty(value = "角色唯一id")
    private String roleId;

    @TableField(exist=false)
    @ApiModelProperty(value = "角色名")
    private String roleName;
}
