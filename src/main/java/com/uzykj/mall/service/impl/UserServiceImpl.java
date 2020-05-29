package com.uzykj.mall.service.impl;

import com.uzykj.mall.dao.UserMapper;
import com.uzykj.mall.entity.User;
import com.uzykj.mall.service.UserService;
import com.uzykj.mall.util.OrderUtil;
import com.uzykj.mall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean add(User user) {
        return userMapper.insertOne(user) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean update(User user) {
        return userMapper.updateOne(user) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean delete(Integer[] user_id_list) {
        return userMapper.delete(user_id_list) > 0;
    }

    @Override
    public List<User> getList(User user, OrderUtil orderUtil, PageUtil pageUtil) {
        return userMapper.select(user, orderUtil, pageUtil);
    }

    @Override
    public User get(Integer user_id) {
        return userMapper.selectOne(user_id);
    }

    @Override
    public User login(String user_name, String user_password) {
        return userMapper.selectByLogin(user_name, user_password);
    }

    @Override
    public Integer getTotal(User user) {
        return userMapper.selectTotal(user);
    }
}
