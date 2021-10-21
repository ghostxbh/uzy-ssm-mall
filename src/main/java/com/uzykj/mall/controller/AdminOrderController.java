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

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 后台管理-订单页
 */
@Slf4j
@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {
    @Autowired
    private ProductOrderService productOrderService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ProductOrderItemService productOrderItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;

    //转到后台管理-订单页-ajax
    @GetMapping()
    public String goToPage(HttpSession session, Map<String, Object> map) {
        PageUtil pageUtil = new PageUtil(0, 10);
        List<ProductOrder> productOrderList = productOrderService.getList(null, null, new OrderUtil("productOrder_id", true), pageUtil);
        Integer productOrderCount = productOrderService.getTotal(null, null);
        pageUtil.setTotal(productOrderCount);

        map.put("productOrderList", productOrderList);
        map.put("productOrderCount", productOrderCount);
        map.put("pageUtil", pageUtil);
        return "admin/orderManagePage";
    }

    //转到后台管理-订单详情页-ajax
    @GetMapping("/{oid}")
    public String goToDetailsPage(HttpSession session, Map<String, Object> map,
                                  @PathVariable Integer oid/* 订单ID */) {
        User user = (User) session.getAttribute("USER_SESSION");
        log.info("获取order_id为{}的订单信息", oid);
        ProductOrder order = productOrderService.get(oid);
        Address address = addressService.get(order.getProductOrder_address().getAddress_areaId());
        Stack<String> addressStack = new Stack<>();
        //详细地址
        addressStack.push(order.getProductOrder_detail_address());
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
        log.warn("订单地址字符串：{}", builder);
        order.setProductOrder_detail_address(builder.toString());
        order.setProductOrder_user(user);
        List<ProductOrderItem> productOrderItemList = productOrderItemService.getListByOrderId(oid, null);
        if (productOrderItemList != null) {
            log.info("获取订单详情-订单项对应的产品信息");
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
        order.setProductOrderItemList(productOrderItemList);
        map.put("order", order);
        return "admin/include/orderDetails";
    }

    //更新订单信息-ajax
    @ResponseBody
    @PutMapping("/{order_id}")
    public String updateOrder(@PathVariable("order_id") String order_id) {
        JSONObject jsonObject = new JSONObject();
        ProductOrder productOrder = new ProductOrder()
                .setProductOrder_id(Integer.valueOf(order_id))
                .setProductOrder_status((byte) 2)
                .setProductOrder_delivery_date(new Date());
        log.info("更新订单信息，订单ID值为：{}", order_id);

        boolean yn = productOrderService.update(productOrder);
        if (yn) {
            log.info("更新成功！");
            jsonObject.put("success", true);
        } else {
            log.info("更新失败！事务回滚");
            jsonObject.put("success", false);
            throw new RuntimeException();
        }
        jsonObject.put("order_id", order_id);
        return jsonObject.toJSONString();
    }

    //删除订单
    @ResponseBody
    @GetMapping("/delete/{arr}")
    public String deleteOrder(@PathVariable("arr") Integer[] order_id_list/* 商品id集合 */
    ) {
        log.info("删除:用户id数组：" + order_id_list.toString());
        JSONObject object = new JSONObject();
        if (productOrderService.deleteList(order_id_list)) {
            object.put("success", true);
            log.info("已删除订单数据：" + order_id_list.length + "条");
        } else {
            object.put("success", false);
        }
        return object.toJSONString();
    }

    //按条件查询订单-ajax
    @ResponseBody
    @GetMapping("/order/{index}/{count}")
    public String getOrderBySearch(@RequestParam(required = false) String productOrder_code/* 订单号 */,
                                   @RequestParam(required = false) String productOrder_post/* 订单邮政编码 */,
                                   @RequestParam(required = false) Byte[] productOrder_status_array/* 订单状态数组 */,
                                   @RequestParam(required = false) String orderBy/* 排序字段 */,
                                   @RequestParam(required = false, defaultValue = "true") Boolean isDesc/* 是否倒序 */,
                                   @PathVariable Integer index/* 页数 */,
                                   @PathVariable Integer count/* 行数 */) {
        //移除不必要条件
        if (productOrder_status_array != null && (productOrder_status_array.length <= 0 || productOrder_status_array.length >= 5)) {
            productOrder_status_array = null;
        }
        if (productOrder_code != null) {
            productOrder_code = productOrder_code.equals("") ? null : productOrder_code;
        }
        if (productOrder_post != null) {
            productOrder_post = productOrder_post.equals("") ? null : productOrder_post;
        }
        if (orderBy != null && orderBy.equals("")) {
            orderBy = null;
        }
        //封装查询条件
        ProductOrder productOrder = new ProductOrder()
                .setProductOrder_code(productOrder_code)
                .setProductOrder_post(productOrder_post);
        OrderUtil orderUtil = null;
        if (orderBy != null) {
            log.info("根据{}排序，是否倒序:{}", orderBy, isDesc);
            orderUtil = new OrderUtil(orderBy, isDesc);
        }

        JSONObject object = new JSONObject();
        log.info("按条件获取第{}页的{}条订单", index + 1, count);
        PageUtil pageUtil = new PageUtil(index, count);
        List<ProductOrder> productOrderList = productOrderService.getList(productOrder, productOrder_status_array, orderUtil, pageUtil);
        object.put("productOrderList", JSONArray.parseArray(JSON.toJSONString(productOrderList)));
        log.info("按条件获取订单总数量");
        Integer productOrderCount = productOrderService.getTotal(productOrder, productOrder_status_array);
        object.put("productOrderCount", productOrderCount);
        log.info("获取分页信息");
        pageUtil.setTotal(productOrderCount);
        object.put("totalPage", pageUtil.getTotalPage());
        object.put("pageUtil", pageUtil);

        return object.toJSONString();
    }
}
