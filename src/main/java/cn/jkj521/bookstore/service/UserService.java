package cn.jkj521.bookstore.service;

import cn.jkj521.bookstore.entity.User;
import cn.jkj521.bookstore.util.OrderUtil;
import cn.jkj521.bookstore.util.PageUtil;

import java.util.List;

public interface UserService {
    boolean add(User user);
    boolean update(User user);
    boolean delete(Integer[] user_id_list);
    List<User> getList(User user, OrderUtil orderUtil, PageUtil pageUtil);
    User get(Integer user_id);
    User login(String user_name, String user_password);
    Integer getTotal(User user);
}
