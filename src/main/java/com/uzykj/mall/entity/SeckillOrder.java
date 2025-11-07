package com.uzykj.mall.entity;

import java.util.Date;

/**
 * @author ghostxbh
 * @date 2024/6/10
 * @description 秒杀订单实体类
 */
public class SeckillOrder {
    private Integer seckill_order_id;
    private Integer user_id;
    private Integer product_id;
    private Integer seckill_id;
    private Double seckill_price;
    private Date create_time;
    private Byte status;

    // getter和setter方法
    public Integer getSeckill_order_id() {
        return seckill_order_id;
    }

    public SeckillOrder setSeckill_order_id(Integer seckill_order_id) {
        this.seckill_order_id = seckill_order_id;
        return this;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public SeckillOrder setUser_id(Integer user_id) {
        this.user_id = user_id;
        return this;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public SeckillOrder setProduct_id(Integer product_id) {
        this.product_id = product_id;
        return this;
    }

    public Integer getSeckill_id() {
        return seckill_id;
    }

    public SeckillOrder setSeckill_id(Integer seckill_id) {
        this.seckill_id = seckill_id;
        return this;
    }

    public Double getSeckill_price() {
        return seckill_price;
    }

    public SeckillOrder setSeckill_price(Double seckill_price) {
        this.seckill_price = seckill_price;
        return this;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public SeckillOrder setCreate_time(Date create_time) {
        this.create_time = create_time;
        return this;
    }

    public Byte getStatus() {
        return status;
    }

    public SeckillOrder setStatus(Byte status) {
        this.status = status;
        return this;
    }
}
