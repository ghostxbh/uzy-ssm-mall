package com.uzykj.mall.service;

import com.uzykj.mall.entity.SeckillActivity;

import java.util.List;

/**
 * @author ghostxbh
 * @date 2024/6/10
 * @description 秒杀活动Service接口
 */
public interface SeckillActivityService {
    boolean add(SeckillActivity seckillActivity);
    boolean update(SeckillActivity seckillActivity);
    boolean delete(Integer[] seckill_id_list);
    List<SeckillActivity> getList(SeckillActivity seckillActivity);
    SeckillActivity get(Integer seckill_id);
    SeckillActivity getByProductId(Integer product_id);
    boolean decreaseStock(Integer seckill_id);
}
