package com.uzykj.mall.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class ProductOrder implements Serializable{
	private Integer productOrder_id;
	private String productOrder_code;
	private Address productOrder_address;
	private String productOrder_detail_address;
	private String productOrder_post;
	private String productOrder_receiver;
	private String productOrder_mobile;
	private Date productOrder_pay_date;
	private Date productOrder_delivery_date;
	private Date productOrder_confirm_date;
	private Byte productOrder_status;
	private User productOrder_user;
	private List<ProductOrderItem> productOrderItemList;

	@Override
	public String toString() {
		return "ProductOrder{" + "productOrder_id=" + productOrder_id + ", productOrder_code='" + productOrder_code
				+ '\'' + ", productOrder_address=" + productOrder_address + ", productOrder_detail_address='"
				+ productOrder_detail_address + '\'' + ", productOrder_post='" + productOrder_post + '\''
				+ ", productOrder_receiver='" + productOrder_receiver + '\'' + ", productOrder_mobile='"
				+ productOrder_mobile + '\'' + ", productOrder_pay_date=" + productOrder_pay_date
				+ ", productOrder_delivery_date=" + productOrder_delivery_date + ", productOrder_confirm_date="
				+ productOrder_confirm_date + ", productOrder_status=" + productOrder_status + ", productOrder_user="
				+ productOrder_user + ", productOrderItemList=" + productOrderItemList + '}';
	}

	public ProductOrder() {
	}

	public ProductOrder(Integer productOrder_id, String productOrder_code, Address productOrder_address,
			String productOrder_detail_address, String productOrder_post, String productOrder_receiver,
			String productOrder_mobile, Date productOrder_pay_date, Byte productOrder_status,
			User productOrder_user) {
		this.productOrder_id = productOrder_id;
		this.productOrder_code = productOrder_code;
		this.productOrder_address = productOrder_address;
		this.productOrder_detail_address = productOrder_detail_address;
		this.productOrder_post = productOrder_post;
		this.productOrder_receiver = productOrder_receiver;
		this.productOrder_mobile = productOrder_mobile;
		this.productOrder_pay_date = productOrder_pay_date;
		this.productOrder_status = productOrder_status;
		this.productOrder_user = productOrder_user;
	}

	public ProductOrder(Integer productOrder_id, String productOrder_code, Address productOrder_address,
			String productOrder_detail_address, String productOrder_post, String productOrder_receiver,
			String productOrder_mobile, Date productOrder_pay_date, Date productOrder_delivery_date,
			Date productOrder_confirm_date, Byte productOrder_status, User productOrder_user,
			List<ProductOrderItem> productOrderItemList) {
		this.productOrder_id = productOrder_id;
		this.productOrder_code = productOrder_code;
		this.productOrder_address = productOrder_address;
		this.productOrder_detail_address = productOrder_detail_address;
		this.productOrder_post = productOrder_post;
		this.productOrder_receiver = productOrder_receiver;
		this.productOrder_mobile = productOrder_mobile;
		this.productOrder_pay_date = productOrder_pay_date;
		this.productOrder_delivery_date = productOrder_delivery_date;
		this.productOrder_confirm_date = productOrder_confirm_date;
		this.productOrder_status = productOrder_status;
		this.productOrder_user = productOrder_user;
		this.productOrderItemList = productOrderItemList;
	}

	public Integer getProductOrder_id() {
		return productOrder_id;
	}

	public ProductOrder setProductOrder_id(Integer productOrder_id) {
		this.productOrder_id = productOrder_id;
		return this;
	}

	public String getProductOrder_code() {
		return productOrder_code;
	}

	public ProductOrder setProductOrder_code(String productOrder_code) {
		this.productOrder_code = productOrder_code;
		return this;
	}

	public Address getProductOrder_address() {
		return productOrder_address;
	}

	public ProductOrder setProductOrder_address(Address productOrder_address) {
		this.productOrder_address = productOrder_address;
		return this;
	}

	public String getProductOrder_detail_address() {
		return productOrder_detail_address;
	}

	public ProductOrder setProductOrder_detail_address(String productOrder_detail_address) {
		this.productOrder_detail_address = productOrder_detail_address;
		return this;
	}

	public String getProductOrder_post() {
		return productOrder_post;
	}

	public ProductOrder setProductOrder_post(String productOrder_post) {
		this.productOrder_post = productOrder_post;
		return this;
	}

	public String getProductOrder_receiver() {
		return productOrder_receiver;
	}

	public ProductOrder setProductOrder_receiver(String productOrder_receiver) {
		this.productOrder_receiver = productOrder_receiver;
		return this;
	}

	public String getProductOrder_mobile() {
		return productOrder_mobile;
	}

	public ProductOrder setProductOrder_mobile(String productOrder_mobile) {
		this.productOrder_mobile = productOrder_mobile;
		return this;
	}

	public String getProductOrder_pay_date() {
		if (productOrder_pay_date != null) {
			SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
			return time.format(productOrder_pay_date);
		}
		return null;
	}

	public ProductOrder setProductOrder_pay_date(Date productOrder_pay_date) {
		this.productOrder_pay_date = productOrder_pay_date;
		return this;
	}

	public String getProductOrder_delivery_date() {
		if (productOrder_delivery_date != null) {
			SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
			return time.format(productOrder_delivery_date);
		}
		return null;
	}

	public ProductOrder setProductOrder_delivery_date(Date productOrder_delivery_date) {
		this.productOrder_delivery_date = productOrder_delivery_date;
		return this;
	}

	public String getProductOrder_confirm_date() {
		if (productOrder_confirm_date != null) {
			SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
			return time.format(productOrder_confirm_date);
		}
		return null;
	}

	public ProductOrder setProductOrder_confirm_date(Date productOrder_confirm_date) {
		this.productOrder_confirm_date = productOrder_confirm_date;
		return this;
	}

	public Byte getProductOrder_status() {
		return productOrder_status;
	}

	public ProductOrder setProductOrder_status(Byte productOrder_status) {
		this.productOrder_status = productOrder_status;
		return this;
	}

	public User getProductOrder_user() {
		return productOrder_user;
	}

	public ProductOrder setProductOrder_user(User productOrder_user) {
		this.productOrder_user = productOrder_user;
		return this;
	}

	public List<ProductOrderItem> getProductOrderItemList() {
		return productOrderItemList;
	}

	public ProductOrder setProductOrderItemList(List<ProductOrderItem> productOrderItemList) {
		this.productOrderItemList = productOrderItemList;
		return this;
	}
}
