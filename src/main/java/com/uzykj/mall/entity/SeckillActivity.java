package com.uzykj.mall.entity;

import java.util.Date;

/**
 * @author ghostxbh
 * @date 2024/6/10
 * @description 秒杀活动实体类
 */
public class SeckillActivity {
    private Integer seckill_id;
    private Integer product_id;
    private Integer seckill_stock;
    private Double seckill_price;
    private Date start_time;
    private Date end_time;
    private Byte status;

    // getter和setter方法
    public Integer getSeckill_id() {
        return seckill_id;
    }

    public SeckillActivity setSeckill_id(Integer seckill_id) {
        this.seckill_id = seckill_id;
        return this;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public SeckillActivity setProduct_id(Integer product_id) {
        this.product_id = product_id;
        return this;
    }

    public Integer getSeckill_stock() {
        return seckill_stock;
    }

    public SeckillActivity setSeckill_stock(Integer seckill_stock) {
        this.seckill_stock = seckill_stock;
        return this;
    }

    public Double getSeckill_price() {
        return seckill_price;
    }

    public SeckillActivity setSeckill_price(Double seckill_price) {
        this.seckill_price = seckill_price;
        return this;
    }

    public Date getStart_time() {
        return start_time;
    }

    public SeckillActivity setStart_time(Date start_time) {
        this.start_time = start_time;
        return this;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public SeckillActivity setEnd_time(Date end_time) {
        this.end_time = end_time;
        return this;
    }

    public Byte getStatus() {
        return status;
    }

    public SeckillActivity setStatus(Byte status) {
        this.status = status;
        return this;
    }
}
