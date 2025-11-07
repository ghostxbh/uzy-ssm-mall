package com.uzykj.mall.service;

import com.uzykj.mall.entity.SeckillOrder;

/**
 * @author ghostxbh
 * @date 2024/6/10
 * @description 秒杀订单Service接口
 */
public interface SeckillOrderService {
    boolean add(SeckillOrder seckillOrder);
    SeckillOrder getByUserIdAndProductId(Integer user_id, Integer product_id);
    SeckillOrder getByOrderId(Integer seckill_order_id);
}
