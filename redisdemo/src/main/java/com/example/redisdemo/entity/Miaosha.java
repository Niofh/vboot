package com.example.redisdemo.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.util.Date;


@Data
@TableName("t_miaosha")
public class Miaosha extends VbootBaseEntity {
    private static final long serialVersionUID = 1L;

    private String skuId;

    private Integer stock;

    private Date startTime;

    private Date endTime;


}
