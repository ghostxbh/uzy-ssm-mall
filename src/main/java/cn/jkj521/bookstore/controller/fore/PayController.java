package cn.jkj521.bookstore.controller.fore;


import cn.jkj521.bookstore.util.AlipayConfig;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/pay")
public class PayController {
	final static Logger log = Logger.getLogger(PayController.class);

	/**
	 *
	 * @Title: AlipayController.java
	 * @Package com.sihai.controller
	 * @Description: 前往支付宝第三方网关进行支付
	 * Copyright: Copyright (c) 2017
	 * Company:FURUIBOKE.SCIENCE.AND.TECHNOLOGY
	 *
	 * @author sihai
	 * @date 2017年8月23日 下午8:50:43
	 * @version V1.0
	 */
	@RequestMapping(value = "/goAlipay", produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String goAlipay(HttpServletRequest request, HttpServletRequest response) throws Exception {

		String orderNum=request.getParameter("orderNum");
		String orderName=request.getParameter("orderName");
		String orderPrice=request.getParameter("orderPrice");
		String returnUrl = "http://localhost:8080/Umall/order/pay";
		//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(returnUrl);
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = orderNum;
		//付款金额，必填
		String total_amount = orderPrice;
		//订单名称，必填
		String subject =orderName;

		// 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
   	String timeout_express = "1c";

		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
				+ "\"total_amount\":\""+ total_amount +"\","
				+ "\"subject\":\""+ subject +"\","
				+ "\"timeout_express\":\""+ timeout_express +"\","
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

		//请求
		String result = alipayClient.pageExecute(alipayRequest).getBody();

		return result;
	}
	
}
