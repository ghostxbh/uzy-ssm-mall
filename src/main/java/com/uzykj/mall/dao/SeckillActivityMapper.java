package com.uzykj.mall.dao;

import com.uzykj.mall.entity.SeckillActivity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ghostxbh
 * @date 2024/6/10
 * @description 秒杀活动Mapper接口
 */
public interface SeckillActivityMapper {
    Integer insertOne(@Param("seckillActivity") SeckillActivity seckillActivity);
    Integer updateOne(@Param("seckillActivity") SeckillActivity seckillActivity);
    Integer delete(@Param("seckill_id_list") Integer[] seckill_id_list);
    List<SeckillActivity> select(@Param("seckillActivity") SeckillActivity seckillActivity);
    SeckillActivity selectOne(@Param("seckill_id") Integer seckill_id);
    SeckillActivity selectByProductId(@Param("product_id") Integer product_id);
    Integer decreaseStock(@Param("seckill_id") Integer seckill_id);
}
