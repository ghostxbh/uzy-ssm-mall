package com.uzykj.mall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uzykj.mall.entity.*;
import com.uzykj.mall.service.*;
import com.uzykj.mall.util.OrderUtil;
import com.uzykj.mall.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 后台管理-用户页
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ProductOrderItemService productOrderItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductOrderService productOrderService;

    //转到后台管理-用户页-ajax
    @GetMapping("/user")
    public String goUserManagePage(Map<String, Object> map) {
        PageUtil pageUtil = new PageUtil(0, 10);
        List<User> userList = userService.getList(null, null, pageUtil);
        Integer userCount = userService.getTotal(null);
        pageUtil.setTotal(userCount);

        map.put("userList", userList);
        map.put("userCount", userCount);
        map.put("pageUtil", pageUtil);
        return "admin/userManagePage";
    }


    //转到后台管理-用户详情页-ajax
    @GetMapping("/user/{uid}")
    public String getUserById(Map<String, Object> map,
                              @PathVariable Integer uid/* 用户ID */) {
        log.info("获取user_id为{}的用户信息", uid);
        User user = userService.get(uid);
        Address address = addressService.get(user.getUser_address().getAddress_areaId());
        Stack<String> addressStack = new Stack<>();
        //最后一级地址
        addressStack.push(address.getAddress_name() + " ");
        //如果不是第一级地址
        while (!address.getAddress_areaId().equals(address.getAddress_regionId().getAddress_areaId())) {
            address = addressService.get(address.getAddress_regionId().getAddress_areaId());
            addressStack.push(address.getAddress_name() + " ");
        }
        StringBuilder builder = new StringBuilder();
        while (!addressStack.empty()) {
            builder.append(addressStack.pop());
        }
        log.info("所在地地址字符串：{}", builder);
        user.setUser_address(new Address().setAddress_name(builder.toString()));

        log.info("获取用户详情-家乡地址信息");
        address = addressService.get(user.getUser_homeplace().getAddress_areaId());
        //最后一级地址
        addressStack.push(address.getAddress_name() + " ");
        //如果不是第一级地址
        while (!address.getAddress_areaId().equals(address.getAddress_regionId().getAddress_areaId())) {
            address = addressService.get(address.getAddress_regionId().getAddress_areaId());
            addressStack.push(address.getAddress_name() + " ");
        }
        builder = new StringBuilder();
        while (!addressStack.empty()) {
            builder.append(addressStack.pop());
        }
        log.info("家乡地址字符串：{}", builder);
        user.setUser_homeplace(new Address().setAddress_name(builder.toString()));

        log.info("获取用户详情-购物车订单项信息");
        List<ProductOrderItem> productOrderItemList = productOrderItemService.getListByUserId(user.getUser_id(), null);
        if (productOrderItemList != null) {
            log.info("获取用户详情-购物车订单项对应的产品信息");
            for (ProductOrderItem productOrderItem : productOrderItemList) {
                Integer productId = productOrderItem.getProductOrderItem_product().getProduct_id();
                log.warn("获取产品ID为{}的产品信息", productId);
                Product product = productService.get(productId);
                if (product != null) {
                    log.warn("获取产品ID为{}的第一张预览图片信息", productId);
                    product.setSingleProductImageList(productImageService.getList(productId, new PageUtil(0, 1)));
                }
                productOrderItem.setProductOrderItem_product(product);
            }
        }
        user.setProductOrderItemList(productOrderItemList);

        if (user.getUser_realname() != null) {
            log.info("用户隐私加密");
            user.setUser_realname(user.getUser_realname().substring(0, 1) + "*");
        } else {
            user.setUser_realname("未命名");
        }

        map.put("user", user);
        return "admin/include/userDetails";
    }

    //按条件查询用户-ajax
    @ResponseBody
    @GetMapping("/user/{index}/{count}")
    public String getUserBySearch(@RequestParam(required = false) String user_name/* 用户名称 */,
                                  @RequestParam(required = false) Byte[] user_gender_array/* 用户性别数组 */,
                                  @RequestParam(required = false) String orderBy/* 排序字段 */,
                                  @RequestParam(required = false, defaultValue = "true") Boolean isDesc/* 是否倒序 */,
                                  @PathVariable Integer index/* 页数 */,
                                  @PathVariable Integer count/* 行数 */) throws UnsupportedEncodingException {
        //移除不必要条件
        Byte gender = null;
        if (user_gender_array != null && user_gender_array.length == 1) {
            gender = user_gender_array[0];
        }

        if (user_name != null) {
            //如果为非空字符串则解决中文乱码：URLDecoder.decode(String,"UTF-8");
            user_name = user_name.equals("") ? null : URLDecoder.decode(user_name, "UTF-8");
        }
        if (orderBy != null && orderBy.equals("")) {
            orderBy = null;
        }
        //封装查询条件
        User user = new User()
                .setUser_name(user_name)
                .setUser_gender(gender);

        OrderUtil orderUtil = null;
        if (orderBy != null) {
            log.info("根据{}排序，是否倒序:{}", orderBy, isDesc);
            orderUtil = new OrderUtil(orderBy, isDesc);
        }

        JSONObject object = new JSONObject();
        log.info("按条件获取第{}页的{}条用户", index + 1, count);
        PageUtil pageUtil = new PageUtil(index, count);
        List<User> userList = userService.getList(user, orderUtil, pageUtil);
        Integer userCount = userService.getTotal(user);
        pageUtil.setTotal(userCount);

        object.put("userList", JSONArray.parseArray(JSON.toJSONString(userList)));
        object.put("userCount", userCount);
        object.put("totalPage", pageUtil.getTotalPage());
        object.put("pageUtil", pageUtil);

        return object.toJSONString();
    }

    //删除用户
    @ResponseBody
    @GetMapping("/user/delete/{arr}")
    public String deleteUser(@PathVariable("arr") Integer[] user_id_list/* 用户id集合 */
    ) {
        log.info("删除:用户id数组：" + user_id_list.toString());
        for (int i = 0; i < user_id_list.length; i++) {

            //删除评论
            List<Review> reviewList = reviewService.getListByUserId(user_id_list[i], null);
            if (reviewList != null && reviewList.size() != 0) {
                Integer[] review_id_list = new Integer[reviewList.size()];
                for (int k = 0; k < reviewList.size(); k++) {
                    review_id_list[k] = reviewList.get(k).getReview_id();
                }
                if (reviewService.deleteList(review_id_list)) {
                    log.info("已删除评论：" + review_id_list.length + "条");
                } else {
                    throw new RuntimeException("删除评论异常");
                }
            }

            //删除订单
            List<ProductOrderItem> productOrderItemList = productOrderItemService.getListByUserId(user_id_list[i], null);
            if (productOrderItemList.size() != 0 && productOrderItemList != null) {
                Integer[] productOrder_id_list = new Integer[productOrderItemList.size()];
                Integer[] productOrderItem_id_list = new Integer[productOrderItemList.size()];
                for (int k = 0; k < productOrderItemList.size(); k++) {
                    productOrder_id_list[k] = productOrderItemList.get(k).getProductOrderItem_order().getProductOrder_id();
                    productOrderItem_id_list[k] = productOrderItemList.get(k).getProductOrderItem_id();
                }

                if (productOrderItemService.deleteList(productOrderItem_id_list)) {
                    log.info("已删除订单辅助表数据：" + productOrderItem_id_list.length + "条");
                } else {
                    throw new RuntimeException("删除订单辅助表数据异常");
                }

                if (productOrderService.deleteList(productOrder_id_list)) {
                    log.info("已删除订单表数据：" + productOrder_id_list.length + "条");
                } else {
                    throw new RuntimeException("删除订单表数据异常");
                }
            }
        }
        JSONObject object = new JSONObject();
        if (userService.delete(user_id_list)) {
            object.put("success", true);
            log.info("已删除用户：" + user_id_list.length + "条");
        } else {
            object.put("success", false);
        }
        return object.toJSONString();
    }
}
