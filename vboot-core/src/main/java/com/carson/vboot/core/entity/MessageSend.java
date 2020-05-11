package com.carson.vboot.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.carson.vboot.core.base.VbootBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_message_send")
@ApiModel(value = "消息接受者")
public class MessageSend  extends VbootBaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息id")
    private String messageId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "状态(0未读，1已读，2回收站）")
    private Integer status;


}
