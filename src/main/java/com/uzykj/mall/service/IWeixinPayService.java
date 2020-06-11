package com.uzykj.mall.service;


import com.uzykj.mall.util.pay.wx.Product;

public interface IWeixinPayService {
    /**
     * 微信支付下单(模式二)
     * 扫码支付 还有模式一 适合固定商品ID
     *
     * @param product
     * @return String
     * @Author xu
     * @Date 2018-10-27
     */
    String weixinPay2(Product product);

    /**
     * 微信支付退款
     *
     * @param product
     * @return String
     * @Author xu
     * @Date 2018-10-27
     */
    String weixinRefund(Product product);

    /**
     * 关闭订单
     *
     * @param product
     * @return String
     * @Author xu
     * @Date 2018-10-27
     */
    String weixinCloseorder(Product product);

    /**
     * 下载微信账单
     *
     * @Author xu
     * @Date 2017年7月31日
     */
    void saveBill();

    /**
     * 微信公众号支付返回一个url地址
     *
     * @param product
     * @return String
     * @Author xu
     * @Date 2018-10-27
     */
    String weixinPayMobile(Product product);

    /**
     * H5支付 唤醒 微信APP 进行支付
     * 申请入口：登录商户平台-->产品中心-->我的产品-->支付产品-->H5支付
     *
     * @param product
     * @return String
     * @Author xu
     * @Date 2018-10-27
     */
    String weixinPayH5(Product product);

    /**
     * 查询订单
     *
     * @param product
     */
    void orderquery(Product product);
}
