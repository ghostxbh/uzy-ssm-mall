package com.uzykj.mall.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.uzykj.mall.controller.BaseController;
import com.uzykj.mall.entity.*;
import com.uzykj.mall.service.*;
import com.uzykj.mall.util.FileIsExists;
import com.uzykj.mall.util.PageUtil;
import com.uzykj.mall.util.qiniu.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 后台管理-分类页
 */
@Controller
public class CategoryController extends BaseController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private LastIDService lastIDService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private PropertyValueService propertyValueService;

    //转到后台管理-分类页-ajax
    @RequestMapping(value = "admin/category", method = RequestMethod.GET)
    public String goToPage(HttpSession session, Map<String, Object> map) {
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }

        logger.info("获取前10条分类列表");
        PageUtil pageUtil = new PageUtil(0, 10);
        List<Category> categoryList = categoryService.getList(null, pageUtil);
        map.put("categoryList", categoryList);
        logger.info("获取分类总数量");
        Integer categoryCount = categoryService.getTotal(null);
        map.put("categoryCount", categoryCount);
        logger.info("获取分页信息");
        pageUtil.setTotal(categoryCount);
        map.put("pageUtil", pageUtil);

        logger.info("转到后台管理-分类页-ajax方式");
        return "admin/categoryManagePage";
    }

    //转到后台管理-分类详情页-ajax
    @RequestMapping(value = "admin/category/{cid}", method = RequestMethod.GET)
    public String goToDetailsPage(HttpSession session, Map<String, Object> map, @PathVariable Integer cid/* 分类ID */) {
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }
        logger.info("获取category_id为{}的分类信息", cid);
        Category category = categoryService.get(cid);
        logger.info("获取分类子信息-属性列表");
        category.setPropertyList(propertyService.getList(new Property().setProperty_category(category), null));
        map.put("category", category);

        logger.info("转到后台管理-分类详情页-ajax方式");
        return "admin/include/categoryDetails";
    }

    //转到后台管理-分类添加页-ajax
    @RequestMapping(value = "admin/category/new", method = RequestMethod.GET)
    public String goToAddPage(HttpSession session, Map<String, Object> map) {
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }

        logger.info("转到后台管理-分类添加页-ajax方式");
        return "admin/include/categoryDetails";
    }

    //添加分类信息-ajax
    @ResponseBody
    @RequestMapping(value = "admin/category", method = RequestMethod.POST)
    public String addCategory(@RequestParam String category_name/* 分类名称 */,
                              @RequestParam String category_image_src/* 分类图片路径 */) {
        JSONObject jsonObject = new JSONObject();
        logger.info("整合分类信息");
        Category category = new Category()
                .setCategory_name(category_name)
                .setCategory_image_src(category_image_src.substring(category_image_src.lastIndexOf("/") + 1));
        logger.info("添加分类信息");
        boolean yn = categoryService.add(category);
        if (yn) {
            int category_id = lastIDService.selectLastID();
            logger.info("添加成功！,新增分类的ID值为：{}", category_id);
            jsonObject.put("success", true);
            jsonObject.put("category_id", category_id);
        } else {
            jsonObject.put("success", false);
            logger.warn("添加失败！事务回滚");
            throw new RuntimeException();
        }

        return jsonObject.toJSONString();
    }

    //更新分类信息-ajax
    @ResponseBody
    @RequestMapping(value = "admin/category/{category_id}", method = RequestMethod.PUT)
    public String updateCategory(@RequestParam String category_name/* 分类名称 */,
                                 @RequestParam String category_image_src/* 分类图片路径 */,
                                 @PathVariable("category_id") Integer category_id/* 分类ID */) {
        JSONObject jsonObject = new JSONObject();
        logger.info("整合分类信息");
        Category category = new Category()
                .setCategory_id(category_id)
                .setCategory_name(category_name)
                .setCategory_image_src(category_image_src.substring(category_image_src.lastIndexOf("/") + 1));
        logger.info("更新分类信息，分类ID值为：{}", category_id);
        boolean yn = categoryService.update(category);
        if (yn) {
            logger.info("更新成功！");
            jsonObject.put("success", true);
            jsonObject.put("category_id", category_id);
        } else {
            jsonObject.put("success", false);
            logger.info("更新失败！事务回滚");
            throw new RuntimeException();
        }

        return jsonObject.toJSONString();
    }

    //删除分类
    @ResponseBody
    @RequestMapping(value = "admin/category/delete/{arr}", method = RequestMethod.GET)
    public String deleteCategory(@PathVariable("arr") Integer[] category_id_list/* 商品id集合 */) {
        JSONObject object = new JSONObject();
        logger.info("删除:用户id数组：" + category_id_list.toString());
        for (int i = 0; i < category_id_list.length; i++) {
            Product product = new Product()
                    .setProduct_category(new Category().setCategory_id(category_id_list[i]));
            List<Product> list = productService.getList(product, null, null, null);
            if (list != null && list.size() != 0) {
                Integer[] product_id_list = new Integer[list.size()];
                for (int j = 0; j < list.size(); j++) {
                    product_id_list[j] = list.get(j).getProduct_id();

                    //删除商品属性
                    List<PropertyValue> propertyValueList = propertyValueService.getListByProductId(list.get(j).getProduct_id(), null);
                    if (propertyValueList != null && propertyValueList.size() != 0) {
                        Integer[] propertyValue_id_list = new Integer[propertyValueList.size()];
                        Integer[] property_id_list = new Integer[propertyValueList.size()];
                        for (int k = 0; k < propertyValueList.size(); k++) {
                            propertyValue_id_list[k] = propertyValueList.get(k).getPropertyValue_id();
                            property_id_list[k] = propertyValueList.get(k).getPropertyValue_property().getProperty_id();
                        }
                        if (propertyValueService.delete(propertyValue_id_list)) {
                            logger.info("已删除属性_值表数据：" + propertyValue_id_list.length + "条");
                        } else {
                            throw new RuntimeException("删除属性_值表数据异常");
                        }
                        if (propertyService.delete(property_id_list)) {
                            logger.info("已删除属性表数据：" + property_id_list.length + "条");
                        } else {
                            throw new RuntimeException("删除属性数据异常");
                        }
                    } else {
                        logger.info("没有商品属性数据");
                        object.put("success", false);
                    }

                    //删除商品图片
                    List<ProductImage> imageList = productImageService.getList(list.get(j).getProduct_id(), null, null);
                    if (imageList != null && imageList.size() != 0) {
                        Integer[] productImage_id_list = new Integer[imageList.size()];
                        for (int k = 0; k < imageList.size(); k++) {
                            productImage_id_list[k] = imageList.get(k).getProductImage_id();
                            String[] split = imageList.get(k).getProductImage_src().split("/");
                            try {
                                QiniuUtil.delete(split[split.length - 1], QiniuUtil.MALL_ZONE);
                            } catch (QiniuException e) {
                                logger.info("七牛云图片资源删除失败！");
                                e.printStackTrace();
                            }
                        }
                        if (productImageService.delete(productImage_id_list)) {
                            logger.info("已删除商品图片表数据：" + productImage_id_list.length + "条");
                        } else {
                            throw new RuntimeException("删除商品图片表数据异常");
                        }
                    } else {
                        logger.info("没有商品属性数据");
                        object.put("success", false);
                    }
                }
                if (productService.delete(product_id_list)) {
                    logger.info("已删除商品数据：" + product_id_list.length + "条");
                } else {
                    logger.info("没有商品属性数据");
                    object.put("success", false);
                }
            }
        }

        if (categoryService.delete(category_id_list)) {
            object.put("success", true);
            logger.info("已删除订单数据：" + category_id_list.length + "条");
        } else {
            object.put("success", false);
        }
        return object.toJSONString();
    }

    //按条件查询分类-ajax
    @ResponseBody
    @RequestMapping(value = "admin/category/{index}/{count}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String getCategoryBySearch(@RequestParam(required = false) String category_name/* 分类名称 */,
                                      @PathVariable Integer index/* 页数 */,
                                      @PathVariable Integer count/* 行数 */) throws UnsupportedEncodingException {
        //移除不必要条件
        if (category_name != null) {
            //如果为非空字符串则解决中文乱码：URLDecoder.decode(String,"UTF-8");
            category_name = category_name.equals("") ? null : URLDecoder.decode(category_name, "UTF-8");
        }

        JSONObject object = new JSONObject();
        logger.info("按条件获取第{}页的{}条分类", index + 1, count);
        PageUtil pageUtil = new PageUtil(index, count);
        List<Category> categoryList = categoryService.getList(category_name, pageUtil);
        object.put("categoryList", JSONArray.parseArray(JSON.toJSONString(categoryList)));
        logger.info("按条件获取分类总数量");
        Integer categoryCount = categoryService.getTotal(category_name);
        object.put("categoryCount", categoryCount);
        logger.info("获取分页信息");
        pageUtil.setTotal(categoryCount);
        object.put("totalPage", pageUtil.getTotalPage());
        object.put("pageUtil", pageUtil);

        return object.toJSONString();
    }

    // 上传分类图片-ajax
    @ResponseBody
    @RequestMapping(value = "admin/uploadCategoryImage", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String uploadCategoryImage(@RequestParam MultipartFile file, HttpSession session) {
        JSONObject object = new JSONObject();
        if (!file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            if (QiniuUtil.IS_ENABLE.equals("true")) {
                try {
                    logger.info("文件上传中...");
                    UpResult upload = QiniuUtil.upload(file.getInputStream(), originalFileName, QiniuUtil.MALL_ZONE);
                    if (upload != null){
                        logger.info("七牛云路径：", upload.zoneName+upload.fileName);
                        logger.info("文件上传完成");
                        object.put("success", true);
                        String fileUrl = QiniuUtil.getFileUrl(upload.fileName, QiniuUtil.MALL_DOMAIN);
                        object.put("fileUrl", fileUrl);
                    }else{
                        logger.info("文件上传失败！");
                        object.put("success", false);
                    }
                } catch (IOException e) {
                    logger.warn("文件上传失败！", e);
                    object.put("success", false);
                }
            } else if (QiniuUtil.IS_ENABLE.equals("false")){
                logger.info("获取图片原始文件名：{}", originalFileName);
                // 转存文件
                FileIsExists.createDirectory(QiniuUtil.LOCAL_FILE_PATH);
                try {
                    file.transferTo(new File(QiniuUtil.LOCAL_FILE_PATH + originalFileName));
                } catch (IOException e) {
                    logger.warn("文件上传失败！", e);
                    object.put("success", false);
                }
                object.put("success", true);
                String fileUrl = "http://localhost:8080/mall/res/images/store/" + originalFileName;
                object.put("fileUrl", fileUrl);
            }
        }
        return object.toJSONString();
    }
}