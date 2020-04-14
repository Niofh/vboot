package com.carson.vboot.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.carson.vboot.core.base.VbootBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@TableName("t_code")
@ApiModel(value = "代码生成")
public class Code  extends VbootBaseEntity {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "生成文件名称")
    @NotBlank(message = "生成文件名称不能为空")
    private String name;

    @ApiModelProperty(value = "包路径")
    @NotBlank(message = "包路径不为空")
    private String packageName;

    @ApiModelProperty(value = "表名称")
    @NotBlank(message = "表名称不能为空")
    private String tableName;

    @ApiModelProperty(value = "备注")
    @NotBlank(message = "备注不能为空")
    private String description;
}
