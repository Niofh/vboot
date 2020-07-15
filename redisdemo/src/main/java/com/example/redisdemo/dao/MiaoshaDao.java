package com.example.redisdemo.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.redisdemo.entity.Miaosha;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface MiaoshaDao extends BaseMapper<Miaosha> {

    @Update("update t_miaosha set stock=stock+#{number} where sku_id = #{skuId}")
    int addStock(@Param("skuId") String skuId, @Param("number") int number);

    @Update("update t_miaosha set stock=stock-#{number} where sku_id = #{skuId}")
    int reduceStock(@Param("skuId") String skuId, @Param("number") int number);
}

