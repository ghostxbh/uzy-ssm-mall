package com.uzykj.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.uzykj.mall.entity.*;
import com.uzykj.mall.service.*;
import com.uzykj.mall.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/review")
public class ForeReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductOrderItemService productOrderItemService;
    @Autowired
    private ProductOrderService productOrderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;

    //转到前台-评论添加页
    @GetMapping("/{orderItem_id}")
    public String goToPage(HttpSession session, Map<String, Object> map,
                           @PathVariable("orderItem_id") Integer orderItem_id) {
        User user = (User) session.getAttribute("USER_SESSION");
        map.put("user", user);

        ProductOrderItem orderItem = productOrderItemService.get(orderItem_id);
        JSONObject checkoutOrderNoStatus = this.checkoutOrderNoStatus(orderItem, orderItem_id);
        if (!(Boolean) checkoutOrderNoStatus.get("miss")) {
            return (String) checkoutOrderNoStatus.get("url");
        }

        // 获取订单项所属产品信息和产品评论信息
        Product product = productService.get(orderItem.getProductOrderItem_product().getProduct_id());
        product.setProduct_review_count(reviewService.getTotalByProductId(product.getProduct_id()));
        product.setSingleProductImageList(productImageService.getList(product.getProduct_id(), new PageUtil(0, 1)));
        orderItem.setProductOrderItem_product(product);

        map.put("orderItem", orderItem);
        return "fore/addReview";
    }

    //添加一条评论
    @PostMapping()
    public String addReview(HttpSession session, Map<String, Object> map,
                            @RequestParam Integer orderItem_id,
                            @RequestParam String review_content) throws UnsupportedEncodingException {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        ProductOrderItem orderItem = productOrderItemService.get(orderItem_id);
        JSONObject checkoutOrderNoStatus = this.checkoutOrderNoStatus(orderItem, orderItem_id);
        if (!(Boolean) checkoutOrderNoStatus.get("miss")) {
            return (String) checkoutOrderNoStatus.get("url");
        }
        Review review = new Review()
                .setReview_product(orderItem.getProductOrderItem_product())
                .setReview_content(new String(review_content.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8))
                .setReview_createDate(new Date())
                .setReview_user(new User().setUser_id(userId))
                .setReview_orderItem(orderItem);

        Boolean yn = reviewService.add(review);
        if (!yn) {
            throw new RuntimeException();
        }

        return "redirect:/product/" + orderItem.getProductOrderItem_product().getProduct_id();
    }

    //获取产品评论信息-ajax
    @ResponseBody
    @GetMapping()
    public String getReviewInfo(@RequestParam("product_id") Integer product_id,
                                @RequestParam("index") Integer index/* 页数 */,
                                @RequestParam("count") Integer count/* 行数*/) {
        List<Review> reviewList = reviewService.getListByProductId(product_id, new PageUtil(index, 10));
        if (reviewList != null) {
            for (Review review : reviewList) {
                review.setReview_user(userService.get(review.getReview_user().getUser_id()));
            }
        }
        Integer total = reviewService.getTotalByProductId(product_id);

        JSONObject object = new JSONObject();
        object.put("reviewList", reviewList);
        object.put("pageUtil", new PageUtil().setTotal(total).setIndex(index).setCount(count));
        return object.toJSONString();
    }

    private JSONObject checkoutOrderNoStatus(ProductOrderItem orderItem, Integer orderItem_id) {
        JSONObject object = new JSONObject();
        object.put("miss", true);
        object.put("url", "redirect:/order/0/10");
        if (orderItem == null) {
            log.warn("订单项不存在，返回订单页");
            object.put("miss", false);
            return object;
        }

        if (orderItem.getProductOrderItem_order() == null) {
            log.warn("订单项状态有误，返回订单页");
            object.put("miss", false);
            return object;
        }

        ProductOrder order = productOrderService.get(orderItem.getProductOrderItem_order().getProductOrder_id());
        if (order == null || order.getProductOrder_status() != 3) {
            log.warn("订单项状态有误，返回订单页");
            object.put("miss", false);
            return object;
        }

        if (reviewService.getTotalByOrderItemId(orderItem_id) > 0) {
            log.warn("订单项所属商品已被评价，返回订单页");
            object.put("miss", false);
            return object;
        }
        return object;
    }
}
