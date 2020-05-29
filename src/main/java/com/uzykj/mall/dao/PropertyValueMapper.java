package com.uzykj.mall.dao;

import com.uzykj.mall.entity.PropertyValue;
import com.uzykj.mall.util.PageUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PropertyValueMapper {
    Integer insertOne(@Param("propertyValue") PropertyValue propertyValue);

    Integer insertList(@Param("propertyValue_list") List<PropertyValue> propertyValueList);
    Integer updateOne(@Param("propertyValue") PropertyValue propertyValue);
    Integer delete(@Param("propertyValue_id_list") Integer[] propertyValue_id_list);
    List<PropertyValue> selectByProduct(@Param("product_id")Integer product_id,@Param("pageUtil") PageUtil pageUtil);
    List<PropertyValue> select(@Param("propertyValue") PropertyValue propertyValue, @Param("pageUtil") PageUtil pageUtil);
    PropertyValue selectOne(@Param("propertyValue_id") Integer propertyValue_id);
    Integer selectTotal(@Param("propertyValue") PropertyValue propertyValue);
}
