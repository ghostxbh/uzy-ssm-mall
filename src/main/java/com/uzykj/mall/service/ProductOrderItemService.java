package com.uzykj.mall.service;

import com.uzykj.mall.entity.OrderGroup;
import com.uzykj.mall.entity.ProductOrderItem;
import com.uzykj.mall.util.PageUtil;

import java.util.Date;
import java.util.List;


public interface ProductOrderItemService {
    boolean add(ProductOrderItem productOrderItem);

    boolean update(ProductOrderItem productOrderItem);

    boolean deleteList(Integer[] productOrderItem_id_list);

    List<ProductOrderItem> getList(PageUtil pageUtil);

    List<ProductOrderItem> getListByOrderId(Integer order_id, PageUtil pageUtil);

    List<ProductOrderItem> getListByUserId(Integer user_id, PageUtil pageUtil);

    List<ProductOrderItem> getListByProductId(Integer product_id, PageUtil pageUtil);

    ProductOrderItem get(Integer productOrderItem_id);

    Integer getTotal();

    Integer getTotalByOrderId(Integer order_id);

    Integer getTotalByUserId(Integer user_id);

    ProductOrderItem getByOrder(Integer order_id);

    List<OrderGroup> getTotalByProductId(Integer product_id, Date beginDate, Date endDate);

    Integer getSaleCountByProductId(Integer product_id);
}
