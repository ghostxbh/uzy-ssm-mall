package com.uzykj.mall.entity;

public class ProductImage {
    private Integer productImage_id;
    private Byte productImage_type;
    private String productImage_src;
    private String productimage_store;
    private Product productImage_product;

    public ProductImage() {

    }

    @Override
    public String toString() {
        return "ProductImage{" +
                "productImage_id=" + productImage_id +
                ", productImage_type=" + productImage_type +
                ", productImage_src='" + productImage_src + '\'' +
                ", productImage_product=" + productImage_product +
                '}';
    }

    public ProductImage(Integer productImage_id, Byte productImage_type, String productImage_src, Product productImage_product) {
        this.productImage_id = productImage_id;
        this.productImage_type = productImage_type;
        this.productImage_src = productImage_src;
        this.productImage_product = productImage_product;
    }

    public Integer getProductImage_id() {
        return productImage_id;
    }

    public ProductImage setProductImage_id(Integer productImage_id) {
        this.productImage_id = productImage_id;
        return this;
    }

    public Byte getProductImage_type() {
        return productImage_type;
    }

    public ProductImage setProductImage_type(Byte productImage_type) {
        this.productImage_type = productImage_type;
        return this;
    }

    public String getProductImage_src() {
        return productImage_src;
    }

    public ProductImage setProductImage_src(String productImage_src) {
        this.productImage_src = productImage_src;
        return this;
    }

    public String getProductimage_store() {
        return productimage_store;
    }

    public ProductImage setProductimage_store(String productimage_store) {
        this.productimage_store = productimage_store;
        return this;
    }

    public Product getProductImage_product() {
        return productImage_product;
    }

    public ProductImage setProductImage_product(Product productImage_product) {
        this.productImage_product = productImage_product;
        return this;
    }
}
