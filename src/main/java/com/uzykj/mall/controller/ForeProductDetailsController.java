package com.uzykj.mall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uzykj.mall.entity.*;
import com.uzykj.mall.service.*;
import com.uzykj.mall.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 前台-产品详情页
 */
@Slf4j
@Controller
public class ForeProductDetailsController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ProductOrderItemService productOrderItemService;


    //转到前台-产品详情页
    @GetMapping("product/{pid}")
    public String goToPage(HttpSession session, Map<String, Object> map,
                           @PathVariable("pid") String pid /*产品ID*/) {
        User user = (User) session.getAttribute("USER_SESSION");
        Integer userId = (Integer) session.getAttribute("USER_ID");
        map.put("user", user);

        Integer product_id = Integer.parseInt(pid);
        Product product = productService.get(product_id);
        if (product == null || product.getProduct_isEnabled() == 1) {
            return "redirect:/404";
        }
        // 获取产品子信息-分类信息
        product.setProduct_category(categoryService.get(product.getProduct_category_id()));
        // 获取产品子信息-产品图片信息
        List<ProductImage> productImageList = productImageService.getList(product_id, null);
        List<ProductImage> singleProductImageList = new ArrayList<>(5);
        List<ProductImage> detailsProductImageList = new ArrayList<>(8);
        for (ProductImage productImage : productImageList) {
            if (productImage.getProductImage_type() == 0) {
                singleProductImageList.add(productImage);
            } else {
                detailsProductImageList.add(productImage);
            }
        }
        product.setSingleProductImageList(singleProductImageList);
        product.setDetailProductImageList(detailsProductImageList);
        // 获取产品子信息-产品属性值信息
        List<PropertyValue> propertyValueList = propertyValueService.getList(new PropertyValue().setPropertyValue_product(product), null);
        // 获取产品子信息-分类信息对应的属性列表
        List<Property> propertyList = propertyService.getList(new Property().setProperty_category(product.getProduct_category()), null);
        // 属性列表和属性值列表合并
        for (Property property : propertyList) {
            for (PropertyValue propertyValue : propertyValueList) {
                if (property.getProperty_id().equals(propertyValue.getPropertyValue_property().getProperty_id())) {
                    List<PropertyValue> property_value_item = new ArrayList<>(1);
                    property_value_item.add(propertyValue);
                    property.setPropertyValueList(property_value_item);
                    break;
                }
            }
        }
        // 获取产品子信息-产品评论信息
        product.setReviewList(reviewService.getListByProductId(product_id, null));
        if (product.getReviewList() != null) {
            for (Review review : product.getReviewList()) {
                review.setReview_user(userService.get(review.getReview_user().getUser_id()));
            }
        }

        // 获取产品子信息-销量数和评论数信息
        product.setProduct_sale_count(productOrderItemService.getSaleCountByProductId(product_id));
        product.setProduct_review_count(reviewService.getTotalByProductId(product_id));

        // 获取猜你喜欢列表
        Integer category_id = product.getProduct_category_id();
        Integer total = productService.getTotal(new Product().setProduct_category(new Category().setCategory_id(category_id)), new Byte[]{0, 2});
        log.info("分类ID为{}的产品总数为{}条", category_id, total);
        //生成随机数
        int i = new Random().nextInt(total);
        if (i + 2 >= total) {
            i = total - 3;
        }
        if (i < 0) {
            i = 0;
        }
        List<Product> loveProductList = productService.getList(new Product().setProduct_category(new Category().setCategory_id(category_id)), new Byte[]{0, 2}, null, new PageUtil().setCount(3).setPageStart(i));
        if (loveProductList != null) {
            // 获取产品列表的相应的一张预览图片
            for (Product loveProduct : loveProductList) {
                loveProduct.setSingleProductImageList(productImageService.getList(loveProduct.getProduct_id(), new PageUtil(0, 1)));
            }
        }
        // 获取分类列表
        List<Category> categoryList = categoryService.getList(null, new PageUtil(0, 3));

        map.put("loveProductList", loveProductList);
        map.put("categoryList", categoryList);
        map.put("propertyList", propertyList);
        map.put("product", product);
        map.put("guessNumber", i);
        map.put("pageUtil", new PageUtil(0, 10).setTotal(product.getProduct_review_count()));
        return "fore/productDetailsPage";
    }

    //按产品ID加载产品评论列表-ajax
    @Deprecated
    @ResponseBody
    @GetMapping("review/{pid}")
    public String loadProductReviewList(@PathVariable("pid") String pid/*产品ID*/,
                                        @RequestParam Integer index/* 页数 */,
                                        @RequestParam Integer count/* 行数 */) {
        Integer product_id = Integer.parseInt(pid);
        // 获取产品评论列表
        List<Review> reviewList = reviewService.getListByProductId(product_id, new PageUtil(index, count));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reviewList", JSONArray.parseArray(JSON.toJSONString(reviewList)));
        return jsonObject.toJSONString();
    }

    //按产品ID加载产品属性列表-ajax
    @Deprecated
    @ResponseBody
    @GetMapping("property/{pid}")
    public String loadProductPropertyList(@PathVariable("pid") String pid/*产品ID*/) {
        Integer product_id = Integer.parseInt(pid);

        // 获取产品详情-属性值信息
        Product product = new Product();
        product.setProduct_id(product_id);
        List<PropertyValue> propertyValueList = propertyValueService.getList(new PropertyValue().setPropertyValue_product(product), null);

        // 获取产品详情-分类信息对应的属性列表
        List<Property> propertyList = propertyService.getList(new Property().setProperty_category(product.getProduct_category()), null);

        // 属性列表和属性值列表合并
        for (Property property : propertyList) {
            for (PropertyValue propertyValue : propertyValueList) {
                if (property.getProperty_id().equals(propertyValue.getPropertyValue_property().getProperty_id())) {
                    List<PropertyValue> property_value_item = new ArrayList<>(1);
                    property_value_item.add(propertyValue);
                    property.setPropertyValueList(property_value_item);
                    break;
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("propertyList", JSONArray.parseArray(JSON.toJSONString(propertyList)));

        return jsonObject.toJSONString();
    }

    //加载猜你喜欢列表-ajax
    @ResponseBody
    @GetMapping("guess/{cid}")
    public String guessYouLike(@PathVariable("cid") Integer cid,
                               @RequestParam Integer guessNumber) {
        // 获取猜你喜欢列表
        Integer total = productService.getTotal(new Product().setProduct_category(new Category().setCategory_id(cid)), new Byte[]{0, 2});
        log.info("分类ID为{}的产品总数为{}条", cid, total);
        //生成随机数
        int i = new Random().nextInt(total);
        if (i + 2 >= total) {
            i = total - 3;
        }
        if (i < 0) {
            i = 0;
        }
        while (i == guessNumber) {
            i = new Random().nextInt(total);
            if (i + 2 >= total) {
                i = total - 3;
            }
            if (i < 0) {
                i = 0;
                break;
            }
        }

        log.info("guessNumber值为{}，新guessNumber值为{}", guessNumber, i);
        List<Product> loveProductList = productService.getList(new Product().setProduct_category(new Category().setCategory_id(cid)), new Byte[]{0, 2}, null, new PageUtil().setCount(3).setPageStart(i));
        if (loveProductList != null) {
            log.info("获取产品列表的相应的一张预览图片");
            for (Product loveProduct : loveProductList) {
                loveProduct.setSingleProductImageList(productImageService.getList(loveProduct.getProduct_id(), new PageUtil(0, 1)));
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        jsonObject.put("loveProductList", JSONArray.parseArray(JSON.toJSONString(loveProductList)));
        jsonObject.put("guessNumber", i);
        return jsonObject.toJSONString();
    }
}
