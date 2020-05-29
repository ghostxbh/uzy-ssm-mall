package com.uzykj.mall.dao;

import com.uzykj.mall.entity.Category;
import com.uzykj.mall.util.PageUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    Integer insertOne(@Param("category") Category category);
    Integer updateOne(@Param("category") Category category);
    Integer delete(@Param("category_id_list")Integer[] category_id_list);
    List<Category> select(@Param("category_name") String category_name, @Param("pageUtil") PageUtil pageUtil);
    Category selectOne(@Param("category_id") Integer category_id);
    Integer selectTotal(@Param("category_name") String category_name);
}