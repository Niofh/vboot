package com.carson.vboot.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.carson.vboot.core.base.VbootBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@TableName("t_dict_detail")
@ApiModel(value = "字典详情表")
public class DictDetail extends VbootBaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "所属字典id")
    @NotBlank(message = "所属字典Id不能为空")
    private String dictId;

    @ApiModelProperty(value = "字典名称")
    @NotBlank(message = "字典名称不能为空")
    private String name;

    @ApiModelProperty(value = "字典值")
    @NotBlank(message = "字典值不能为空")
    private Integer code;

    @ApiModelProperty(value = "排序")
    private BigDecimal sort;


}