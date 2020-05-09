package com.carson.vboot.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.carson.vboot.core.base.VbootBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@TableName("t_message")
@ApiModel(value = "消息表")
public class Message extends VbootBaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "文章内容")
    @NotBlank(message = "文章内容不能为空")
    private String content;

    @ApiModelProperty(value = "消息类型")
    @NotBlank(message = "消息类型不能为空")
    private String msgType;

    @ApiModelProperty(value = "新账号是否发送消息")
    private Integer createUserSend;


}
