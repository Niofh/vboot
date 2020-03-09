package com.carson.vboot.core.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.carson.vboot.core.base.VbootBaseEntity;
import com.carson.vboot.core.common.enums.CommonEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author carson
 */
@TableName("t_role")
@ApiModel(value = "角色")
@Data
public class Role extends VbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色名 以ROLE_开头")
    private String name;

    @ApiModelProperty(value = "是否为注册默认角色")
    private Boolean defaultRole;

    @ApiModelProperty(value = "数据权限类型 0全部默认 1自定义 2本部门及以下 3本部门 4仅本人")
    private Integer dataType = CommonEnums.DATA_TYPE_ALL.getId();

    @ApiModelProperty(value = "备注")
    private String description;


}