package com.uzykj.mall.dao;

import com.uzykj.mall.entity.Admin;
import com.uzykj.mall.util.PageUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminMapper {
    Integer insertOne(@Param("admin") Admin admin);
    Integer updateOne(@Param("admin") Admin admin);
    List<Admin> select(@Param("admin_name") String admin_name, @Param("pageUtil") PageUtil pageUtil);
    Admin selectOne(@Param("admin_name") String admin_name, @Param("admin_id") Integer admin_id);
    Admin selectByLogin(@Param("admin_name") String admin_name, @Param("admin_password") String admin_password);
    Integer selectTotal(@Param("admin_name") String admin_name);
}