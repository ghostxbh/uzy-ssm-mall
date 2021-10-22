package com.uzykj.mall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.uzykj.mall.entity.*;
import com.uzykj.mall.service.*;
import com.uzykj.mall.util.OrderUtil;
import com.uzykj.mall.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 前台-订单页
 */
@Slf4j
@Controller
@RequestMapping("/order")
public class ForeOrderController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductOrderItemService productOrderItemService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductOrderService productOrderService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private LastIDService lastIDService;

    //转到前台-订单列表页
    @GetMapping("/")
    public String goToPageSimple() {
        return "redirect:/order/0/10";
    }

    @GetMapping("/{index}/{count}")
    public String goToPage(HttpSession session, Map<String, Object> map,
                           @RequestParam(required = false) Byte status,
                           @PathVariable("index") Integer index/* 页数 */,
                           @PathVariable("count") Integer count/* 行数*/) {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        Byte[] status_array = null;
        if (status != null) {
            status_array = new Byte[]{status};
        }

        PageUtil pageUtil = new PageUtil(index, count);
        log.info("根据用户ID:{}获取订单列表", userId);
        List<ProductOrder> productOrderList = productOrderService.getList(new ProductOrder().setProductOrder_user(user), status_array, new OrderUtil("productOrder_id", true), pageUtil);

        //订单总数量
        Integer orderCount = 0;
        if (productOrderList.size() > 0) {
            orderCount = productOrderService.getTotal(new ProductOrder().setProductOrder_user(user), status_array);
            for (ProductOrder order : productOrderList) {
                List<ProductOrderItem> productOrderItemList = productOrderItemService.getListByOrderId(order.getProductOrder_id(), null);
                if (productOrderItemList != null) {
                    for (ProductOrderItem productOrderItem : productOrderItemList) {
                        Integer product_id = productOrderItem.getProductOrderItem_product().getProduct_id();
                        Product product = productService.get(product_id);
                        product.setSingleProductImageList(productImageService.getList(product_id, new PageUtil(0, 1)));
                        productOrderItem.setProductOrderItem_product(product);
                        if (order.getProductOrder_status() == 3) {
                            productOrderItem.setIsReview(reviewService.getTotalByOrderItemId(productOrderItem.getProductOrderItem_id()) > 0);
                        }
                    }
                }
                order.setProductOrderItemList(productOrderItemList);
            }
        }
        pageUtil.setTotal(orderCount);
        List<Category> categoryList = categoryService.getList(null, new PageUtil(0, 5));

        map.put("pageUtil", pageUtil);
        map.put("productOrderList", productOrderList);
        map.put("categoryList", categoryList);
        map.put("status", status);

        return "fore/orderListPage";
    }

    //转到前台-订单建立页
    @GetMapping("/create/{product_id}")
    public String goToOrderConfirmPage(@PathVariable("product_id") Integer product_id,
                                       @RequestParam(required = false, defaultValue = "1") Short product_number,
                                       Map<String, Object> map,
                                       HttpSession session,
                                       HttpServletRequest request) throws UnsupportedEncodingException {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        log.info("通过产品ID获取产品信息：{}", product_id);
        Product product = productService.get(product_id);
        if (product == null) {
            return "redirect:/";
        }

        // 获取产品的详细信息
        product.setProduct_category(categoryService.get(product.getProduct_category().getCategory_id()));
        product.setSingleProductImageList(productImageService.getList(product_id, new PageUtil(0, 1)));

        // 封装订单项对象
        ProductOrderItem productOrderItem = new ProductOrderItem();
        productOrderItem.setProductOrderItem_product(product);
        productOrderItem.setProductOrderItem_number(product_number);
        productOrderItem.setProductOrderItem_price(product.getProduct_sale_price() * product_number);
        productOrderItem.setProductOrderItem_user(new User().setUser_id(Integer.valueOf(userId.toString())));

        String addressId = "110000";
        String cityAddressId = "110100";
        String districtAddressId = "110101";
        String detailsAddress = null;
        String order_post = null;
        String order_receiver = null;
        String order_phone = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                String cookieValue = cookie.getValue();
                switch (cookieName) {
                    case "addressId":
                        addressId = cookieValue;
                        break;
                    case "cityAddressId":
                        cityAddressId = cookieValue;
                        break;
                    case "districtAddressId":
                        districtAddressId = cookieValue;
                        break;
                    case "order_post":
                        order_post = URLDecoder.decode(cookieValue, "UTF-8");
                        break;
                    case "order_receiver":
                        order_receiver = URLDecoder.decode(cookieValue, "UTF-8");
                        break;
                    case "order_phone":
                        order_phone = URLDecoder.decode(cookieValue, "UTF-8");
                        break;
                    case "detailsAddress":
                        detailsAddress = URLDecoder.decode(cookieValue, "UTF-8");
                        break;
                }
            }
        }
        // 获取地址Tree
        List<Address> addressList = addressService.getRoot();
        List<Address> cityAddress = addressService.getList(null, addressId);
        List<Address> districtAddress = addressService.getList(null, cityAddressId);

        List<ProductOrderItem> productOrderItemList = new ArrayList<>();
        productOrderItemList.add(productOrderItem);

        map.put("orderItemList", productOrderItemList);
        map.put("addressList", addressList);
        map.put("cityList", cityAddress);
        map.put("districtList", districtAddress);
        map.put("orderTotalPrice", productOrderItem.getProductOrderItem_price());

        map.put("addressId", addressId);
        map.put("cityAddressId", cityAddressId);
        map.put("districtAddressId", districtAddressId);
        map.put("order_post", order_post);
        map.put("order_receiver", order_receiver);
        map.put("order_phone", order_phone);
        map.put("detailsAddress", detailsAddress);

        return "fore/productBuyPage";
    }

    //转到前台-购物车订单建立页
    @GetMapping("/create/byCart")
    public String goToOrderConfirmPageByCart(Map<String, Object> map,
                                             HttpSession session, HttpServletRequest request,
                                             @RequestParam(required = false) Integer[] order_item_list) throws UnsupportedEncodingException {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);


        if (order_item_list == null || order_item_list.length == 0) {
            log.warn("用户订单项数组不存在，回到购物车页");
            return "redirect:/order/cart";
        }

        // 通过订单项ID数组获取订单信息
        List<ProductOrderItem> orderItemList = new ArrayList<>(order_item_list.length);
        for (Integer orderItem_id : order_item_list) {
            orderItemList.add(productOrderItemService.get(orderItem_id));
        }
        // 检查订单项合法性
        if (orderItemList.size() == 0) {
            log.warn("用户订单项获取失败，回到购物车页");
            return "redirect:/order/cart";
        }
        for (ProductOrderItem orderItem : orderItemList) {
            if (!Objects.equals(orderItem.getProductOrderItem_user().getUser_id(), userId)) {
                log.warn("用户订单项与用户不匹配，回到购物车页");
                return "redirect:/order/cart";
            }
            if (orderItem.getProductOrderItem_order() != null) {
                log.warn("用户订单项不属于购物车，回到购物车页");
                return "redirect:/order/cart";
            }
        }
        // 验证通过，获取订单项的产品信息
        double orderTotalPrice = 0.0;
        for (ProductOrderItem orderItem : orderItemList) {
            Product product = productService.get(orderItem.getProductOrderItem_product().getProduct_id());
            product.setProduct_category(categoryService.get(product.getProduct_category().getCategory_id()));
            product.setSingleProductImageList(productImageService.getList(product.getProduct_id(), new PageUtil(0, 1)));
            orderItem.setProductOrderItem_product(product);
            orderTotalPrice += orderItem.getProductOrderItem_price();
        }
        String addressId = "110000";
        String cityAddressId = "110100";
        String districtAddressId = "110101";
        String detailsAddress = null;
        String order_post = null;
        String order_receiver = null;
        String order_phone = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                String cookieValue = cookie.getValue();
                switch (cookieName) {
                    case "addressId":
                        addressId = cookieValue;
                        break;
                    case "cityAddressId":
                        cityAddressId = cookieValue;
                        break;
                    case "districtAddressId":
                        districtAddressId = cookieValue;
                        break;
                    case "order_post":
                        order_post = URLDecoder.decode(cookieValue, "UTF-8");
                        break;
                    case "order_receiver":
                        order_receiver = URLDecoder.decode(cookieValue, "UTF-8");
                        break;
                    case "order_phone":
                        order_phone = URLDecoder.decode(cookieValue, "UTF-8");
                        break;
                    case "detailsAddress":
                        detailsAddress = URLDecoder.decode(cookieValue, "UTF-8");
                        break;
                }
            }
        }
        // 获取地址Tree
        List<Address> addressList = addressService.getRoot();
        List<Address> cityAddress = addressService.getList(null, addressId);
        List<Address> districtAddress = addressService.getList(null, cityAddressId);

        map.put("orderItemList", orderItemList);
        map.put("addressList", addressList);
        map.put("cityList", cityAddress);
        map.put("districtList", districtAddress);
        map.put("orderTotalPrice", orderTotalPrice);

        map.put("addressId", addressId);
        map.put("cityAddressId", cityAddressId);
        map.put("districtAddressId", districtAddressId);
        map.put("order_post", order_post);
        map.put("order_receiver", order_receiver);
        map.put("order_phone", order_phone);
        map.put("detailsAddress", detailsAddress);

        return "fore/productBuyPage";
    }

    //转到前台-订单支付页
    @GetMapping("/pay/{order_code}")
    public String goToOrderPayPage(Map<String, Object> map, HttpSession session,
                                   @PathVariable("order_code") String order_code) {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        // 验证订单信息
        // 查询订单是否存在
        ProductOrder order = productOrderService.getByCode(order_code);
        JSONObject checkoutOrderNoStatus = this.checkoutOrderNoStatus(order, userId);
        if (!(Boolean) checkoutOrderNoStatus.get("miss")) {
            return (String) checkoutOrderNoStatus.get("url");
        }

        order.setProductOrderItemList(productOrderItemService.getListByOrderId(order.getProductOrder_id(), null));

        double orderTotalPrice = 0.00;
        if (order.getProductOrderItemList().size() == 1) {
            log.info("获取单订单项的产品信息");
            ProductOrderItem productOrderItem = order.getProductOrderItemList().get(0);
            Product product = productService.get(productOrderItem.getProductOrderItem_product().getProduct_id());
            product.setProduct_category(categoryService.get(product.getProduct_category().getCategory_id()));
            productOrderItem.setProductOrderItem_product(product);
            orderTotalPrice = productOrderItem.getProductOrderItem_price();
        } else {
            for (ProductOrderItem productOrderItem : order.getProductOrderItemList()) {
                orderTotalPrice += productOrderItem.getProductOrderItem_price();
            }
        }
        log.info("订单总金额为：{}元", orderTotalPrice);

        map.put("productOrder", order);
        map.put("orderTotalPrice", orderTotalPrice);

        return "fore/productPayPage";
    }

    //转到前台-订单支付成功页
    @GetMapping("/pay/success/{order_code}")
    public String goToOrderPaySuccessPage(Map<String, Object> map, HttpSession session,
                                          @PathVariable("order_code") String order_code) {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        // 验证订单信息
        // 查询订单是否存在
        ProductOrder order = productOrderService.getByCode(order_code);
        JSONObject checkoutOrderNoStatus = this.checkoutOrderNoStatus(order, userId);
        if (!(Boolean) checkoutOrderNoStatus.get("miss")) {
            return (String) checkoutOrderNoStatus.get("url");
        }
        order.setProductOrderItemList(productOrderItemService.getListByOrderId(order.getProductOrder_id(), null));

        double orderTotalPrice = 0.00;
        if (order.getProductOrderItemList().size() == 1) {
            // 获取单订单项的产品信息
            ProductOrderItem productOrderItem = order.getProductOrderItemList().get(0);
            orderTotalPrice = productOrderItem.getProductOrderItem_price();
        } else {
            for (ProductOrderItem productOrderItem : order.getProductOrderItemList()) {
                orderTotalPrice += productOrderItem.getProductOrderItem_price();
            }
        }
        log.info("订单总金额为：{}元", orderTotalPrice);

        // 获取订单详情-地址信息
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
        log.info("订单地址字符串：{}", builder);
        order.setProductOrder_detail_address(builder.toString());

        map.put("productOrder", order);
        map.put("orderTotalPrice", orderTotalPrice);

        return "fore/productPaySuccessPage";
    }

    //转到前台-订单确认页
    @GetMapping("/confirm/{order_code}")
    public String goToOrderConfirmPage(Map<String, Object> map, HttpSession session,
                                       @PathVariable("order_code") String order_code) {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        // 验证订单信息
        // 查询订单是否存在
        ProductOrder order = productOrderService.getByCode(order_code);
        JSONObject checkoutOrderNoStatus = this.checkoutOrderNoStatus(order, userId);
        if (!(Boolean) checkoutOrderNoStatus.get("miss")) {
            return (String) checkoutOrderNoStatus.get("url");
        }
        order.setProductOrderItemList(productOrderItemService.getListByOrderId(order.getProductOrder_id(), null));

        double orderTotalPrice = 0.00;
        if (order.getProductOrderItemList().size() == 1) {
            log.info("获取单订单项的产品信息");
            ProductOrderItem productOrderItem = order.getProductOrderItemList().get(0);
            Integer product_id = productOrderItem.getProductOrderItem_product().getProduct_id();
            Product product = productService.get(product_id);
            product.setSingleProductImageList(productImageService.getList(product_id, new PageUtil(0, 1)));
            productOrderItem.setProductOrderItem_product(product);
            orderTotalPrice = productOrderItem.getProductOrderItem_price();
        } else {
            // 获取多订单项的产品信息
            for (ProductOrderItem productOrderItem : order.getProductOrderItemList()) {
                Integer product_id = productOrderItem.getProductOrderItem_product().getProduct_id();
                Product product = productService.get(product_id);
                product.setSingleProductImageList(productImageService.getList(product_id, new PageUtil(0, 1)));
                productOrderItem.setProductOrderItem_product(product);
                orderTotalPrice += productOrderItem.getProductOrderItem_price();
            }
        }
        log.info("订单总金额为：{}元", orderTotalPrice);

        map.put("productOrder", order);
        map.put("orderTotalPrice", orderTotalPrice);
        return "fore/orderConfirmPage";
    }

    //转到前台-订单完成页
    @GetMapping("/success/{order_code}")
    public String goToOrderSuccessPage(Map<String, Object> map, HttpSession session,
                                       @PathVariable("order_code") String order_code) {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        // 验证订单信息
        // 查询订单是否存在
        ProductOrder order = productOrderService.getByCode(order_code);
        JSONObject checkoutOrderNoStatus = this.checkoutOrderNoStatus(order, userId);
        if (!(Boolean) checkoutOrderNoStatus.get("miss")) {
            return (String) checkoutOrderNoStatus.get("url");
        }

        // 获取订单中订单项数量
        Integer count = productOrderItemService.getTotalByOrderId(order.getProductOrder_id());
        Product product = null;
        if (count == 1) {
            // 获取订单中的唯一订单项
            ProductOrderItem productOrderItem = productOrderItemService.getListByOrderId(order.getProductOrder_id(), new PageUtil(0, 1)).get(0);
            if (productOrderItem != null) {
                // 获取订单项评论数量
                count = reviewService.getTotalByOrderItemId(productOrderItem.getProductOrderItem_id());
                if (count == 0) {
                    // 获取订单项产品信息
                    product = productService.get(productOrderItem.getProductOrderItem_product().getProduct_id());
                    if (product != null) {
                        product.setSingleProductImageList(productImageService.getList(product.getProduct_id(), new PageUtil(0, 1)));
                    }
                }
            }
            map.put("orderItem", productOrderItem);
        }

        map.put("product", product);
        return "fore/orderSuccessPage";
    }

    //转到前台-购物车页
    @GetMapping("/cart")
    public String goToCartPage(Map<String, Object> map, HttpSession session) {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        // 获取用户购物车信息
        List<ProductOrderItem> orderItemList = productOrderItemService.getListByUserId(Integer.valueOf(userId.toString()), null);
        Integer orderItemTotal = 0;
        if (orderItemList.size() > 0) {
            // 获取用户购物车的商品总数
            orderItemTotal = productOrderItemService.getTotalByUserId(Integer.valueOf(userId.toString()));
            // 获取用户购物车内的商品信息
            for (ProductOrderItem orderItem : orderItemList) {
                Integer product_id = orderItem.getProductOrderItem_product().getProduct_id();
                Product product = productService.get(product_id);
                product.setSingleProductImageList(productImageService.getList(product_id, null));
                product.setProduct_category(categoryService.get(product.getProduct_category().getCategory_id()));
                orderItem.setProductOrderItem_product(product);
            }
        }
        map.put("orderItemList", orderItemList);
        map.put("orderItemTotal", orderItemTotal);
        return "fore/productBuyCarPage";
    }

    //更新订单信息为已支付，待发货-ajax
    @ResponseBody
    @PutMapping("/pay/{order_code}")
    public String orderPay(Map<String, Object> map, HttpSession session,
                           @PathVariable("order_code") String order_code) {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        JSONObject object = new JSONObject();
        ProductOrder order = productOrderService.getByCode(order_code);
        if (order == null) {
            log.warn("订单不存在，返回订单列表页");
            object.put("success", false);
            object.put("url", "/order/0/10");
            return object.toJSONString();
        }
        if (order.getProductOrder_user().getUser_id() != Integer.parseInt(userId.toString())) {
            log.warn("用户与订单信息不一致，返回订单列表页");
            object.put("success", false);
            object.put("url", "/order/0/10");
            return object.toJSONString();
        }

        if (order.getProductOrder_status() == 1) {
            log.warn("订单确认成功");
            object.put("success", true);
            object.put("url", "/order/pay/success/" + order_code);
            return object.toJSONString();
        } else {
            log.warn("订单状态异常");
            object.put("success", false);
            object.put("url", "/order/0/10");
            return object.toJSONString();
        }
    }

    //更新订单信息为已发货，待确认-ajax
    @GetMapping("/delivery/{order_code}")
    public String orderDelivery(Map<String, Object> map, HttpSession session,
                                @PathVariable("order_code") String order_code) {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        ProductOrder order = productOrderService.getByCode(order_code);
        JSONObject checkoutOrderNoStatus = this.checkoutOrderNoStatus(order, userId);
        if (!(Boolean) checkoutOrderNoStatus.get("miss")) {
            return (String) checkoutOrderNoStatus.get("url");
        }

        // 更新订单信息
        ProductOrder productOrder = new ProductOrder()
                .setProductOrder_id(order.getProductOrder_id())
                .setProductOrder_delivery_date(new Date())
                .setProductOrder_status((byte) 2);
        productOrderService.update(productOrder);
        return "redirect:/order/0/10";
    }

    //更新订单信息为交易成功-ajax
    @ResponseBody
    @PutMapping("/success/{order_code}")
    public String orderSuccess(Map<String, Object> map, HttpSession session,
                               @PathVariable("order_code") String order_code) {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        JSONObject object = new JSONObject();
        ProductOrder order = productOrderService.getByCode(order_code);
        JSONObject checkoutOrderNoStatus = this.checkoutOrderNoStatus(order, userId);
        if (!(Boolean) checkoutOrderNoStatus.get("miss")) {
            object.put("success", checkoutOrderNoStatus.get("miss"));
            object.put("url", checkoutOrderNoStatus.get("url"));
            return object.toJSONString();
        }

        ProductOrder productOrder = new ProductOrder()
                .setProductOrder_id(order.getProductOrder_id())
                .setProductOrder_status((byte) 3)
                .setProductOrder_confirm_date(new Date());

        boolean yn = productOrderService.update(productOrder);
        if (yn) {
            object.put("success", true);
        } else {
            object.put("success", false);
        }
        return object.toJSONString();
    }

    //删除订单
    @ResponseBody
    @GetMapping("/delete/{order_code}")
    public String deleteOrderItem(Map<String, Object> map, HttpSession session,
                                  @PathVariable("order_code") String order_code) {
        User user = (User) session.getAttribute("USER_SESSION");
        map.put("user", user);

        JSONObject object = new JSONObject();
        // 检查用户的购物车项
        ProductOrder byCode = productOrderService.getByCode(order_code);
        log.info("删除订单-{}", byCode.toString());
        boolean b = productOrderService.deleteList(new Integer[]{byCode.getProductOrder_id()});
        if (b) {
            log.info("删除订单成功");
            object.put("success", true);
        }
        return object.toJSONString();
    }

    //更新订单信息为交易关闭-ajax
    @ResponseBody
    @PutMapping("/close/{order_code}")
    public String orderClose(Map<String, Object> map, HttpSession session,
                             @PathVariable("order_code") String order_code) {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        JSONObject object = new JSONObject();
        ProductOrder order = productOrderService.getByCode(order_code);
        JSONObject checkoutOrderNoStatus = this.checkoutOrderNoStatus(order, userId);
        if (!(Boolean) checkoutOrderNoStatus.get("miss")) {
            object.put("success", checkoutOrderNoStatus.get("miss"));
            object.put("url", checkoutOrderNoStatus.get("url"));
            return object.toJSONString();
        }

        ProductOrder productOrder = new ProductOrder()
                .setProductOrder_id(order.getProductOrder_id())
                .setProductOrder_status((byte) 4);

        boolean yn = productOrderService.update(productOrder);
        if (yn) {
            object.put("success", true);
        } else {
            object.put("success", false);
        }
        return object.toJSONString();
    }

    //更新购物车订单项数量-ajax
    @ResponseBody
    @PutMapping("/orderItem")
    public String updateOrderItem(HttpSession session, Map<String, Object> map,
                                  @RequestBody() String orderItemMap) {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        JSONObject object = new JSONObject();
        JSONObject orderItemString = JSON.parseObject(orderItemMap);
        Set<String> orderItemIDSet = orderItemString.keySet();
        if (orderItemIDSet.size() > 0) {
            // 更新产品订单项数量
            for (String key : orderItemIDSet) {
                ProductOrderItem productOrderItem = productOrderItemService.get(Integer.valueOf(key));
                if (productOrderItem == null || !productOrderItem.getProductOrderItem_user().getUser_id().equals(userId)) {
                    log.warn("订单项为空或用户状态不一致！");
                    object.put("success", false);
                    return object.toJSONString();
                }
                if (productOrderItem.getProductOrderItem_order() != null) {
                    log.warn("用户订单项不属于购物车，回到购物车页");
                    return "redirect:/order/cart";
                }
                Short number = Short.valueOf(orderItemString.getString(key.toString()));
                if (number <= 0 || number > 500) {
                    log.warn("订单项产品数量不合法！");
                    object.put("success", false);
                    return object.toJSONString();
                }
                double price = productOrderItem.getProductOrderItem_price() / productOrderItem.getProductOrderItem_number();
                Boolean yn = productOrderItemService.update(new ProductOrderItem().setProductOrderItem_id(Integer.valueOf(key)).setProductOrderItem_number(number).setProductOrderItem_price(number * price));
                if (!yn) {
                    throw new RuntimeException();
                }
            }
            Object[] orderItemIDArray = orderItemIDSet.toArray();
            object.put("success", true);
            object.put("orderItemIDArray", orderItemIDArray);
            return object.toJSONString();
        } else {
            log.warn("无订单项可以处理");
            object.put("success", false);
            return object.toJSONString();
        }
    }

    //创建新订单-单订单项-ajax
    @ResponseBody
    @PostMapping()
    public String createOrderByOne(HttpSession session, Map<String, Object> map,
                                   HttpServletResponse response,
                                   @RequestParam String addressId,
                                   @RequestParam String cityAddressId,
                                   @RequestParam String districtAddressId,
                                   @RequestParam String productOrder_detail_address,
                                   @RequestParam String productOrder_post,
                                   @RequestParam String productOrder_receiver,
                                   @RequestParam String productOrder_mobile,
                                   @RequestParam String userMessage,
                                   @RequestParam Integer orderItem_product_id,
                                   @RequestParam Short orderItem_number) throws UnsupportedEncodingException {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        JSONObject object = new JSONObject();
        Product product = productService.get(orderItem_product_id);
        if (product == null) {
            object.put("success", false);
            object.put("url", "/");
            return object.toJSONString();
        }
        // 将收货地址等相关信息存入Cookie中

        Cookie cookie1 = new Cookie("addressId", addressId);
        Cookie cookie2 = new Cookie("cityAddressId", cityAddressId);
        Cookie cookie3 = new Cookie("districtAddressId", districtAddressId);
        Cookie cookie4 = new Cookie("order_post", URLEncoder.encode(productOrder_post, "UTF-8"));
        Cookie cookie5 = new Cookie("order_receiver", URLEncoder.encode(productOrder_receiver, "UTF-8"));
        Cookie cookie6 = new Cookie("order_phone", URLEncoder.encode(productOrder_mobile, "UTF-8"));
        Cookie cookie7 = new Cookie("detailsAddress", URLEncoder.encode(productOrder_detail_address, "UTF-8"));
        int maxAge = 60 * 60 * 24 * 365;  //设置过期时间为一年
        cookie1.setMaxAge(maxAge);
        cookie2.setMaxAge(maxAge);
        cookie3.setMaxAge(maxAge);
        cookie4.setMaxAge(maxAge);
        cookie5.setMaxAge(maxAge);
        cookie6.setMaxAge(maxAge);
        cookie7.setMaxAge(maxAge);
        response.addCookie(cookie1);
        response.addCookie(cookie2);
        response.addCookie(cookie3);
        response.addCookie(cookie4);
        response.addCookie(cookie5);
        response.addCookie(cookie6);
        response.addCookie(cookie7);

        StringBuffer productOrder_code = new StringBuffer()
                .append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))
                .append(0)
                .append(userId);
        log.info("生成的订单号为：{}", productOrder_code);

        // 整合订单对象
        ProductOrder productOrder = new ProductOrder()
                .setProductOrder_status((byte) 0)
                .setProductOrder_address(new Address().setAddress_areaId(districtAddressId))
                .setProductOrder_post(productOrder_post)
                .setProductOrder_user(user)
                .setProductOrder_mobile(productOrder_mobile)
                .setProductOrder_receiver(productOrder_receiver)
                .setProductOrder_detail_address(productOrder_detail_address)
                .setProductOrder_pay_date(new Date())
                .setProductOrder_code(productOrder_code.toString());
        Boolean yn = productOrderService.add(productOrder);
        if (!yn) {
            throw new RuntimeException();
        }
        Integer order_id = lastIDService.selectLastID();

        ProductOrderItem productOrderItem = new ProductOrderItem()
                .setProductOrderItem_user(new User().setUser_id(Integer.valueOf(userId.toString())))
                .setProductOrderItem_product(productService.get(orderItem_product_id))
                .setProductOrderItem_number(orderItem_number)
                .setProductOrderItem_price(product.getProduct_sale_price() * orderItem_number)
                .setProductOrderItem_userMessage(userMessage)
                .setProductOrderItem_order(new ProductOrder().setProductOrder_id(order_id));
        yn = productOrderItemService.add(productOrderItem);
        if (!yn) {
            throw new RuntimeException();
        }

        object.put("success", true);
        object.put("url", "/order/pay/" + productOrder.getProductOrder_code());
        return object.toJSONString();
    }

    //创建新订单-多订单项-ajax
    @ResponseBody
    @PostMapping("/list")
    public String createOrderByList(HttpSession session, Map<String, Object> map,
                                    HttpServletResponse response,
                                    @RequestParam String addressId,
                                    @RequestParam String cityAddressId,
                                    @RequestParam String districtAddressId,
                                    @RequestParam String productOrder_detail_address,
                                    @RequestParam String productOrder_post,
                                    @RequestParam String productOrder_receiver,
                                    @RequestParam String productOrder_mobile,
                                    @RequestParam String orderItemJSON) throws UnsupportedEncodingException {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        JSONObject object = new JSONObject();
        JSONObject orderItemMap = JSONObject.parseObject(orderItemJSON);
        Set<String> orderItem_id = orderItemMap.keySet();
        List<ProductOrderItem> productOrderItemList = new ArrayList<>(3);
        if (orderItem_id.size() > 0) {
            for (String id : orderItem_id) {
                ProductOrderItem orderItem = productOrderItemService.get(Integer.valueOf(id));
                if (orderItem == null || !orderItem.getProductOrderItem_user().getUser_id().equals(userId)) {
                    log.warn("订单项为空或用户状态不一致！");
                    object.put("success", false);
                    object.put("url", "/order/cart");
                    return object.toJSONString();
                }
                if (orderItem.getProductOrderItem_order() != null) {
                    log.warn("用户订单项不属于购物车，回到购物车页");
                    object.put("success", false);
                    object.put("url", "/order/cart");
                    return object.toJSONString();
                }
                boolean yn = productOrderItemService.update(new ProductOrderItem().setProductOrderItem_id(Integer.valueOf(id)).setProductOrderItem_userMessage(orderItemMap.getString(id)));
                if (!yn) {
                    throw new RuntimeException();
                }
                orderItem.setProductOrderItem_product(productService.get(orderItem.getProductOrderItem_product().getProduct_id()));
                productOrderItemList.add(orderItem);
            }
        } else {
            object.put("success", false);
            object.put("url", "/order/cart");
            return object.toJSONString();
        }
        log.info("将收货地址等相关信息存入Cookie中");
        Cookie cookie1 = new Cookie("addressId", addressId);
        Cookie cookie2 = new Cookie("cityAddressId", cityAddressId);
        Cookie cookie3 = new Cookie("districtAddressId", districtAddressId);
        Cookie cookie4 = new Cookie("order_post", URLEncoder.encode(productOrder_post, "UTF-8"));
        Cookie cookie5 = new Cookie("order_receiver", URLEncoder.encode(productOrder_receiver, "UTF-8"));
        Cookie cookie6 = new Cookie("order_phone", URLEncoder.encode(productOrder_mobile, "UTF-8"));
        Cookie cookie7 = new Cookie("detailsAddress", URLEncoder.encode(productOrder_detail_address, "UTF-8"));
        int maxAge = 60 * 60 * 24 * 365;  //设置过期时间为一年
        cookie1.setMaxAge(maxAge);
        cookie2.setMaxAge(maxAge);
        cookie3.setMaxAge(maxAge);
        cookie4.setMaxAge(maxAge);
        cookie5.setMaxAge(maxAge);
        cookie6.setMaxAge(maxAge);
        cookie7.setMaxAge(maxAge);
        response.addCookie(cookie1);
        response.addCookie(cookie2);
        response.addCookie(cookie3);
        response.addCookie(cookie4);
        response.addCookie(cookie5);
        response.addCookie(cookie6);
        response.addCookie(cookie7);
        StringBuffer productOrder_code = new StringBuffer()
                .append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))
                .append(0)
                .append(userId);
        log.info("生成的订单号为：{}", productOrder_code);
        log.info("整合订单对象");
        ProductOrder productOrder = new ProductOrder()
                .setProductOrder_status((byte) 0)
                .setProductOrder_address(new Address().setAddress_areaId(districtAddressId))
                .setProductOrder_post(productOrder_post)
                .setProductOrder_user(user)
                .setProductOrder_mobile(productOrder_mobile)
                .setProductOrder_receiver(productOrder_receiver)
                .setProductOrder_detail_address(productOrder_detail_address)
                .setProductOrder_pay_date(new Date())
                .setProductOrder_code(productOrder_code.toString());
        Boolean yn = productOrderService.add(productOrder);
        if (!yn) {
            throw new RuntimeException();
        }
        Integer order_id = lastIDService.selectLastID();
        log.info("整合订单项对象");
        for (ProductOrderItem orderItem : productOrderItemList) {
            orderItem.setProductOrderItem_order(new ProductOrder().setProductOrder_id(order_id));
            yn = productOrderItemService.update(orderItem);
        }
        if (!yn) {
            throw new RuntimeException();
        }

        object.put("success", true);
        object.put("url", "/order/pay/" + productOrder.getProductOrder_code());
        return object.toJSONString();
    }

    //创建订单项-购物车-ajax
    @ResponseBody
    @PostMapping("/orderItem/create/{product_id}")
    public String createOrderItem(Map<String, Object> map,
                                  @PathVariable("product_id") Integer product_id,
                                  @RequestParam(required = false, defaultValue = "1") Short product_number,
                                  HttpSession session) {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        JSONObject object = new JSONObject();
        log.info("通过产品ID获取产品信息：{}", product_id);
        Product product = productService.get(product_id);
        if (product == null) {
            object.put("url", "/login");
            object.put("success", false);
            return object.toJSONString();
        }

        ProductOrderItem productOrderItem = new ProductOrderItem();
        log.info("检查用户的购物车项");
        List<ProductOrderItem> orderItemList = productOrderItemService.getListByUserId(Integer.valueOf(userId.toString()), null);
        for (ProductOrderItem orderItem : orderItemList) {
            if (orderItem.getProductOrderItem_product().getProduct_id().equals(product_id)) {
                log.info("找到已有的产品，进行数量追加");
                int number = orderItem.getProductOrderItem_number();
                number += 1;
                productOrderItem.setProductOrderItem_id(orderItem.getProductOrderItem_id());
                productOrderItem.setProductOrderItem_number((short) number);
                productOrderItem.setProductOrderItem_price(number * product.getProduct_sale_price());
                boolean yn = productOrderItemService.update(productOrderItem);
                if (yn) {
                    object.put("success", true);
                } else {
                    object.put("success", false);
                }
                return object.toJSONString();
            }
        }
        // 封装订单项对象
        productOrderItem.setProductOrderItem_product(product);
        productOrderItem.setProductOrderItem_number(product_number);
        productOrderItem.setProductOrderItem_price(product.getProduct_sale_price() * product_number);
        productOrderItem.setProductOrderItem_user(new User().setUser_id(Integer.valueOf(userId.toString())));
        boolean yn = productOrderItemService.add(productOrderItem);
        if (yn) {
            object.put("success", true);
        } else {
            object.put("success", false);
        }
        return object.toJSONString();
    }

    //删除订单项-购物车-ajax
    @ResponseBody
    @DeleteMapping("/orderItem/{orderItem_id}")
    public String deleteOrderItem(Map<String, Object> map, @PathVariable("orderItem_id") Integer orderItem_id,
                                  HttpSession session,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        JSONObject object = new JSONObject();
        // 检查用户的购物车项
        List<ProductOrderItem> orderItemList = productOrderItemService.getListByUserId(Integer.valueOf(userId.toString()), null);
        boolean isMine = false;
        for (ProductOrderItem orderItem : orderItemList) {
            log.info("找到匹配的购物车项");
            if (orderItem.getProductOrderItem_id().equals(orderItem_id)) {
                isMine = true;
                break;
            }
        }
        if (isMine) {
            log.info("删除订单项信息");
            boolean yn = productOrderItemService.deleteList(new Integer[]{orderItem_id});
            if (yn) {
                object.put("success", true);
            } else {
                object.put("success", false);
            }
        } else {
            object.put("success", false);
        }
        return object.toJSONString();
    }

    private JSONObject checkoutOrderNoStatus(ProductOrder order, Integer userId) {
        JSONObject object = new JSONObject();
        object.put("miss", true);
        object.put("url", "redirect:/order/0/10");
        if (order == null) {
            log.warn("订单不存在，返回订单列表页");
            object.put("miss", false);
            return object;
        }
        // 验证订单状态
        if (order.getProductOrder_status() != 0) {
            log.warn("订单状态不正确，返回订单列表页");
            object.put("miss", false);
            return object;
        }
        // 验证用户与订单是否一致
        if (!Objects.equals(order.getProductOrder_user().getUser_id(), userId)) {
            log.warn("用户与订单信息不一致，返回订单列表页");
            object.put("miss", false);
            return object;
        }
        return object;
    }
}