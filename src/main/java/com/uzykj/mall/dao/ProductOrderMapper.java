package com.uzykj.mall.dao;

import com.uzykj.mall.entity.OrderGroup;
import com.uzykj.mall.entity.ProductOrder;
import com.uzykj.mall.util.OrderUtil;
import com.uzykj.mall.util.PageUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

public interface ProductOrderMapper {
    Integer insertOne(@Param("productOrder") ProductOrder productOrder);

    Integer updateOne(@Param("productOrder") ProductOrder productOrder);

    @Update("UPDATE productorder SET productorder_status = #{productOrder.productOrder_status} WHERE productorder_id = #{productOrder.productOrder_id}")
    Integer update(@Param("productOrder") ProductOrder productOrder);

    Integer deleteList(@Param("productOrder_id_list") Integer[] productOrder_id_list);

    List<ProductOrder> select(@Param("productOrder") ProductOrder productOrder, @Param("productOrder_status_array") Byte[] productOrder_status_array, @Param("orderUtil") OrderUtil orderUtil, @Param("pageUtil") PageUtil pageUtil);

    ProductOrder selectOne(@Param("productOrder_id") Integer productOrder_id);

    ProductOrder selectByCode(@Param("productOrder_code") String productOrder_code);

    Integer selectTotal(@Param("productOrder") ProductOrder productOrder, @Param("productOrder_status_array") Byte[] productOrder_status_array);

    List<OrderGroup> getTotalByDate(@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    List<ProductOrder> selectByUserId(@Param("user_id") Integer user_id, @Param("pageUtil") PageUtil pageUtil);
}
