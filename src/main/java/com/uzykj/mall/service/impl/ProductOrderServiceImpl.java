package com.uzykj.mall.service.impl;

import com.uzykj.mall.dao.ProductOrderMapper;
import com.uzykj.mall.entity.OrderGroup;
import com.uzykj.mall.entity.ProductOrder;
import com.uzykj.mall.service.ProductOrderService;
import com.uzykj.mall.util.OrderUtil;
import com.uzykj.mall.util.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Service("productOrderService")
public class ProductOrderServiceImpl implements ProductOrderService,Serializable {
    private ProductOrderMapper productOrderMapper;

    @Resource(name = "productOrderMapper")
    public void setProductOrderMapper(ProductOrderMapper productOrderMapper) {
        this.productOrderMapper = productOrderMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean add(ProductOrder productOrder) {
        return productOrderMapper.insertOne(productOrder) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean update(ProductOrder productOrder) {
        return productOrderMapper.updateOne(productOrder) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public Integer update1(ProductOrder productOrder) {
        return productOrderMapper.update(productOrder);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean deleteList(Integer[] productOrder_id_list) {
        return productOrderMapper.deleteList(productOrder_id_list) > 0;
    }

    @Override
    public List<ProductOrder> getList(ProductOrder productOrder, Byte[] productOrder_status_array, OrderUtil orderUtil, PageUtil pageUtil) {
        return productOrderMapper.select(productOrder, productOrder_status_array, orderUtil, pageUtil);
    }

    @Override
    public List<ProductOrder> getListByUserId(Integer user_id, PageUtil pageUtil) {
        return productOrderMapper.selectByUserId(user_id, pageUtil);
    }


    @Override
    public List<OrderGroup> getTotalByDate(Date beginDate, Date endDate) {
        return productOrderMapper.getTotalByDate(beginDate, endDate);
    }

    @Override
    public ProductOrder get(Integer productOrder_id) {
        return productOrderMapper.selectOne(productOrder_id);
    }

    @Override
    public ProductOrder getByCode(String productOrder_code) {
        return productOrderMapper.selectByCode(productOrder_code);
    }

    @Override
    public Integer getTotal(ProductOrder productOrder, Byte[] productOrder_status_array) {
        return productOrderMapper.selectTotal(productOrder, productOrder_status_array);
    }
}
