package com.carson.vboot.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.carson.vboot.core.base.VbootBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author oufuhua
 */
@Data
@ApiModel("角色权限表")
@TableName("t_role_permission")
public class RolePermission extends VbootBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色id")
    private String roleId;

    @ApiModelProperty("权限id")
    private String permissionId;
}
