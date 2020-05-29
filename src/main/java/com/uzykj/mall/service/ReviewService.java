package com.uzykj.mall.service;

import com.uzykj.mall.entity.Review;
import com.uzykj.mall.util.PageUtil;

import java.util.List;

public interface ReviewService {
    boolean add(Review review);
    boolean update(Review review);
    boolean deleteList(Integer[] review_id_list);
    List<Review> getList(Review review, PageUtil pageUtil);
    List<Review> getListByUserId(Integer user_id, PageUtil pageUtil);
    List<Review> getListByProductId(Integer product_id, PageUtil pageUtil);
    Review get(Integer review_id);
    Integer getTotal(Review review);
    Integer getTotalByUserId(Integer user_id);
    Integer getTotalByProductId(Integer product_id);

    Integer getTotalByOrderItemId(Integer productOrderItem_id);
}
