package cn.jkj521.bookstore.service.impl;

import cn.jkj521.bookstore.dao.PropertyValueMapper;
import cn.jkj521.bookstore.entity.PropertyValue;
import cn.jkj521.bookstore.service.PropertyValueService;
import cn.jkj521.bookstore.util.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service("propertyValueService")
public class PropertyValueServiceImpl implements PropertyValueService {
    private PropertyValueMapper propertyValueMapper;

    @Resource(name = "propertyValueMapper")
    public void setPropertyValueMapper(PropertyValueMapper propertyValueMapper) {
        this.propertyValueMapper = propertyValueMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean add(PropertyValue propertyValue) {
        return propertyValueMapper.insertOne(propertyValue) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean addList(List<PropertyValue> propertyValueList) {
        return propertyValueMapper.insertList(propertyValueList) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean update(PropertyValue propertyValue) {
        return propertyValueMapper.updateOne(propertyValue) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean delete(Integer[] propertyValue_id_list) {
        return propertyValueMapper.delete(propertyValue_id_list)>0;
    }

    @Override
    public List<PropertyValue> getList(PropertyValue propertyValue, PageUtil pageUtil) {
        return propertyValueMapper.select(propertyValue, pageUtil);
    }

    @Override
    public List<PropertyValue> getListByProductId(Integer productId, PageUtil pageUtil) {
        return propertyValueMapper.selectByProduct(productId, pageUtil);
    }

    @Override
    public PropertyValue get(Integer propertyValue_id) {
        return propertyValueMapper.selectOne(propertyValue_id);
    }

    @Override
    public Integer getTotal(PropertyValue propertyValue) {
        return propertyValueMapper.selectTotal(propertyValue);
    }
}
