package cn.jkj521.bookstore.controller.fore;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import cn.jkj521.bookstore.controller.BaseController;
import cn.jkj521.bookstore.entity.Category;
import cn.jkj521.bookstore.entity.Product;
import cn.jkj521.bookstore.entity.User;
import cn.jkj521.bookstore.service.*;
import cn.jkj521.bookstore.util.OrderUtil;
import cn.jkj521.bookstore.util.PageUtil;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class ForeProductListController extends BaseController {
    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "categoryService")
    private CategoryService categoryService;
    @Resource(name = "productImageService")
    private ProductImageService productImageService;
    @Resource(name = "reviewService")
    private ReviewService reviewService;
    @Resource(name = "productOrderService")
    private ProductOrderService productOrderService;
    @Resource(name = "productOrderItemService")
    private ProductOrderItemService productOrderItemService;


    //转到前台天猫-产品搜索列表页
    @RequestMapping(value = "product", method = RequestMethod.GET)
    public String goToPage(HttpSession session, Map<String, Object> map,
                           @RequestParam(value = "category_id", required = false) Integer category_id/* 分类ID */,
                           @RequestParam(value = "product_name", required = false) String product_name/* 产品名称 */,
                           HttpServletResponse response) throws UnsupportedEncodingException {
        logger.info("检查用户是否登录");
        Object userId = checkUser(session);

        if (userId != null) {
            logger.info("获取用户信息");
            User user = (User)session.getAttribute("user");
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
        
        if (category_id == null && product_name == null) {
            return "redirect:/";
        }
        if (product_name != null && product_name.trim().equals("")) {
            return "redirect:/";
        }

        logger.info("整合搜索信息");
        Product product = new Product();
        @SuppressWarnings("unused")
		OrderUtil orderUtil = null;
        String searchValue = null;
        Integer searchType = null;

        if (category_id != null) {
            product.setProduct_category(new Category().setCategory_id(category_id));
            searchType = category_id;
        }
        //关键词数组
        String[] product_name_split = null;
        //产品列表
        List<Product> productList;
        //产品总数量
        Integer productCount;
        //分页工具
        PageUtil pageUtil = new PageUtil(0, 20);
        if (product_name != null) {
            //product_name = new String(product_name.getBytes("ISO8859-1"), "UTF-8");
            product_name_split = product_name.split(" ");
            logger.warn("提取的关键词有{}", Arrays.toString(product_name_split));
            product.setProduct_name(product_name);
            searchValue = product_name;
        }
        if (product_name_split != null && product_name_split.length > 1) {
            logger.info("获取组合商品列表");
            productList = productService.getMoreList(product, new Byte[]{0, 2}, null, pageUtil, product_name_split);
            logger.info("按组合条件获取产品总数量");
            productCount = productService.getMoreListTotal(product, new Byte[]{0, 2}, product_name_split);
        } else {
            logger.info("获取商品列表");
            productList = productService.getList(product, new Byte[]{0, 2}, null, pageUtil);
            logger.info("按条件获取产品总数量");
            productCount = productService.getTotal(product, new Byte[]{0, 2});
        }
        logger.info("获取商品列表的对应信息");
        for (Product p : productList) {
            p.setSingleProductImageList(productImageService.getList(p.getProduct_id(), (byte) 0, null));
            p.setProduct_sale_count(productOrderItemService.getSaleCountByProductId(p.getProduct_id()));
            p.setProduct_review_count(reviewService.getTotalByProductId(p.getProduct_id()));
            p.setProduct_category(categoryService.get(p.getProduct_category().getCategory_id()));
        }
        logger.info("获取分类列表");
        List<Category> categoryList = categoryService.getList(null, new PageUtil(0, 5));
        logger.info("获取分页信息");
        pageUtil.setTotal(productCount);

        map.put("categoryList", categoryList);
        map.put("totalPage", pageUtil.getTotalPage());
        map.put("pageUtil", pageUtil);
        map.put("productList", productList);
        map.put("searchValue", searchValue);
        map.put("searchType", searchType);

        logger.info("转到前台天猫-产品搜索列表页");
        return "fore/productListPage";
    }

    //产品高级查询
    @RequestMapping(value = "product/{index}/{count}", method = RequestMethod.GET)
    public String searchProduct(HttpSession session, Map<String, Object> map,
                                @PathVariable("index") Integer index/* 页数 */,
                                @PathVariable("count") Integer count/* 行数*/,
                                @RequestParam(value = "category_id", required = false) Integer category_id/* 分类ID */,
                                @RequestParam(value = "product_name", required = false) String product_name/* 产品名称 */,
                                @RequestParam(required = false) String orderBy/* 排序字段 */,
                                @RequestParam(required = false, defaultValue = "true") Boolean isDesc/* 是否倒序 */) throws UnsupportedEncodingException {
        logger.info("整合搜索信息");
        Product product = new Product();
        OrderUtil orderUtil = null;
        String searchValue = null;
        Integer searchType = null;

        if (category_id != null) {
            product.setProduct_category(new Category().setCategory_id(category_id));
            searchType = category_id;
        }
        if (product_name != null) {
            product.setProduct_name(product_name);
        }
        if (orderBy != null) {
            logger.info("根据{}排序，是否倒序:{}", orderBy, isDesc);
            orderUtil = new OrderUtil(orderBy, isDesc);
        }
        //关键词数组
        String[] product_name_split = null;
        //产品列表
        List<Product> productList;
        //产品总数量
        Integer productCount;
        //分页工具
        PageUtil pageUtil = new PageUtil(0, 20);
        if (product_name != null) {
            product_name = new String(product_name.getBytes("ISO8859-1"), "UTF-8");
            product_name_split = product_name.split(" ");
            logger.warn("提取的关键词有{}", Arrays.toString(product_name_split));
            product.setProduct_name(product_name);
            searchValue = product_name;
        }
        if (product_name_split != null && product_name_split.length > 1) {
            logger.info("获取组合商品列表");
            productList = productService.getMoreList(product, new Byte[]{0, 2}, orderUtil, pageUtil, product_name_split);
            logger.info("按组合条件获取产品总数量");
            productCount = productService.getMoreListTotal(product, new Byte[]{0, 2}, product_name_split);
        } else {
            logger.info("获取商品列表");
            productList = productService.getList(product, new Byte[]{0, 2}, orderUtil, pageUtil);
            logger.info("按条件获取产品总数量");
            productCount = productService.getTotal(product, new Byte[]{0, 2});
        }
        logger.info("获取商品列表的对应信息");
        for (Product p : productList) {
            p.setSingleProductImageList(productImageService.getList(p.getProduct_id(), (byte) 0, null));
            p.setProduct_sale_count(productOrderItemService.getSaleCountByProductId(p.getProduct_id()));
            p.setProduct_review_count(reviewService.getTotalByProductId(p.getProduct_id()));
            p.setProduct_category(categoryService.get(p.getProduct_category().getCategory_id()));
        }
        logger.info("获取分类列表");
        List<Category> categoryList = categoryService.getList(null, new PageUtil(0, 5));
        logger.info("获取分页信息");
        pageUtil.setTotal(productCount);

        map.put("productCount", productCount);
        map.put("totalPage", pageUtil.getTotalPage());
        map.put("pageUtil", pageUtil);
        map.put("productList", JSONArray.parseArray(JSON.toJSONString(productList)));
        map.put("orderBy", orderBy);
        map.put("isDesc", isDesc);
        map.put("searchValue", searchValue);
        map.put("searchType", searchType);
        map.put("categoryList", categoryList);

        return "fore/productListPage";
    }
}