package com.carson.vboot.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.carson.vboot.core.base.VbootBaseEntity;
import com.carson.vboot.core.common.constant.CommonConstant;
import com.carson.vboot.core.common.enums.CommonEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * created by Nicofh on 2020-03-08
 */
@Data
@TableName("t_user")
@ApiModel(value = "用户")
public class User extends VbootBaseEntity  {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    @NotNull(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "邮件")
    @Email(message = "不是正确邮箱格式")
    private String email;

    @ApiModelProperty(value = "省市县地址")
    private String address;

    @ApiModelProperty(value = "街道地址")
    private String street;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "密码强度")
    private String passStrength;

    @ApiModelProperty(value = "用户头像")
    private String avatar = CommonConstant.USER_DEFAULT_AVATAR;

    @ApiModelProperty(value = "用户类型 0普通用户 1管理员")
    private Integer type = CommonEnums.USER_TYPE_NORMAL.getId();

    @ApiModelProperty(value = "状态 默认0正常 -1禁用")
    private Integer status = CommonEnums.USER_STATUS_NORMAL.getId();

    @ApiModelProperty(value = "描述/详情/备注")
    private String description;

    @ApiModelProperty(value = "所属部门id")
    private String departmentId;

//   不在列表显示，或者在查看/修改时候渲染，减少数据库压力
//    @TableField(exist = false)
//    @ApiModelProperty(value = "所属部门名称")
//    private String departmentTitle;

    @TableField(exist = false)
    @ApiModelProperty(value = "关联角色id")
    private List<String> roleIds;

}
