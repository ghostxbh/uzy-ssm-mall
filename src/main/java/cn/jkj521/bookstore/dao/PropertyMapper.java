package cn.jkj521.bookstore.dao;

import cn.jkj521.bookstore.entity.Property;
import cn.jkj521.bookstore.util.PageUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PropertyMapper {
    Integer insertOne(@Param("property") Property property);

    Integer insertList(@Param("property_list") List<Property> propertyList);

    Integer updateOne(@Param("property") Property property);

    Integer delete(@Param("property_id_list") Integer[] property_id_list);

    List<Property> select(@Param("property") Property property, @Param("pageUtil") PageUtil pageUtil);

    Property selectOne(@Param("property_id") Integer property_id);

    Integer selectTotal(@Param("property") Property property);
}
