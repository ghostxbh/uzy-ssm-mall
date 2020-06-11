package com.uzykj.mall.util.pay.wx;

import java.io.Serializable;

/**
 * 产品订单信息 创建者 科帮网 创建时间 2017年7月27日
 */
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	private String productId;// 商品ID
	private String subject;// 订单名称
	private String body;// 商品描述
	private String totalFee;// 总金额(单位是分)
	private String outTradeNo;// 订单号(唯一)
	private String spbillCreateIp;// 发起人IP地址
	private String attach;// 附件数据主要用于商户携带订单的自定义数据
	private Short payType;// 支付类型(1:支付宝 2:微信 3:银联)
	private Short payWay;// 支付方式 (1：PC,平板 2：手机)
	private String frontUrl;// 前台回调地址 非扫码支付使用
	private String return_url;
	private String notify_url;

	public Product() {
		super();
	}

	public Product(String productId, String subject, String body, String totalFee, String outTradeNo,
                   String spbillCreateIp, String attach, Short payType, Short payWay, String frontUrl, String return_url,
                   String notify_url) {
		super();
		this.productId = productId;
		this.subject = subject;
		this.body = body;
		this.totalFee = totalFee;
		this.outTradeNo = outTradeNo;
		this.spbillCreateIp = spbillCreateIp;
		this.attach = attach;
		this.payType = payType;
		this.payWay = payWay;
		this.frontUrl = frontUrl;
		this.notify_url = notify_url;
		this.return_url = return_url;
	}

	public String getProductId() {
		return productId;
	}

	public Product setProductId(String productId) {
		this.productId = productId;
		return this;
	}

	public String getSubject() {
		return subject;
	}

	public Product setSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public String getBody() {
		return body;
	}

	public Product setBody(String body) {
		this.body = body;
		return this;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public Product setTotalFee(String totalFee) {
		this.totalFee = totalFee;
		return this;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public Product setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
		return this;
	}

	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}

	public Product setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
		return this;
	}

	public String getAttach() {
		return attach;
	}

	public Product setAttach(String attach) {
		this.attach = attach;
		return this;
	}

	public Short getPayType() {
		return payType;
	}

	public Product setPayType(Short payType) {
		this.payType = payType;
		return this;
	}

	public Short getPayWay() {
		return payWay;
	}

	public Product setPayWay(Short payWay) {
		this.payWay = payWay;
		return this;
	}

	public String getFrontUrl() {
		return frontUrl;
	}

	public Product setFrontUrl(String frontUrl) {
		this.frontUrl = frontUrl;
		return this;
	}

	public String getReturn_url() {
		return return_url;
	}

	public Product setReturn_url(String return_url) {
		this.return_url = return_url;
		return this;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public Product setNotify_url(String notify_url) {
		this.notify_url = notify_url;
		return this;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", subject=" + subject + ", body=" + body + ", totalFee=" + totalFee
				+ ", outTradeNo=" + outTradeNo + ", spbillCreateIp=" + spbillCreateIp + ", attach=" + attach
				+ ", payType=" + payType + ", payWay=" + payWay + ", frontUrl=" + frontUrl + ", return_url="
				+ return_url + ", notify_url=" + notify_url + "]";
	}
	
}
