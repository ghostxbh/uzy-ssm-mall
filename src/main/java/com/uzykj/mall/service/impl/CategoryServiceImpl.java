package com.uzykj.mall.service.impl;

import com.uzykj.mall.dao.CategoryMapper;
import com.uzykj.mall.entity.Category;
import com.uzykj.mall.service.CategoryService;
import com.uzykj.mall.util.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    private CategoryMapper categoryMapper;

    @Resource(name = "categoryMapper")
    public void setCategoryMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean add(Category category) {
        return categoryMapper.insertOne(category) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean update(Category category) {
        return categoryMapper.updateOne(category) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean delete(Integer[] category_id_list) {
        return categoryMapper.delete(category_id_list) > 0;
    }

    @Override
    public List<Category> getList(String category_name, PageUtil pageUtil) {
        return categoryMapper.select(category_name, pageUtil);
    }

    @Override
    public Category get(Integer category_id) {
        return categoryMapper.selectOne(category_id);
    }

    @Override
    public Integer getTotal(String category_name) {
        return categoryMapper.selectTotal(category_name);
    }
}
