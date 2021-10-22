package com.uzykj.mall.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.uzykj.mall.entity.*;
import com.uzykj.mall.entity.enums.ImageTypeEnum;
import com.uzykj.mall.entity.enums.ProductImageStoreEnum;
import com.uzykj.mall.service.*;
import com.uzykj.mall.util.FileUtil;
import com.uzykj.mall.util.PageUtil;
import com.uzykj.mall.util.qiniu.QiniuUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


/**
 * 后台管理-分类页
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminCategoryController {
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

    @Value("${storeService.use}")
    private String storeUse;
    @Value("${storeService.local.local_file_path}")
    private String localFilePath;
    @Value("${storeService.local.local_file_prefix}")
    private String localFileUrl;

    //转到后台管理-分类页-ajax
    @GetMapping("/category")
    public String goToPage(Map<String, Object> map) {
        PageUtil pageUtil = new PageUtil(0, 10);
        List<Category> categoryList = categoryService.getList(null, pageUtil);
        Integer categoryCount = categoryService.getTotal(null);
        pageUtil.setTotal(categoryCount);

        map.put("categoryList", categoryList);
        map.put("categoryCount", categoryCount);
        map.put("pageUtil", pageUtil);
        return "admin/categoryManagePage";
    }

    //转到后台管理-分类详情页-ajax
    @GetMapping("/category/{cid}")
    public String goToDetailsPage(Map<String, Object> map,
                                  @PathVariable Integer cid/* 分类ID */) {
        Category category = categoryService.get(cid);
        category.setPropertyList(propertyService.getList(new Property().setProperty_category(category), null));

        map.put("category", category);
        return "admin/include/categoryDetails";
    }

    //转到后台管理-分类添加页-ajax
    @GetMapping(value = "/category/new")
    public String goToAddPage() {
        return "admin/include/categoryDetails";
    }

    //添加分类信息-ajax
    @ResponseBody
    @PostMapping("/category")
    public String addCategory(@RequestParam String category_name/* 分类名称 */,
                              @RequestParam String category_image_src/* 分类图片路径 */) {
        JSONObject jsonObject = new JSONObject();
        Category category = new Category()
                .setCategory_name(category_name)
                .setCategory_image_src(category_image_src.substring(category_image_src.lastIndexOf("/") + 1));
        boolean yn = categoryService.add(category);
        if (yn) {
            int category_id = lastIDService.selectLastID();
            jsonObject.put("success", true);
            jsonObject.put("category_id", category_id);
        } else {
            jsonObject.put("success", false);
            throw new RuntimeException();
        }
        return jsonObject.toJSONString();
    }

    //更新分类信息-ajax
    @ResponseBody
    @PutMapping("/category/{category_id}")
    public String updateCategory(@RequestParam String category_name/* 分类名称 */,
                                 @RequestParam String category_image_src/* 分类图片路径 */,
                                 @PathVariable("category_id") Integer category_id/* 分类ID */) {
        JSONObject jsonObject = new JSONObject();
        Category category = new Category()
                .setCategory_id(category_id)
                .setCategory_name(category_name)
                .setCategory_image_src(category_image_src.substring(category_image_src.lastIndexOf("/") + 1));
        boolean yn = categoryService.update(category);
        if (yn) {
            jsonObject.put("success", true);
            jsonObject.put("category_id", category_id);
        } else {
            jsonObject.put("success", false);
            throw new RuntimeException();
        }

        return jsonObject.toJSONString();
    }

    //删除分类
    @ResponseBody
    @GetMapping("/category/delete/{arr}")
    public String deleteCategory(@PathVariable("arr") Integer[] category_id_list/* 商品id集合 */) {
        JSONObject object = new JSONObject();
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
                            log.info("已删除属性_值表数据：" + propertyValue_id_list.length + "条");
                        } else {
                            throw new RuntimeException("删除属性_值表数据异常");
                        }
                        if (propertyService.delete(property_id_list)) {
                            log.info("已删除属性表数据：" + property_id_list.length + "条");
                        } else {
                            throw new RuntimeException("删除属性数据异常");
                        }
                    } else {
                        log.info("没有商品属性数据");
                        object.put("success", false);
                    }

                    //删除商品图片
                    List<ProductImage> imageList = productImageService.getList(list.get(j).getProduct_id(), null);
                    if (imageList != null && imageList.size() != 0) {
                        Integer[] productImage_id_list = new Integer[imageList.size()];
                        for (int k = 0; k < imageList.size(); k++) {
                            productImage_id_list[k] = imageList.get(k).getProductImage_id();
                            String[] split = imageList.get(k).getProductImage_src().split("/");
                            try {
                                QiniuUtil.delete(split[split.length - 1], QiniuUtil.MALL_ZONE);
                            } catch (QiniuException e) {
                                log.info("七牛云图片资源删除失败！");
                                e.printStackTrace();
                            }
                        }
                        if (productImageService.delete(productImage_id_list)) {
                            log.info("已删除商品图片表数据：" + productImage_id_list.length + "条");
                        } else {
                            throw new RuntimeException("删除商品图片表数据异常");
                        }
                    } else {
                        log.info("没有商品属性数据");
                        object.put("success", false);
                    }
                }
                if (productService.delete(product_id_list)) {
                    log.info("已删除商品数据：" + product_id_list.length + "条");
                } else {
                    log.info("没有商品属性数据");
                    object.put("success", false);
                }
            }
        }

        if (categoryService.delete(category_id_list)) {
            object.put("success", true);
            log.info("已删除订单数据：" + category_id_list.length + "条");
        } else {
            object.put("success", false);
        }
        return object.toJSONString();
    }

    //按条件查询分类-ajax
    @ResponseBody
    @GetMapping("/category/{index}/{count}")
    public String getCategoryBySearch(@RequestParam(required = false) String category_name/* 分类名称 */,
                                      @PathVariable Integer index/* 页数 */,
                                      @PathVariable Integer count/* 行数 */) throws UnsupportedEncodingException {
        //移除不必要条件
        if (category_name != null) {
            //如果为非空字符串则解决中文乱码：URLDecoder.decode(String,"UTF-8");
            category_name = category_name.equals("") ? null : URLDecoder.decode(category_name, "UTF-8");
        }

        JSONObject object = new JSONObject();
        log.info("按条件获取第{}页的{}条分类", index + 1, count);
        PageUtil pageUtil = new PageUtil(index, count);
        List<Category> categoryList = categoryService.getList(category_name, pageUtil);
        log.info("按条件获取分类总数量");
        Integer categoryCount = categoryService.getTotal(category_name);
        log.info("获取分页信息");

        pageUtil.setTotal(categoryCount);
        object.put("categoryList", JSONArray.parseArray(JSON.toJSONString(categoryList)));
        object.put("categoryCount", categoryCount);
        object.put("totalPage", pageUtil.getTotalPage());
        object.put("pageUtil", pageUtil);

        return object.toJSONString();
    }


    // 上传分类图片-ajax
    @ResponseBody
    @PostMapping("/uploadCategoryImage")
    public String uploadCategoryImage(@RequestParam MultipartFile file, HttpSession session) {
        JSONObject object = new JSONObject();
        object.put("success", false);

        if (!file.isEmpty()) {
            try {
                String originalFileName = file.getOriginalFilename();
                if (storeUse.equals(ProductImageStoreEnum.qiniu.toString())) {
                    UpResult upload = QiniuUtil.upload(file.getInputStream(), originalFileName, QiniuUtil.MALL_ZONE);
                    if (upload != null) {
                        log.info("七牛云上传路径：" + upload.zoneName + upload.fileName);
                        object.put("success", true);
                        String fileUrl = QiniuUtil.getFileUrl(upload.fileName, QiniuUtil.MALL_DOMAIN);
                        object.put("fileUrl", fileUrl);
                    }
                } else if (storeUse.equals(ProductImageStoreEnum.local.toString())) {
                    // 转存文件
                    assert originalFileName != null;
                    String fileName = FileUtil.generNewFileName(originalFileName);
                    String filePath = FileUtil.generLocalFilePath(session, localFilePath, ImageTypeEnum.category.toString());
                    log.info("文件本地上传路径：" + filePath + fileName);
                    FileUtil.createDirectory(filePath);
                    file.transferTo(new File(filePath + fileName));
                    String fileUrl = FileUtil.generFileUrl(localFileUrl, ImageTypeEnum.category.toString(), fileName);
                    object.put("success", true);
                    object.put("fileUrl", fileUrl);
                }
            } catch (IOException e) {
                log.warn("文件上传失败！", e);
            }
        }
        return object.toJSONString();
    }
}