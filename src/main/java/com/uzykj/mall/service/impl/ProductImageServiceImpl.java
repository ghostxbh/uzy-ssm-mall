package com.uzykj.mall.service.impl;

import com.uzykj.mall.dao.ProductImageMapper;
import com.uzykj.mall.entity.ProductImage;
import com.uzykj.mall.service.ProductImageService;
import com.uzykj.mall.util.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("productImageService")
public class ProductImageServiceImpl implements ProductImageService {
    private ProductImageMapper productImageMapper;
    @Resource(name = "productImageMapper")
    public void setProductImageMapper(ProductImageMapper productImageMapper) {
        this.productImageMapper = productImageMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean add(ProductImage productImage) {
        return productImageMapper.insertOne(productImage)>0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean addList(List<ProductImage> productImageList) {
        return productImageMapper.insertList(productImageList) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean update(ProductImage productImage) {
        return productImageMapper.updateOne(productImage)>0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean delete(Integer[] productImage_id_list) {
        return productImageMapper.delete(productImage_id_list)>0;
    }

    @Override
    public List<ProductImage> getList(Integer product_id, PageUtil pageUtil) {
        List<ProductImage> imageList = productImageMapper.select(product_id, pageUtil);
        // TODO 当前未使用七牛
        /*for (ProductImage image : imageList) {
            image.setProductImage_src(QiniuUtil.getFileUrl(image.getProductImage_src(),QiniuUtil.BOOKSTORE_DOMAIN));
        }*/
        return imageList;
    }

    @Override
    public ProductImage get(Integer productImage_id) {
        return productImageMapper.selectOne(productImage_id);
    }

    @Override
    public Integer getTotal(Integer product_id, Byte productImage_type) {
        return productImageMapper.selectTotal(product_id,productImage_type);
    }
}
