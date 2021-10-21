package com.uzykj.mall.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Product {
    private Integer product_id;
    private String product_name;
    private String product_title;
    private Double product_price;
    private Double product_sale_price;
    private Date product_create_date;
    private Integer product_category_id;
    private Category product_category;
    private Byte product_isEnabled;
    private List<PropertyValue> propertyValueList;
    private List<ProductImage> singleProductImageList;
    private List<ProductImage> detailProductImageList;
    private List<Review> reviewList;
    private List<ProductOrderItem> productOrderItemList;
    //销量数
    private Integer product_sale_count;
    //评价数
    private Integer product_review_count;
    public Object setProduct_sale_co;

    @Override
    public String toString() {
        return "Product{" +
                "product_id=" + product_id +
                ", product_name='" + product_name + '\'' +
                ", product_title='" + product_title + '\'' +
                ", product_price=" + product_price +
                ", product_sale_price=" + product_sale_price +
                ", product_create_date=" + product_create_date +
                ", product_category=" + product_category +
                ", product_isEnabled=" + product_isEnabled +
                ", propertyValueList=" + propertyValueList +
                ", singleProductImageList=" + singleProductImageList +
                ", detailProductImageList=" + detailProductImageList +
                ", reviewList=" + reviewList +
                ", productOrderItemList=" + productOrderItemList +
                ", product_sale_count=" + product_sale_count +
                ", product_review_count=" + product_review_count +
                '}';
    }

    public Product(){

    }

    public Product(Integer product_id, String product_name, Double product_sale_price, Category product_category, Byte product_isEnabled) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_sale_price = product_sale_price;
        this.product_category = product_category;
        this.product_isEnabled = product_isEnabled;
    }

    public Product(Integer product_id, String product_name, String product_title, Double product_price, Double product_sale_price, Date product_create_date, Category product_category, Byte product_isEnabled, List<PropertyValue> propertyValueList, List<ProductImage> singleProductImageList, List<ProductImage> detailProductImageList, List<Review> reviewList, List<ProductOrderItem> productOrderItemList,Integer product_sale_count,Integer product_review_count) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_title = product_title;
        this.product_price = product_price;
        this.product_sale_price = product_sale_price;
        this.product_create_date = product_create_date;
        this.product_category = product_category;
        this.product_isEnabled = product_isEnabled;
        this.propertyValueList = propertyValueList;
        this.singleProductImageList = singleProductImageList;
        this.detailProductImageList = detailProductImageList;
        this.reviewList = reviewList;
        this.productOrderItemList = productOrderItemList;
        this.product_sale_count = product_sale_count;
        this.product_review_count = product_review_count;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public Product setProduct_id(Integer product_id) {
        this.product_id = product_id;
        return this;
    }

    public String getProduct_name() {
        return product_name;
    }

    public Product setProduct_name(String product_name) {
        this.product_name = product_name;
        return this;
    }

    public String getProduct_title() {
        return product_title;
    }

    public Product setProduct_title(String product_title) {
        this.product_title = product_title;
        return this;
    }

    public Double getProduct_price() {
        return product_price;
    }

    public Product setProduct_price(Double product_price) {
        this.product_price = product_price;
        return this;
    }

    public Double getProduct_sale_price() {
        return product_sale_price;
    }

    public Product setProduct_sale_price(Double product_sale_price) {
        this.product_sale_price = product_sale_price;
        return this;
    }

    public String getProduct_create_date() {
        if(product_create_date != null){
            SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.UK);
            return time.format(product_create_date);
        }
        return null;
    }

    public Product setProduct_create_date(Date product_create_date) {
        this.product_create_date = product_create_date;
        return this;
    }

    public Integer getProduct_category_id() {
        return product_category_id;
    }

    public Product setProduct_category_id(Integer product_category_id) {
        this.product_category_id = product_category_id;
        return this;
    }

    public Category getProduct_category() {
        return product_category;
    }

    public Product setProduct_category(Category product_category) {
        this.product_category = product_category;
        return this;
    }

    public Byte getProduct_isEnabled() {
        return this.product_isEnabled;
    }

    public Product setProduct_isEnabled(Byte product_isEnabled) {
        this.product_isEnabled = product_isEnabled;
        return this;
    }

    public List<ProductImage> getSingleProductImageList() {
        return singleProductImageList;
    }

    public Product setSingleProductImageList(List<ProductImage> singleProductImageList) {
        this.singleProductImageList = singleProductImageList;
        return this;
    }

    public List<ProductImage> getDetailProductImageList() {
        return detailProductImageList;
    }

    public Product setDetailProductImageList(List<ProductImage> detailProductImageList) {
        this.detailProductImageList = detailProductImageList;
        return this;
    }

    public List<PropertyValue> getPropertyValueList() {
        return propertyValueList;
    }

    public Product setPropertyValueList(List<PropertyValue> propertyValueList) {
        this.propertyValueList = propertyValueList;
        return this;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public Product setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
        return this;
    }

    public List<ProductOrderItem> getProductOrderItemList() {
        return productOrderItemList;
    }

    public Product setProductOrderItemList(List<ProductOrderItem> productOrderItemList) {
        this.productOrderItemList = productOrderItemList;
        return this;
    }

    public Integer getProduct_sale_count() {
        return product_sale_count;
    }

    public Product setProduct_sale_count(Integer product_sale_count) {
        this.product_sale_count = product_sale_count;
        return this;
    }

    public Integer getProduct_review_count() {
        return product_review_count;
    }

    public Product setProduct_review_count(Integer product_review_count) {
        this.product_review_count = product_review_count;
        return this;
    }
}
