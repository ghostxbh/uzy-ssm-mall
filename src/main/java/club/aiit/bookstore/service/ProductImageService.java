package club.aiit.bookstore.service;

import club.aiit.bookstore.entity.ProductImage;
import club.aiit.bookstore.util.PageUtil;

import java.util.List;

public interface ProductImageService {
    boolean add(ProductImage productImage);

    boolean addList(List<ProductImage> productImageList);

    boolean update(ProductImage productImage);

    boolean delete(Integer[] productImage_id_list);

    List<ProductImage> getList(Integer product_id, Byte productImage_type, PageUtil pageUtil);

    ProductImage get(Integer productImage_id);

    Integer getTotal(Integer product_id, Byte productImage_type);
}
