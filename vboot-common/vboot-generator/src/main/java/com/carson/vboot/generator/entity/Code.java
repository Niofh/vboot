package com.carson.vboot.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.carson.vboot.core.base.VbootBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@TableName("t_code")
@ApiModel(value = "代码生成")
public class Code  extends VbootBaseEntity {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "表名称")
    @NotNull(message = "表名称不能为空")
    private String tableName;

    @ApiModelProperty(value = "备注")
    @NotNull(message = "备注不能为空")
    private String description;
}
