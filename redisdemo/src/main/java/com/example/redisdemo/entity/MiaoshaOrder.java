package com.example.redisdemo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_miaosha_order")
public class MiaoshaOrder  extends VbootBaseEntity {
    private static final long serialVersionUID = 1L;

    private String skuId;

    private String userId;

    // 是否付款
    private Integer pay;


}
