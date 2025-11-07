package com.uzykj.mall.dao;

import com.uzykj.mall.entity.SeckillOrder;
import org.apache.ibatis.annotations.Param;

/**
 * @author ghostxbh
 * @date 2024/6/10
 * @description 秒杀订单Mapper接口
 */
public interface SeckillOrderMapper {
    Integer insertOne(@Param("seckillOrder") SeckillOrder seckillOrder);
    SeckillOrder selectByUserIdAndProductId(@Param("user_id") Integer user_id, @Param("product_id") Integer product_id);
    SeckillOrder selectByOrderId(@Param("seckill_order_id") Integer seckill_order_id);
}
