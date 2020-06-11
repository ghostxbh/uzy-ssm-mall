package com.uzykj.mall.controller.fore.pay;


import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.uzykj.mall.controller.BaseController;
import com.uzykj.mall.entity.ProductOrder;
import com.uzykj.mall.entity.User;
import com.uzykj.mall.service.ProductOrderService;
import com.uzykj.mall.util.pay.ali.AlipayConfig;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/alipay")
public class AliPayController extends BaseController {

    @Autowired
    private ProductOrderService orderService;

    @RequestMapping(value = "/pc", method = RequestMethod.POST)
    @ResponseBody
    public String goAlipay(HttpServletRequest request, HttpServletRequest response) {
        String orderNum = request.getParameter("orderNum");
        String orderName = request.getParameter("orderName");
        String orderPrice = request.getParameter("orderPrice");
        // 设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.returnUrl);
        alipayRequest.setNotifyUrl(AlipayConfig.notifyUrl);

        // 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
        String timeout_express = "3h";
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderNum);
        bizContent.put("total_amount", orderPrice);//订单金额:元
        bizContent.put("subject", orderName);//订单标题
        bizContent.put("seller_id", AlipayConfig.appId);//实际收款账号，一般填写商户PID即可
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");//电脑网站支付
        bizContent.put("timeout_express", timeout_express);
        bizContent.put("qr_pay_mode", "2");
        String biz = bizContent.toString().replaceAll("\"", "'");
        alipayRequest.setBizContent(biz);
        logger.info("业务参数:" + alipayRequest.getBizContent());
        String form = "fail";
        try {
            form = AlipayConfig.getAlipayClient().pageExecute(alipayRequest).getBody();
            bizContent.put("from", form);
        } catch (AlipayApiException e) {
            logger.error("支付宝构造表单失败", e);
        }
        return bizContent.toString();
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public String queryOrder(@RequestParam("out_trade_no") String thisNo, @RequestParam("trade_no") String aliNo)
            throws AlipayApiException {

        AlipayTradeQueryRequest alipay_request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(thisNo);
        model.setTradeNo(aliNo);
        alipay_request.setBizModel(model);

        AlipayClient alipayClient = AlipayConfig.getAlipayClient();
        AlipayTradeQueryResponse alipay_response = alipayClient.execute(alipay_request);
        return alipay_response.getBody();
    }

    @RequestMapping(value = "/close", method = RequestMethod.GET, produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String closeOrder(@RequestParam("out_trade_no") String thisNo, @RequestParam("trade_no") String aliNo)
            throws AlipayApiException {

        AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + thisNo + "\"," + "\"trade_no\":\"" + aliNo + "\"}");
        AlipayClient alipayClient = AlipayConfig.getAlipayClient();
        AlipayTradeCloseResponse execute = alipayClient.execute(alipayRequest);
        return execute.getBody();
    }

    @RequestMapping(value = "/refund", method = RequestMethod.POST, produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String closeOrder(@RequestParam("out_trade_no") String thisNo, @RequestParam("trade_no") String aliNo,
                             @RequestParam("refund_amount") Double aliPrice, @RequestParam("refund_reason") String aliReason,
                             @RequestParam("out_request_no") String aliRequestNo) throws AlipayApiException {
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + thisNo + "\"," + "\"trade_no\":\"" + aliNo + "\","
                + "\"refund_amount\":\"" + aliPrice + "\"," + "\"refund_reason\":\"" + aliReason + "\","
                + "\"out_request_no\":\"" + aliRequestNo + "\"}");
        AlipayClient alipayClient = AlipayConfig.getAlipayClient();
        AlipayTradeRefundResponse execute = alipayClient.execute(alipayRequest);
        logger.info(execute.getBody().toString());
        return execute.getBody();
    }

    @RequestMapping(value = "/notify_url", method = RequestMethod.POST, produces = "text/html; charset=UTF-8")
    public void aliNotify(Map<String, Object> map, HttpSession session, HttpServletRequest request,
                          HttpServletResponse response) throws Exception {
        String message = "success";
        logger.info("获取用户信息");
        User user = (User) session.getAttribute("user");
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipayPublicKey, AlipayConfig.charset,
                AlipayConfig.signType); // 调用SDK验证签名

        if (signVerified) {// 验证成功
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

            logger.info("------验证订单支付信息------");
            if (trade_status.equals("TRADE_FINISHED")) {
                logger.info("支付失败");
                message = "failed";
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                logger.info("支付成功");
                logger.info("查询订单是否存在");
				/*ProductOrder order = productOrderService.getByCode(out_trade_no);
				order.setProductOrderItemList(
						productOrderItemService.getListByOrderId(order.getProductOrder_id(), null));

				double orderTotalPrice = 0.00;
				if (order.getProductOrderItemList().size() == 1) {
					logger.info("获取单订单项的产品信息");
					ProductOrderItem productOrderItem = order.getProductOrderItemList().get(0);
					orderTotalPrice = productOrderItem.getProductOrderItem_price();
				} else {
					for (ProductOrderItem productOrderItem : order.getProductOrderItemList()) {
						orderTotalPrice += productOrderItem.getProductOrderItem_price();
					}
				}
				logger.info("订单总金额为：{}元" + orderTotalPrice);
				map.put("status", true);
				map.put("productOrder", order);
				map.put("orderTotalPrice", orderTotalPrice);
				Byte i = 1;
				order.setProductOrder_status(i);
				boolean update = productOrderService.update(order);
				if (update) {
					logger.info("交易成功");
				} else {
					message = "failed";
					throw new RuntimeException("交易失败");
				}*/
            }
            logger.info("success");
            logger.info("商户订单号" + out_trade_no);
            logger.info("支付宝交易号" + trade_no);
        } else {// 验证失败
            response.getWriter().print("failure");
            logger.info("验证失败！");
        }
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(message.getBytes());
        out.flush();
        out.close();
    }

    @RequestMapping(value = "/return_url", method = RequestMethod.GET)
    public String aliReturn(Map<String, Object> map, HttpSession session, HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        @SuppressWarnings("unchecked")
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipayPublicKey, AlipayConfig.charset,
                AlipayConfig.signType); // 调用SDK验证签名

        if (signVerified) {
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            // 付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
            /*ProductOrder order = productOrderService.getByCode(out_trade_no);*/

            // 支付宝订单状态查询
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.aliGateway, AlipayConfig.appId,
                    AlipayConfig.merchantPrivateKey, "json", AlipayConfig.charset, AlipayConfig.alipayPublicKey,
                    AlipayConfig.signType);
            AlipayTradeQueryRequest query = new AlipayTradeQueryRequest();
            query.setBizContent(
                    "{" + "\"out_trade_no\":\"" + out_trade_no + "\"," + "\"trade_no\":\"" + trade_no + "\"}");
            AlipayTradeQueryResponse queryResponse = alipayClient.execute(query);

            if (queryResponse.isSuccess()) {
                String status = queryResponse.getTradeStatus().toString();
                switch (status) {
                    case "WAIT_BUYER_PAY":
                        logger.info("交易创建，等待买家付款");
                        return "redirect:/page/index";
                    case "TRADE_CLOSED":
                        logger.info("未付款交易超时关闭，或支付完成后全额退款");
                        return "redirect:/page/index";
                    case "TRADE_SUCCESS":
                        logger.info("交易支付成功");
                        ProductOrder byCode = orderService.getByCode(out_trade_no);
                        byCode.setProductOrder_status((byte) 5);
                        boolean yn = orderService.update(byCode);
                        if (yn) {
                            logger.info("订单状态--支付成功");
                            return "redirect:/filecenter/index";
                        } else {
                            throw new RuntimeException("订单异常");
                        }
                    case "TRADE_FINISHED":
                        logger.info("交易结束，不可退款");
                        return "redirect:/page/index";
                }
            } else {
                throw new RuntimeException("支付查询异常");
            }
            logger.info(
                    "trade_no:" + trade_no + "<br/>out_trade_no:" + out_trade_no + "<br/>total_amount:" + total_amount);
        } else {
            request.setAttribute("status", false);
            logger.info("验签失败");
            throw new RuntimeException("验签失败");
        }
        return "redirect:/page/index";
    }
}
