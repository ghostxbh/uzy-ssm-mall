package cn.jkj521.bookstore.service;

import cn.jkj521.bookstore.entity.Property;
import cn.jkj521.bookstore.util.PageUtil;

import java.util.List;

public interface PropertyService {
    boolean add(Property property);
    boolean addList(List<Property> propertyList);
    boolean update(Property property);
    boolean delete(Integer[] property_id_list);

    List<Property> getList(Property property, PageUtil pageUtil);
    Property get(Integer property_id);
    Integer getTotal(Property property);
}
