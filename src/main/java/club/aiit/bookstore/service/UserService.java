package club.aiit.bookstore.service;

import club.aiit.bookstore.entity.User;
import club.aiit.bookstore.util.OrderUtil;
import club.aiit.bookstore.util.PageUtil;

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
