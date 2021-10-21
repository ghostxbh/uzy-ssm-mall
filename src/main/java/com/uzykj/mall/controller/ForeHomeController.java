package com.uzykj.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.uzykj.mall.entity.Category;
import com.uzykj.mall.entity.Product;
import com.uzykj.mall.entity.User;
import com.uzykj.mall.service.CategoryService;
import com.uzykj.mall.service.ProductImageService;
import com.uzykj.mall.service.ProductService;
import com.uzykj.mall.util.OrderUtil;
import com.uzykj.mall.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 前台-主页
 */
@Slf4j
@Controller
public class ForeHomeController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;


    //转到前台-主页
    @GetMapping("/")
    public String goToPage(HttpSession session, Map<String, Object> map, HttpServletResponse response) {
        //SessionMap
        User user = (User) session.getAttribute("USER_SESSION");
        map.put("user", user);

        List<Category> categoryList = categoryService.getList(null, null);
        for (Category category : categoryList) {
            log.info("获取分类id为{}的产品集合，按产品ID倒序排序", category.getCategory_id());
            List<Product> productList = productService.getList(new Product().setProduct_category(category), new Byte[]{0, 2}, new OrderUtil("product_id", true), new PageUtil(0, 8));
            if (productList != null) {
                for (Product product : productList) {
                    Integer product_id = product.getProduct_id();
                    log.info("获取产品id为{}的产品预览图片信息", product_id);
                    product.setSingleProductImageList(productImageService.getList(product_id, new PageUtil(0, 1)));
                }
            }
            category.setProductList(productList);
        }
        List<Product> specialProductList = productService.getList(null, new Byte[]{2}, null, new PageUtil(0, 6));

        map.put("categoryList", categoryList);
        map.put("specialProductList", specialProductList);

        return "fore/homePage";
    }

    //转到前台-错误页
    @GetMapping("error")
    public String goToErrorPage() {
        return "fore/errorPage";
    }

    //获取主页分类下产品信息-ajax
    @ResponseBody
    @GetMapping("product/nav/{category_id}")
    public String getProductByNav(@PathVariable("category_id") Integer category_id) {
        JSONObject object = new JSONObject();
        if (category_id == null) {
            object.put("success", false);
            return object.toJSONString();
        }
        log.info("获取分类ID为{}的产品标题数据", category_id);
        List<Product> productList = productService.getTitle(new Product().setProduct_category(new Category().setCategory_id(category_id)), new PageUtil(0, 40));
        List<List<Product>> complexProductList = new ArrayList<>(8);
        List<Product> products = new ArrayList<>(5);
        for (int i = 0; i < productList.size(); i++) {
            //如果临时集合中产品数达到5个，加入到产品二维集合中，并重新实例化临时集合
            if (i % 5 == 0) {
                complexProductList.add(products);
                products = new ArrayList<>(5);
            }
            products.add(productList.get(i));
        }
        complexProductList.add(products);
        Category category = new Category().setCategory_id(category_id).setComplexProductList(complexProductList);
        object.put("success", true);
        object.put("category", category);
        return object.toJSONString();
    }
}
