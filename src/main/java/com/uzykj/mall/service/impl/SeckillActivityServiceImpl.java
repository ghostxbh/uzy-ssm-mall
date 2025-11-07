package com.uzykj.mall.service.impl;

import com.uzykj.mall.dao.SeckillActivityMapper;
import com.uzykj.mall.entity.SeckillActivity;
import com.uzykj.mall.service.SeckillActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ghostxbh
 * @date 2024/6/10
 * @description 秒杀活动Service实现类
 */
@Service
public class SeckillActivityServiceImpl implements SeckillActivityService {
    @Autowired
    private SeckillActivityMapper seckillActivityMapper;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean add(SeckillActivity seckillActivity) {
        return seckillActivityMapper.insertOne(seckillActivity) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean update(SeckillActivity seckillActivity) {
        return seckillActivityMapper.updateOne(seckillActivity) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean delete(Integer[] seckill_id_list) {
        return seckillActivityMapper.delete(seckill_id_list) > 0;
    }

    @Override
    public List<SeckillActivity> getList(SeckillActivity seckillActivity) {
        return seckillActivityMapper.select(seckillActivity);
    }

    @Override
    public SeckillActivity get(Integer seckill_id) {
        return seckillActivityMapper.selectOne(seckill_id);
    }

    @Override
    public SeckillActivity getByProductId(Integer product_id) {
        return seckillActivityMapper.selectByProductId(product_id);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean decreaseStock(Integer seckill_id) {
        return seckillActivityMapper.decreaseStock(seckill_id) > 0;
    }
}
