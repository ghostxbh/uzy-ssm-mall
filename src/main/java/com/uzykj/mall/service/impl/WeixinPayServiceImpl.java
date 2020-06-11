package com.uzykj.mall.service.impl;

import com.uzykj.mall.service.IWeixinPayService;
import com.uzykj.mall.util.FileIsExists;
import com.uzykj.mall.util.HttpUtil;
import com.uzykj.mall.util.pay.wx.ClientCustomSSL;
import com.uzykj.mall.util.pay.wx.Constants;
import com.uzykj.mall.util.pay.wx.Product;
import com.uzykj.mall.util.pay.wx.WxpayConfig;
import com.uzykj.mall.util.pay.wx.util.WxpayUtil;
import com.uzykj.mall.util.pay.wx.util.ZxingUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import weixin.popular.api.SnsAPI;

import java.io.File;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
@Component
public class WeixinPayServiceImpl implements IWeixinPayService {
    private static final Logger logger = LoggerFactory.getLogger(WeixinPayServiceImpl.class);

    /**
     * 微信支付要求商户订单号保持唯一性（建议根据当前系统时间加随机序列来生成订单号）。
     * 重新发起一笔支付要使用原订单号，避免重复支付；已支付过或已调用关单、撤销的订单号不能重新发起支付。
     * 注意：支付金额和商品描述必须一样，下单后金额或者描述如果有改变也会出现订单号重复。
     */
    @Override
    public String weixinPay2(Product product) {
        logger.info("订单号：{}生成微信支付码", product.getOutTradeNo());
        String message = Constants.SUCCESS;
        try {

            logger.info("图片存放路径{}", product.getAttach());
            String imgPath = product.getAttach() + product.getOutTradeNo() + ".png";
            if (FileIsExists.judeDirExists(new File(product.getAttach()))) {
                logger.info("图片路径{}", imgPath);
            } else {
                throw new RuntimeException("图片路径异常");
            }
            // 账号信息
            String key = WxpayConfig.API_KEY; // key
            String trade_type = "NATIVE";// 交易类型 原生扫码支付
            SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
            WxpayConfig.commonParams(packageParams);
            packageParams.put("product_id", product.getProductId());// 商品ID
            packageParams.put("body", product.getBody());// 商品描述
            packageParams.put("out_trade_no", product.getOutTradeNo());// 商户订单号
            String totalFee = product.getTotalFee();
            totalFee = WxpayUtil.subZeroAndDot(totalFee);
            packageParams.put("total_fee", totalFee);// 总金额
            packageParams.put("spbill_create_ip", product.getSpbillCreateIp());// 发起人IP地址
            packageParams.put("notify_url", WxpayConfig.SM_NOTIFYURL);// 回调地址
            packageParams.put("trade_type", trade_type);// 交易类型
            String sign = WxpayUtil.createSign("UTF-8", packageParams, key);
            packageParams.put("sign", sign);// 签名

            String requestXML = WxpayUtil.getRequestXml(packageParams);
            String resXml = HttpUtil.postData(WxpayConfig.UNIFIED_ORDER_URL, requestXML);
            Map map = WxpayUtil.doXMLParse(resXml);
            String returnCode = (String) map.get("return_code");
            logger.info(returnCode);
            if ("SUCCESS".equals(returnCode)) {
                String resultCode = (String) map.get("result_code");
                if ("SUCCESS".equals(resultCode)) {
                    logger.info("订单号：{}生成微信支付码成功", product.getOutTradeNo());
                    String urlCode = (String) map.get("code_url");
                    WxpayConfig.shorturl(urlCode);//转换为短链接
                    ZxingUtil.getQRCodeImge(urlCode, 256, imgPath);// 生成二维码
                } else {
                    String errCodeDes = (String) map.get("err_code_des");
                    logger.info("订单号：{}生成微信支付码(系统)失败:{}", product.getOutTradeNo(), errCodeDes);
                    message = Constants.FAIL;
                }
            } else {
                String returnMsg = (String) map.get("return_msg");
                logger.info("(订单号：{}生成微信支付码(通信)失败:{}", product.getOutTradeNo(), returnMsg);
                message = Constants.FAIL;
            }
        } catch (Exception e) {
            logger.error("订单号：{}生成微信支付码失败(系统异常))", product.getOutTradeNo(), e);
            message = Constants.FAIL;
        }
        return message;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public String weixinRefund(Product product) {
        logger.info("订单号：{}微信退款", product.getOutTradeNo());
        String message = Constants.SUCCESS;
        try {
            // 账号信息
            String mch_id = WxpayConfig.MCH_ID; // 商业号
            String key = WxpayConfig.API_KEY; // key

            SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
            WxpayConfig.commonParams(packageParams);
            packageParams.put("out_trade_no", product.getOutTradeNo());// 商户订单号
            packageParams.put("out_refund_no", product.getOutTradeNo());//商户退款单号
            String totalFee = product.getTotalFee();
            totalFee = WxpayUtil.subZeroAndDot(totalFee);
            packageParams.put("total_fee", totalFee);// 总金额
            packageParams.put("refund_fee", totalFee);//退款金额
            packageParams.put("op_user_id", mch_id);//操作员帐号, 默认为商户号
            String sign = WxpayUtil.createSign("UTF-8", packageParams, key);
            packageParams.put("sign", sign);// 签名
            String requestXML = WxpayUtil.getRequestXml(packageParams);
            String weixinPost = ClientCustomSSL.doRefund(WxpayConfig.REFUND_URL, requestXML).toString();
            Map map = WxpayUtil.doXMLParse(weixinPost);
            String returnCode = (String) map.get("return_code");
            if ("SUCCESS".equals(returnCode)) {
                String resultCode = (String) map.get("result_code");
                if ("SUCCESS".equals(resultCode)) {
                    logger.info("订单号：{}微信退款成功并删除二维码", product.getOutTradeNo());
                } else {
                    String errCodeDes = (String) map.get("err_code_des");
                    logger.info("订单号：{}微信退款失败:{}", product.getOutTradeNo(), errCodeDes);
                    message = Constants.FAIL;
                }
            } else {
                String returnMsg = (String) map.get("return_msg");
                logger.info("订单号：{}微信退款失败:{}", product.getOutTradeNo(), returnMsg);
                message = Constants.FAIL;
            }
        } catch (Exception e) {
            logger.error("订单号：{}微信支付失败(系统异常)", product.getOutTradeNo(), e);
            message = Constants.FAIL;
        }
        return message;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public String weixinCloseorder(Product product) {
        logger.info("订单号：{}微信关闭订单", product.getOutTradeNo());
        String message = Constants.SUCCESS;
        try {
            String key = WxpayConfig.API_KEY; // key
            SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
            WxpayConfig.commonParams(packageParams);
            packageParams.put("out_trade_no", product.getOutTradeNo());// 商户订单号
            String sign = WxpayUtil.createSign("UTF-8", packageParams, key);
            packageParams.put("sign", sign);// 签名
            String requestXML = WxpayUtil.getRequestXml(packageParams);
            String resXml = HttpUtil.postData(WxpayConfig.CLOSE_ORDER_URL, requestXML);
            Map map = WxpayUtil.doXMLParse(resXml);
            String returnCode = (String) map.get("return_code");
            if ("SUCCESS".equals(returnCode)) {
                String resultCode = (String) map.get("result_code");
                if ("SUCCESS".equals(resultCode)) {
                    logger.info("订单号：{}微信关闭订单成功", product.getOutTradeNo());
                } else {
                    String errCode = (String) map.get("err_code");
                    String errCodeDes = (String) map.get("err_code_des");
                    if ("ORDERNOTEXIST".equals(errCode) || "ORDERCLOSED".equals(errCode)) {//订单不存在或者已经关闭
                        logger.info("订单号：{}微信关闭订单:{}", product.getOutTradeNo(), errCodeDes);
                    } else {
                        logger.info("订单号：{}微信关闭订单失败:{}", product.getOutTradeNo(), errCodeDes);
                        message = Constants.FAIL;
                    }
                }
            } else {
                String returnMsg = (String) map.get("return_msg");
                logger.info("订单号：{}微信关闭订单失败:{}", product.getOutTradeNo(), returnMsg);
                message = Constants.FAIL;
            }
        } catch (Exception e) {
            logger.error("订单号：{}微信关闭订单失败(系统异常)", product.getOutTradeNo(), e);
            message = Constants.FAIL;
        }
        return message;
    }

    /**
     * 商户可以通过该接口下载历史交易清单。比如掉单、系统错误等导致商户侧和微信侧数据不一致，通过对账单核对后可校正支付状态。
     * 注意：
     * 1、微信侧未成功下单的交易不会出现在对账单中。支付成功后撤销的交易会出现在对账单中，跟原支付单订单号一致，bill_type为REVOKED；
     * 2、微信在次日9点启动生成前一天的对账单，建议商户10点后再获取；
     * 3、对账单中涉及金额的字段单位为“元”。
     * <p>
     * 4、对账单接口只能下载三个月以内的账单。
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void saveBill() {
        try {
            String key = WxpayConfig.API_KEY; // key
            //获取两天以前的账单
            //String billDate = DateUtil.getBeforeDayDate("2");
            SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
            WxpayConfig.commonParams(packageParams);//公用部分
            packageParams.put("bill_type", "ALL");//ALL，返回当日所有订单信息，默认值SUCCESS，返回当日成功支付的订单REFUND，返回当日退款订单
            //packageParams.put("tar_type", "GZIP");//压缩账单
            packageParams.put("bill_date", "20161206");//账单日期
            String sign = WxpayUtil.createSign("UTF-8", packageParams, key);
            packageParams.put("sign", sign);// 签名
            String requestXML = WxpayUtil.getRequestXml(packageParams);
            String resXml = HttpUtil.postData(WxpayConfig.DOWNLOAD_BILL_URL, requestXML);
            if (resXml.startsWith("<xml>")) {
                Map map = WxpayUtil.doXMLParse(resXml);
                String returnMsg = (String) map.get("return_msg");
                logger.info("微信查询订单失败:{}", returnMsg);
            } else {
                //入库
            }
        } catch (Exception e) {
            logger.error("微信查询订单异常", e);
        }

    }

    @Override
    public String weixinPayMobile(Product product) {
        String totalFee = product.getTotalFee();
        //redirect_uri 需要在微信支付端添加认证网址
        totalFee = WxpayUtil.subZeroAndDot(totalFee);
        String redirect_uri = WxpayConfig.H5_NOTIFYURL + "weixinMobile/dopay?outTradeNo=" + product.getOutTradeNo() + "&totalFee=" + totalFee;
        //也可以通过state传递参数 redirect_uri 后面加参数未经过验证
        return SnsAPI.connectOauth2Authorize(WxpayConfig.APP_ID, redirect_uri, true, null);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public String weixinPayH5(Product product) {
        logger.info("订单号：{}发起H5支付", product.getOutTradeNo());
        String mweb_url = "";
        try {
            // 账号信息
            String key = WxpayConfig.API_KEY; // key
            String trade_type = "MWEB";//交易类型 H5 支付
            SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
            WxpayConfig.commonParams(packageParams);
            packageParams.put("product_id", product.getProductId());// 商品ID
            packageParams.put("body", product.getBody());// 商品描述
            packageParams.put("out_trade_no", product.getOutTradeNo());// 商户订单号
            String totalFee = product.getTotalFee();
            totalFee = WxpayUtil.subZeroAndDot(totalFee);
            packageParams.put("total_fee", totalFee);// 总金额
            //H5支付要求商户在统一下单接口中上传用户真实ip地址 spbill_create_ip
            packageParams.put("spbill_create_ip", product.getSpbillCreateIp());// 发起人IP地址
            packageParams.put("notify_url", WxpayConfig.H5_NOTIFYURL);// 回调地址
            packageParams.put("trade_type", trade_type);// 交易类型
            //H5支付专用
            JSONObject value = new JSONObject();
            value.put("type", "WAP");
            value.put("wap_url", "http://www.jkj521.cn");////WAP网站URL地址
            value.put("wap_name", "优账云财税");//WAP 网站名
            JSONObject scene_info = new JSONObject();
            scene_info.put("h5_info", value);
            packageParams.put("scene_info", scene_info.toString());

            String sign = WxpayUtil.createSign("UTF-8", packageParams, key);
            packageParams.put("sign", sign);// 签名

            String requestXML = WxpayUtil.getRequestXml(packageParams);
            String resXml = HttpUtil.postData(WxpayConfig.UNIFIED_ORDER_URL, requestXML);
            Map map = WxpayUtil.doXMLParse(resXml);
            String returnCode = (String) map.get("return_code");
            if ("SUCCESS".equals(returnCode)) {
                String resultCode = (String) map.get("result_code");
                if ("SUCCESS".equals(resultCode)) {
                    logger.info("订单号：{}发起H5支付成功", product.getOutTradeNo());
                    mweb_url = (String) map.get("mweb_url");
                } else {
                    String errCodeDes = (String) map.get("err_code_des");
                    logger.info("订单号：{}发起H5支付(系统)失败:{}", product.getOutTradeNo(), errCodeDes);
                }
            } else {
                String returnMsg = (String) map.get("return_msg");
                logger.info("(订单号：{}发起H5支付(通信)失败:{}", product.getOutTradeNo(), returnMsg);
            }
        } catch (Exception e) {
            logger.error("订单号：{}发起H5支付失败(系统异常))", product.getOutTradeNo(), e);
        }
        return mweb_url;
    }

    /**
     * SUCCESS—支付成功
     * REFUND—转入退款
     * NOTPAY—未支付
     * CLOSED—已关闭
     * REVOKED—已撤销（刷卡支付）
     * USERPAYING--用户支付中
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     * 支付状态机请见下单API页面
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void orderquery(Product product) {
        try {
            // 账号信息
            String key = WxpayConfig.API_KEY; // key
            SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
            WxpayConfig.commonParams(packageParams);
            packageParams.put("out_trade_no", product.getOutTradeNo());// 商户订单号
            String sign = WxpayUtil.createSign("UTF-8", packageParams, key);
            packageParams.put("sign", sign);// 签名
            String requestXML = WxpayUtil.getRequestXml(packageParams);
            String resXml = HttpUtil.postData(WxpayConfig.CHECK_ORDER_URL, requestXML);
            Map map = WxpayUtil.doXMLParse(resXml);
            String returnCode = (String) map.get("return_code");
            logger.info(returnCode);
            if ("SUCCESS".equals(returnCode)) {
                String resultCode = (String) map.get("result_code");
                if ("SUCCESS".equals(resultCode)) {
                    String tradeState = (String) map.get("trade_state");
                    logger.info(tradeState);
                } else {
                    String errCodeDes = (String) map.get("err_code_des");
                    logger.info(errCodeDes);
                }
            } else {
                String returnMsg = (String) map.get("return_msg");
                logger.info(returnMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
