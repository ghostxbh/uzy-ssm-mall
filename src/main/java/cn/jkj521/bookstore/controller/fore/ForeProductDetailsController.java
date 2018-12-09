package cn.jkj521.bookstore.controller.fore;

import cn.jkj521.bookstore.util.QiniuUtil;
import cn.jkj521.bookstore.util.redis.HostUtil;
import cn.yunzhf.accounting.user.entity.UzUser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.jkj521.bookstore.controller.BaseController;
import cn.jkj521.bookstore.entity.*;
import cn.jkj521.bookstore.service.*;
import cn.jkj521.bookstore.util.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 前台天猫-产品详情页
 */
@Controller
public class ForeProductDetailsController extends BaseController {
    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "productImageService")
    private ProductImageService productImageService;
    @Resource(name = "categoryService")
    private CategoryService categoryService;
    @Resource(name = "propertyValueService")
    private PropertyValueService propertyValueService;
    @Resource(name = "propertyService")
    private PropertyService propertyService;
    @Resource(name = "reviewService")
    private ReviewService reviewService;
    @Resource(name = "productOrderItemService")
    private ProductOrderItemService productOrderItemService;

    
    //转到前台天猫-产品详情页
    @RequestMapping(value = "product/{pid}", method = RequestMethod.GET)
    public String goToPage(HttpSession session, Map<String, Object> map,
                           @PathVariable("pid") String pid /*产品ID*/,
                           HttpServletResponse response) {
        logger.info("检查用户是否登录");
        Object userId = checkUser(session);
        
        if (userId != null) {
            logger.info("获取用户信息");
            UzUser user = (UzUser)session.getAttribute("user");
            //User user = userService.get(Integer.parseInt(userId.toString()));
            map.put("user", user);
        }/*else{
        	try {
        		response.sendRedirect(HostUtil.host + "AccountingOnline/user/checkLogin?url=bookstore/getsign");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }*/
        
        logger.info("获取产品ID");
        Integer product_id = Integer.parseInt(pid);
        logger.info("获取产品信息");
        Product product = productService.get(product_id);
        if (product == null || product.getProduct_isEnabled() == 1) {
            return "redirect:/404";
        }
        logger.info("获取产品子信息-分类信息");
        product.setProduct_category(categoryService.get(product.getProduct_category().getCategory_id()));
        logger.info("获取产品子信息-产品图片信息");
        List<ProductImage> productImageList = productImageService.getList(product_id, null, null);
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
        logger.info("获取产品子信息-产品属性值信息");
        List<PropertyValue> propertyValueList = propertyValueService.getList(new PropertyValue().setPropertyValue_product(product), null);
        logger.info("获取产品子信息-分类信息对应的属性列表");
        List<Property> propertyList = propertyService.getList(new Property().setProperty_category(product.getProduct_category()), null);
        logger.info("属性列表和属性值列表合并");
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
        logger.info("获取产品子信息-产品评论信息");
        product.setReviewList(reviewService.getListByProductId(product_id, null));
        if (product.getReviewList() != null) {
            for (Review review : product.getReviewList()) {
                review.setReview_user(userService.get(review.getReview_user().getUser_id()));
            }
        }

        logger.info("获取产品子信息-销量数和评论数信息");
        product.setProduct_sale_count(productOrderItemService.getSaleCountByProductId(product_id));
        product.setProduct_review_count(reviewService.getTotalByProductId(product_id));

        logger.info("获取猜你喜欢列表");
        Integer category_id = product.getProduct_category().getCategory_id();
        Integer total = productService.getTotal(new Product().setProduct_category(new Category().setCategory_id(category_id)), new Byte[]{0, 2});
        logger.info("分类ID为{}的产品总数为{}条", category_id, total);
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
            logger.info("获取产品列表的相应的一张预览图片");
            for (Product loveProduct : loveProductList) {
                loveProduct.setSingleProductImageList(productImageService.getList(loveProduct.getProduct_id(), (byte) 0, new PageUtil(0, 1)));
            }
        }
        logger.info("获取分类列表");
        List<Category> categoryList = categoryService.getList(null, new PageUtil(0, 3));

        map.put("loveProductList", loveProductList);
        map.put("categoryList", categoryList);
        map.put("propertyList", propertyList);
        map.put("product", product);
        map.put("guessNumber", i);
        map.put("pageUtil", new PageUtil(0, 10).setTotal(product.getProduct_review_count()));
        logger.info("转到前台-产品详情页");
        return "fore/productDetailsPage";
    }

    //按产品ID加载产品评论列表-ajax
    @Deprecated
    @ResponseBody
    @RequestMapping(value = "review/{pid}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String loadProductReviewList(@PathVariable("pid") String pid/*产品ID*/,
                                        @RequestParam Integer index/* 页数 */,
                                        @RequestParam Integer count/* 行数 */) {
        logger.info("获取产品ID");
        Integer product_id = Integer.parseInt(pid);
        logger.info("获取产品评论列表");
        List<Review> reviewList = reviewService.getListByProductId(product_id, new PageUtil(index, count));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reviewList", JSONArray.parseArray(JSON.toJSONString(reviewList)));

        return jsonObject.toJSONString();
    }

    //按产品ID加载产品属性列表-ajax
    @Deprecated
    @ResponseBody
    @RequestMapping(value = "property/{pid}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String loadProductPropertyList(@PathVariable("pid") String pid/*产品ID*/) {
        logger.info("获取产品ID");
        Integer product_id = Integer.parseInt(pid);

        logger.info("获取产品详情-属性值信息");
        Product product = new Product();
        product.setProduct_id(product_id);
        List<PropertyValue> propertyValueList = propertyValueService.getList(new PropertyValue().setPropertyValue_product(product), null);

        logger.info("获取产品详情-分类信息对应的属性列表");
        List<Property> propertyList = propertyService.getList(new Property().setProperty_category(product.getProduct_category()), null);

        logger.info("属性列表和属性值列表合并");
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
    @RequestMapping(value = "guess/{cid}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String guessYouLike(@PathVariable("cid") Integer cid, @RequestParam Integer guessNumber) {
        logger.info("获取猜你喜欢列表");
        Integer total = productService.getTotal(new Product().setProduct_category(new Category().setCategory_id(cid)), new Byte[]{0, 2});
        logger.info("分类ID为{}的产品总数为{}条", cid, total);
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

        logger.info("guessNumber值为{}，新guessNumber值为{}", guessNumber, i);
        List<Product> loveProductList = productService.getList(new Product().setProduct_category(new Category().setCategory_id(cid)), new Byte[]{0, 2}, null, new PageUtil().setCount(3).setPageStart(i));
        if (loveProductList != null) {
            logger.info("获取产品列表的相应的一张预览图片");
            for (Product loveProduct : loveProductList) {
                loveProduct.setSingleProductImageList(productImageService.getList(loveProduct.getProduct_id(), (byte) 0, new PageUtil(0, 1)));
            }
        }

        JSONObject jsonObject = new JSONObject();
        logger.info("获取数据成功！");
        jsonObject.put("success", true);
        jsonObject.put("loveProductList", JSONArray.parseArray(JSON.toJSONString(loveProductList)));
        jsonObject.put("guessNumber", i);
        return jsonObject.toJSONString();
    }
}
