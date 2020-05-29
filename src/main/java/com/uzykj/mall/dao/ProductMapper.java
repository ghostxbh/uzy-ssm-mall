package com.uzykj.mall.dao;

import com.uzykj.mall.entity.Product;
import com.uzykj.mall.util.OrderUtil;
import com.uzykj.mall.util.PageUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    Integer insertOne(@Param("product") Product product);
    Integer updateOne(@Param("product") Product product);
    Integer delete(@Param("product_id_list")Integer[] product_id_list);
    List<Product> select(@Param("product") Product product, @Param("product_isEnabled_array") Byte[] product_isEnabled_array, @Param("orderUtil") OrderUtil orderUtil, @Param("pageUtil") PageUtil pageUtil);

    List<Product> selectTitle(@Param("product") Product product, @Param("pageUtil") PageUtil pageUtil);
    Product selectOne(@Param("product_id") Integer product_Id);
    Integer selectTotal(@Param("product") Product product,@Param("product_isEnabled_array") Byte[] product_isEnabled_array);

    List<Product> selectMoreList(@Param("product") Product product, @Param("product_isEnabled_array") Byte[] bytes, @Param("orderUtil") OrderUtil orderUtil, @Param("pageUtil") PageUtil pageUtil, @Param("product_name_split") String[] product_name_split);

    Integer selectMoreListTotal(@Param("product") Product product, @Param("product_isEnabled_array") Byte[] product_isEnabled_array, @Param("product_name_split") String[] product_name_split);
}
