package com.carson.vboot.generator.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.carson.vboot.core.base.VbootBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * @author oufuhua
 */
@Data
@TableName("t_code_detail")
@ApiModel(value = "数据库属性表")
public class CodeDetail extends VbootBaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "代码主键")
    private String codeId;

    @ApiModelProperty(value = "字段名(英文)")
    @NotBlank(message = "字段名不能为空")
    private String name;

    @ApiModelProperty(value = "中文名")
    @NotBlank(message = "中文名不能为空")
    private String chinaName;

    @ApiModelProperty(value = "java类型")
    @NotBlank(message = "java类型不能为空")
    private String javaType;

    @ApiModelProperty(value = "字段类型 varchar,datetime,tinyint,decimal等类型")
    @NotBlank(message = "字段类型不能为空")
    private String nameType;

    @ApiModelProperty(value = "备注 sql使用的")
    private String description;

    @ApiModelProperty(value = "是否必填 0是不必填，1是必填")
    private Integer required;

    @ApiModelProperty(value = "是否显示表格中 0不，1是")
    private Integer tableSite;

    @ApiModelProperty(value = "表单类型：1输入框，单选，多选，时间框，下拉列表，textarea")
    private Integer formType;

    @ApiModelProperty(value = "查询方式 = != >= <= like notNull between")
    private Integer search;

    @ApiModelProperty(value = "数字字典id")
    private String dictKey;

    @ApiModelProperty(value = "排序号")
    private BigDecimal num;

}
