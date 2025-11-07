package com.uzykj.mall.service.impl;

import com.uzykj.mall.dao.SeckillOrderMapper;
import com.uzykj.mall.entity.SeckillOrder;
import com.uzykj.mall.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ghostxbh
 * @date 2024/6/10
 * @description 秒杀订单Service实现类
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean add(SeckillOrder seckillOrder) {
        return seckillOrderMapper.insertOne(seckillOrder) > 0;
    }

    @Override
    public SeckillOrder getByUserIdAndProductId(Integer user_id, Integer product_id) {
        return seckillOrderMapper.selectByUserIdAndProductId(user_id, product_id);
    }

    @Override
    public SeckillOrder getByOrderId(Integer seckill_order_id) {
        return seckillOrderMapper.selectByOrderId(seckill_order_id);
    }
}
