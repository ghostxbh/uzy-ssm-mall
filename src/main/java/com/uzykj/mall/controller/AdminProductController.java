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
import com.uzykj.mall.util.OrderUtil;
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
import java.util.*;

/**
 * 后台管理-产品页
 */
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminProductController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private LastIDService lastIDService;

    @Value("${storeService.use}")
    public String storeUse;
    @Value("${storeService.local.local_file_path}")
    public String localFilePath;
    @Value("${storeService.local.local_file_prefix}")
    public String localFileUrl;

    //转到后台管理-产品页-ajax
    @GetMapping("/product")
    public String goToPage(HttpSession session, Map<String, Object> map) {
        List<Category> categoryList = categoryService.getList(null, null);
        PageUtil pageUtil = new PageUtil(0, 10);
        List<Product> productList = productService.getList(null, null, null, pageUtil);
        Integer productCount = productService.getTotal(null, null);
        pageUtil.setTotal(productCount);

        map.put("categoryList", categoryList);
        map.put("productList", productList);
        map.put("productCount", productCount);
        map.put("pageUtil", pageUtil);
        return "admin/productManagePage";
    }

    //转到后台管理-产品详情页-ajax
    @GetMapping("/product/{pid}")
    public String goToDetailsPage(HttpSession session, Map<String, Object> map,
                                  @PathVariable Integer pid/* 产品ID */) {
        log.info("获取product_id为{}的产品信息", pid);
        Product product = productService.get(pid);
        Integer product_id = product.getProduct_id();
        List<ProductImage> productImageList = productImageService.getList(product_id, null);

        List<ProductImage> singleProductImageList = new ArrayList<>(5);
        List<ProductImage> detailsProductImageList = new ArrayList<>(8);
        productImageList.stream().map(productImage -> productImage.getProductImage_type() == 0 ?
                singleProductImageList.add(productImage) : detailsProductImageList.add(productImage));

        product.setSingleProductImageList(singleProductImageList);
        product.setDetailProductImageList(detailsProductImageList);

        List<PropertyValue> propertyValueList = propertyValueService.getList(new PropertyValue().setPropertyValue_product(product), null);
        List<Property> propertyList = propertyService.getList(new Property().setProperty_category(product.getProduct_category()), null);

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
        List<Category> categoryList = categoryService.getList(null, null);

        map.put("product", product);
        map.put("propertyList", propertyList);
        map.put("categoryList", categoryList);
        return "admin/include/productDetails";
    }

    //转到后台管理-产品添加页-ajax
    @GetMapping("/product/new")
    public String goToAddPage(Map<String, Object> map) {
        List<Category> categoryList = categoryService.getList(null, null);
        List<Property> propertyList = propertyService.getList(new Property().setProperty_category(categoryList.get(0)), null);

        map.put("categoryList", categoryList);
        map.put("propertyList", propertyList);
        return "admin/include/productDetails";
    }

    //添加产品信息-ajax.
    @ResponseBody
    @PostMapping("/product")
    public String addProduct(@RequestParam String product_name/* 产品名称 */,
                             @RequestParam String product_title/* 产品标题 */,
                             @RequestParam Integer product_category_id/* 产品类型ID */,
                             @RequestParam Double product_sale_price/* 产品最低价 */,
                             @RequestParam Double product_price/* 产品最高价 */,
                             @RequestParam Byte product_isEnabled/* 产品状态 */,
                             @RequestParam String propertyJson/* 产品属性JSON */,
                             @RequestParam(required = false) String[] productSingleImageList/*产品预览图片名称数组*/,
                             @RequestParam(required = false) String[] productDetailsImageList/*产品详情图片名称数组*/) {
        JSONObject jsonObject = new JSONObject();
        Product product = new Product()
                .setProduct_name(product_name)
                .setProduct_title(product_title)
                .setProduct_category(new Category().setCategory_id(product_category_id))
                .setProduct_sale_price(product_sale_price)
                .setProduct_price(product_price)
                .setProduct_isEnabled(product_isEnabled)
                .setProduct_create_date(new Date());
        boolean yn = productService.add(product);
        if (!yn) {
            log.warn("产品添加失败！事务回滚");
            jsonObject.put("success", false);
            throw new RuntimeException();
        }

        int product_id = lastIDService.selectLastID();
        log.info("添加成功！,新增产品的ID值为：{}", product_id);

        JSONObject object = JSON.parseObject(propertyJson);
        Set<String> propertyIdSet = object.keySet();
        if (propertyIdSet.size() > 0) {
            log.info("整合产品子信息-产品属性");
            List<PropertyValue> propertyValueList = new ArrayList<>(5);
            for (String key : propertyIdSet) {
                String value = object.getString(key.toString());
                PropertyValue propertyValue = new PropertyValue()
                        .setPropertyValue_value(value)
                        .setPropertyValue_property(new Property().setProperty_id(Integer.valueOf(key)))
                        .setPropertyValue_product(new Product().setProduct_id(product_id));
                propertyValueList.add(propertyValue);
            }
            log.info("共有{}条产品属性数据", propertyValueList.size());
            yn = propertyValueService.addList(propertyValueList);
            if (yn) {
                log.info("产品属性添加成功！");
            } else {
                log.warn("产品属性添加失败！事务回滚");
                jsonObject.put("success", false);
                throw new RuntimeException();
            }
        }
        if (productSingleImageList != null && productSingleImageList.length > 0) {
            log.info("整合产品子信息-产品预览图片");
            List<ProductImage> productImageList = new ArrayList<>(5);
            for (String imageName : productSingleImageList) {
                productImageList.add(new ProductImage()
                        .setProductImage_type((byte) 0)
                        .setProductImage_src(imageName.substring(imageName.lastIndexOf("/") + 1))
                        .setProductImage_product(new Product().setProduct_id(product_id))
                );
            }
            log.info("共有{}条产品预览图片数据", productImageList.size());
            yn = productImageService.addList(productImageList);
            if (yn) {
                log.info("产品预览图片添加成功！");
            } else {
                log.warn("产品预览图片添加失败！事务回滚");
                jsonObject.put("success", false);
                throw new RuntimeException();
            }
        }

        if (productDetailsImageList != null && productDetailsImageList.length > 0) {
            log.info("整合产品子信息-产品详情图片");
            List<ProductImage> productImageList = new ArrayList<>(5);
            for (String imageName : productDetailsImageList) {
                productImageList.add(new ProductImage()
                        .setProductImage_type((byte) 1)
                        .setProductImage_src(imageName.substring(imageName.lastIndexOf("/") + 1))
                        .setProductImage_product(new Product().setProduct_id(product_id))
                );
            }
            log.info("共有{}条产品详情图片数据", productImageList.size());
            yn = productImageService.addList(productImageList);
            if (yn) {
                log.info("产品详情图片添加成功！");
            } else {
                log.warn("产品详情图片添加失败！事务回滚");
                jsonObject.put("success", false);
                throw new RuntimeException();
            }
        }
        jsonObject.put("success", true);
        jsonObject.put("product_id", product_id);

        return jsonObject.toJSONString();
    }

    //更新产品信息-ajax
    @ResponseBody
    @PutMapping("/product/{product_id}")
    public String updateProduct(@RequestParam String product_name/* 产品名称 */,
                                @RequestParam String product_title/* 产品标题 */,
                                @RequestParam Integer product_category_id/* 产品类型ID */,
                                @RequestParam Double product_sale_price/* 产品最低价 */,
                                @RequestParam Double product_price/* 产品最高价 */,
                                @RequestParam Byte product_isEnabled/* 产品状态 */,
                                @RequestParam String propertyAddJson/* 产品添加属性JSON */,
                                @RequestParam String propertyUpdateJson/* 产品更新属性JSON */,
                                @RequestParam(required = false) Integer[] propertyDeleteList/* 产品删除属性ID数组 */,
                                @RequestParam(required = false) String[] productSingleImageList/*产品预览图片名称数组*/,
                                @RequestParam(required = false) String[] productDetailsImageList/*产品详情图片名称数组*/,
                                @PathVariable("product_id") Integer product_id/* 产品ID */) {
        JSONObject jsonObject = new JSONObject();
        Product product = new Product()
                .setProduct_id(product_id)
                .setProduct_name(product_name)
                .setProduct_title(product_title)
                .setProduct_category(new Category().setCategory_id(product_category_id))
                .setProduct_sale_price(product_sale_price)
                .setProduct_price(product_price)
                .setProduct_isEnabled(product_isEnabled)
                .setProduct_create_date(new Date());
        log.info("更新产品信息，产品ID值为：{}", product_id);
        boolean yn = productService.update(product);
        if (!yn) {
            log.info("产品信息更新失败！事务回滚");
            jsonObject.put("success", false);
            throw new RuntimeException();
        }
        log.info("产品信息更新成功！");

        JSONObject object = JSON.parseObject(propertyAddJson);
        Set<String> propertyIdSet = object.keySet();
        if (propertyIdSet.size() > 0) {
            log.info("整合产品子信息-需要添加的产品属性");
            List<PropertyValue> propertyValueList = new ArrayList<>(5);
            for (String key : propertyIdSet) {
                String value = object.getString(key.toString());
                PropertyValue propertyValue = new PropertyValue()
                        .setPropertyValue_value(value)
                        .setPropertyValue_property(new Property().setProperty_id(Integer.valueOf(key)))
                        .setPropertyValue_product(product);
                propertyValueList.add(propertyValue);
            }
            log.info("共有{}条需要添加的产品属性数据", propertyValueList.size());
            yn = propertyValueService.addList(propertyValueList);
            if (yn) {
                log.info("产品属性添加成功！");
            } else {
                log.warn("产品属性添加失败！事务回滚");
                jsonObject.put("success", false);
                throw new RuntimeException();
            }
        }

        object = JSON.parseObject(propertyUpdateJson);
        propertyIdSet = object.keySet();
        if (propertyIdSet.size() > 0) {
            log.info("整合产品子信息-需要更新的产品属性");
            List<PropertyValue> propertyValueList = new ArrayList<>(5);
            for (String key : propertyIdSet) {
                String value = object.getString(key.toString());
                PropertyValue propertyValue = new PropertyValue()
                        .setPropertyValue_value(value)
                        .setPropertyValue_id(Integer.valueOf(key));
                propertyValueList.add(propertyValue);
            }
            log.info("共有{}条需要更新的产品属性数据", propertyValueList.size());
            for (int i = 0; i < propertyValueList.size(); i++) {
                log.info("正在更新第{}条，共{}条", i + 1, propertyValueList.size());
                yn = propertyValueService.update(propertyValueList.get(i));
                if (yn) {
                    log.info("产品属性更新成功！");
                } else {
                    log.warn("产品属性更新失败！事务回滚");
                    jsonObject.put("success", false);
                    throw new RuntimeException();
                }
            }
        }
        if (propertyDeleteList != null && propertyDeleteList.length > 0) {
            log.info("整合产品子信息-需要删除的产品属性");
            log.info("共有{}条需要删除的产品属性数据", propertyDeleteList.length);
            yn = propertyValueService.delete(propertyDeleteList);
            if (yn) {
                log.info("产品属性删除成功！");
            } else {
                log.warn("产品属性删除失败！事务回滚");
                jsonObject.put("success", false);
                throw new RuntimeException();
            }
        }
        if (productSingleImageList != null && productSingleImageList.length > 0) {
            log.info("整合产品子信息-产品预览图片");
            List<ProductImage> productImageList = new ArrayList<>(5);
            for (String imageName : productSingleImageList) {
                productImageList.add(new ProductImage()
                        .setProductImage_type((byte) 0)
                        .setProductImage_src(imageName.substring(imageName.lastIndexOf("/") + 1))
                        .setProductImage_product(product)
                );
            }
            log.info("共有{}条产品预览图片数据", productImageList.size());
            yn = productImageService.addList(productImageList);
            if (yn) {
                log.info("产品预览图片添加成功！");
            } else {
                log.warn("产品预览图片添加失败！事务回滚");
                jsonObject.put("success", false);
                throw new RuntimeException();
            }
        }
        if (productDetailsImageList != null && productDetailsImageList.length > 0) {
            log.info("整合产品子信息-产品详情图片");
            List<ProductImage> productImageList = new ArrayList<>(5);
            for (String imageName : productDetailsImageList) {
                productImageList.add(new ProductImage()
                        .setProductImage_type((byte) 1)
                        .setProductImage_src(imageName)
                        .setProductImage_product(product)
                );
            }
            log.info("共有{}条产品详情图片数据", productImageList.size());
            yn = productImageService.addList(productImageList);
            if (yn) {
                log.info("产品详情图片添加成功！");
            } else {
                log.warn("产品详情图片添加失败！事务回滚");
                jsonObject.put("success", false);
                throw new RuntimeException();
            }
        }
        jsonObject.put("success", true);
        jsonObject.put("product_id", product_id);

        return jsonObject.toJSONString();
    }

    //删除商品
    @ResponseBody
    @PostMapping("/product/delete/{arr}")
    public String deleteProduct(HttpSession session,
                                @PathVariable("arr") Integer[] product_id_list/* 商品id集合 */) {
        JSONObject object = new JSONObject();
        log.info("删除:用户id数组：" + product_id_list.toString());
        for (int i = 0; i < product_id_list.length; i++) {

            //删除商品属性
            List<PropertyValue> propertyValueList = propertyValueService.getListByProductId(product_id_list[i], null);
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
            List<ProductImage> imageList = productImageService.getList(product_id_list[i], null);
            if (imageList != null && imageList.size() != 0) {
                Integer[] productImage_id_list = new Integer[imageList.size()];
                for (int k = 0; k < imageList.size(); k++) {
                    productImage_id_list[k] = imageList.get(k).getProductImage_id();
                    String store = imageList.get(k).getProductimage_store();
                    try {
                        String[] split = imageList.get(k).getProductImage_src().split("/");
                        String fileName = split[split.length - 1];
                        if (ProductImageStoreEnum.local.toString().equals(store)) {
                            String filePath = session.getServletContext().getRealPath("/") + localFilePath + ImageTypeEnum.product;
                            File file = new File(filePath + fileName);
                            if (file.isFile()) {
                                file.delete();
                            }
                        } else if (ProductImageStoreEnum.qiniu.toString().equals(store)) {
                            QiniuUtil.delete(fileName, QiniuUtil.MALL_ZONE);
                        }
                    } catch (QiniuException e) {
                        log.error("图片资源删除失败！", e);
                    }
                }
                if (productImageService.delete(productImage_id_list)) {
                    log.info("已删除商品图片表数据：" + productImage_id_list.length + "条");
                } else {
                    throw new RuntimeException("删除商品图片表数据异常");
                }
            } else {
                log.info("没有商品图片数据");
                object.put("success", false);
            }
        }

        if (productService.delete(product_id_list)) {
            object.put("success", true);
            log.info("已删除商品：" + product_id_list.length + "条");
        } else {
            object.put("success", false);
        }
        return object.toJSONString();
    }

    //按条件查询产品-ajax
    @ResponseBody
    @GetMapping("/product/{index}/{count}")
    public String getProductBySearch(@RequestParam(required = false) String product_name/* 产品名称 */,
                                     @RequestParam(required = false) Integer category_id/* 产品类型ID */,
                                     @RequestParam(required = false) Double product_sale_price/* 产品最低价 */,
                                     @RequestParam(required = false) Double product_price/* 产品最高价 */,
                                     @RequestParam(required = false) Byte[] product_isEnabled_array/* 产品状态数组 */,
                                     @RequestParam(required = false) String orderBy/* 排序字段 */,
                                     @RequestParam(required = false, defaultValue = "true") Boolean isDesc/* 是否倒序 */,
                                     @PathVariable Integer index/* 页数 */,
                                     @PathVariable Integer count/* 行数 */) throws UnsupportedEncodingException {
        //移除不必要条件
        if (product_isEnabled_array != null && (product_isEnabled_array.length <= 0 || product_isEnabled_array.length >= 3)) {
            product_isEnabled_array = null;
        }
        if (category_id != null && category_id == 0) {
            category_id = null;
        }
        if (product_name != null) {
            //如果为非空字符串则解决中文乱码：URLDecoder.decode(String,"UTF-8");
            product_name = product_name.equals("") ? null : URLDecoder.decode(product_name, "UTF-8");
        }
        if (orderBy != null && orderBy.equals("")) {
            orderBy = null;
        }
        //封装查询条件
        Product product = new Product()
                .setProduct_name(product_name)
                .setProduct_category(new Category().setCategory_id(category_id))
                .setProduct_price(product_price)
                .setProduct_sale_price(product_sale_price);
        OrderUtil orderUtil = null;
        if (orderBy != null) {
            log.info("根据{}排序，是否倒序:{}", orderBy, isDesc);
            orderUtil = new OrderUtil(orderBy, isDesc);
        }

        JSONObject object = new JSONObject();
        log.info("按条件获取第{}页的{}条产品", index + 1, count);
        PageUtil pageUtil = new PageUtil(index, count);
        List<Product> productList = productService.getList(product, product_isEnabled_array, orderUtil, pageUtil);
        Integer productCount = productService.getTotal(product, product_isEnabled_array);
        pageUtil.setTotal(productCount);

        object.put("productList", JSONArray.parseArray(JSON.toJSONString(productList)));
        object.put("productCount", productCount);
        object.put("totalPage", pageUtil.getTotalPage());
        object.put("pageUtil", pageUtil);

        return object.toJSONString();
    }

    //按类型ID查询属性-ajax
    @ResponseBody
    @GetMapping("/property/type/{property_category_id}")
    public String getPropertyByCategoryId(@PathVariable Integer property_category_id/* 属性所属类型ID*/) {
        //封装查询条件
        Category category = new Category()
                .setCategory_id(property_category_id);
        JSONObject object = new JSONObject();
        log.info("按类型获取属性列表，类型ID：{}", property_category_id);
        List<Property> propertyList = propertyService.getList(new Property().setProperty_category(category), null);
        object.put("propertyList", JSONArray.parseArray(JSON.toJSONString(propertyList)));

        return object.toJSONString();
    }

    //按ID删除产品图片并返回最新结果-ajax
    @ResponseBody
    @DeleteMapping("/productImage/{productImage_id}")
    public String deleteProductImageById(HttpSession session,
                                         @PathVariable Integer productImage_id/* 产品图片ID */) {
        JSONObject object = new JSONObject();
        log.info("获取productImage_id为{}的产品图片信息", productImage_id);
        @SuppressWarnings("unused")
        ProductImage productImage = productImageService.get(productImage_id);
        Boolean yn = false;
        try {
            String[] split = productImage.getProductImage_src().split("/");
            String fileName = split[split.length - 1];
            if (ProductImageStoreEnum.local.toString().equals(productImage.getProductimage_store())) {
                String filePath = session.getServletContext().getRealPath("/") + localFilePath + ImageTypeEnum.product;
                File file = new File(filePath + fileName);
                if (file.isFile()) {
                    file.delete();
                }
            } else if (ProductImageStoreEnum.qiniu.toString().equals(productImage.getProductimage_store())) {
                QiniuUtil.delete(fileName, QiniuUtil.MALL_ZONE);
            }
            yn = productImageService.delete(new Integer[]{productImage_id});
        } catch (QiniuException e) {
            log.error("图片删除失败！", e);
        }
        if (yn) {
            log.info("删除图片成功！");
            object.put("success", true);
        } else {
            log.warn("删除图片失败！事务回滚");
            object.put("success", false);
            throw new RuntimeException();
        }
        return object.toJSONString();
    }

    //上传产品图片-ajax
    @ResponseBody
    @PostMapping("/uploadProductImage")
    public String uploadProductImage(@RequestParam MultipartFile file,
                                     @RequestParam String imageType, HttpSession session) {
        JSONObject object = new JSONObject();
        object.put("success", false);

        if (!file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            log.info("获取图片原始文件名：{}", originalFileName);
            String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
            String fileName = UUID.randomUUID() + extension;
            try {
                if (storeUse.equals(ProductImageStoreEnum.qiniu.toString())) {
                    UpResult upload = QiniuUtil.upload(file.getInputStream(), originalFileName, QiniuUtil.MALL_ZONE);
                    if (upload != null) {
                        log.info("七牛云路径：" + upload.zoneName + upload.fileName);
                        String fileUrl = QiniuUtil.getFileUrl(upload.fileName, QiniuUtil.MALL_DOMAIN);
                        object.put("success", true);
                        object.put("fileUrl", fileUrl);
                    }
                } else if (storeUse.equals(ProductImageStoreEnum.local.toString())) {
                    String filePath = session.getServletContext().getRealPath("/") + localFilePath + ImageTypeEnum.product;
                    // 转存文件
                    FileUtil.createDirectory(filePath);
                    String fileAddr = filePath + fileName;
                    log.info("文件本地上传路径：" + fileAddr);

                    file.transferTo(new File(fileAddr));
                    String fileUrl = localFileUrl + fileName;

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