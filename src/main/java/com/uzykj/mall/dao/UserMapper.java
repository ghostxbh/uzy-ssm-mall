package com.uzykj.mall.dao;

import com.uzykj.mall.entity.User;
import com.uzykj.mall.util.OrderUtil;
import com.uzykj.mall.util.PageUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    Integer insertOne(@Param("user") User user);
    Integer updateOne(@Param("user") User user);
    Integer delete(@Param("user_id_list")Integer[] user_id_list);
    List<User> select(@Param("user") User user, @Param("orderUtil") OrderUtil orderUtil, @Param("pageUtil") PageUtil pageUtil);
    User selectOne(@Param("user_id") Integer user_id);
    User selectByLogin(@Param("user_name") String user_name, @Param("user_password") String user_password);
    Integer selectTotal(@Param("user") User user);
}
