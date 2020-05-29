package com.uzykj.mall.entity;

import java.util.List;

public class Category {
    private Integer category_id;
    private String category_name;
    private String category_image_src;
    //产品列表
    private List<Product> productList;
    //产品二维集合
    private List<List<Product>> complexProductList;
    //属性列表
    private List<Property> propertyList;

    @Override
    public String toString() {
        return "Category{" +
                "category_id=" + category_id +
                ", category_name='" + category_name + '\'' +
                ", category_image_src='" + category_image_src + '\'' +
                '}';
    }

    public Category(){

    }

    public Category(Integer category_id, String category_name, String category_image_src) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_image_src = category_image_src;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public Category setCategory_id(Integer category_id) {
        this.category_id = category_id;
        return this;
    }

    public String getCategory_name() {
        return category_name;
    }

    public Category setCategory_name(String category_name) {
        this.category_name = category_name;
        return this;
    }

    public String getCategory_image_src() {
        return category_image_src;
    }

    public Category setCategory_image_src(String category_image_src) {
        this.category_image_src = category_image_src;
        return this;
    }

    public List<List<Product>> getComplexProductList() {
        return complexProductList;
    }

    public Category setComplexProductList(List<List<Product>> productList) {
        this.complexProductList = productList;
        return this;
    }

    public List<Property> getPropertyList() {
        return propertyList;
    }

    public Category setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
        return this;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public Category setProductList(List<Product> productList) {
        this.productList = productList;
        return this;
    }
}
