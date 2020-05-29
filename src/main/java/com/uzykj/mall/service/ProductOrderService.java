package com.uzykj.mall.service;

import com.uzykj.mall.entity.OrderGroup;
import com.uzykj.mall.entity.ProductOrder;
import com.uzykj.mall.util.OrderUtil;
import com.uzykj.mall.util.PageUtil;

import java.util.Date;
import java.util.List;

public interface ProductOrderService {
    boolean add(ProductOrder productOrder);

    boolean update(ProductOrder productOrder);

    Integer update1(ProductOrder productOrder);

    boolean deleteList(Integer[] productOrder_id_list);

    List<ProductOrder> getList(ProductOrder productOrder, Byte[] productOrder_status_array, OrderUtil orderUtil, PageUtil pageUtil);

    List<ProductOrder> getListByUserId(Integer user_id, PageUtil pageUtil);

    List<OrderGroup> getTotalByDate(Date beginDate, Date endDate);

    ProductOrder get(Integer productOrder_id);

    ProductOrder getByCode(String productOrder_code);

    Integer getTotal(ProductOrder productOrder, Byte[] productOrder_status_array);
}
