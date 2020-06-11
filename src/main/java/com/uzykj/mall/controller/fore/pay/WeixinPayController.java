package com.uzykj.mall.controller.fore.pay;

import com.uzykj.mall.controller.BaseController;
import com.uzykj.mall.entity.ProductOrder;
import com.uzykj.mall.service.IWeixinPayService;
import com.uzykj.mall.service.ProductOrderService;
import com.uzykj.mall.util.FileIsExists;
import com.uzykj.mall.util.pay.wx.Product;
import com.uzykj.mall.util.pay.wx.WxpayConfig;
import com.uzykj.mall.util.pay.wx.util.WxpayUtil;
import com.uzykj.mall.util.qiniu.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * 微信支付API
 *
 * @author xu
 * @DateTime 2018-10-27 下午15：02
 */
@Controller
@RequestMapping(value = "/wxpay")
public class WeixinPayController extends BaseController {
    private static final String FUNCNAME = "[weixin pay controller]";
    @Autowired
    private IWeixinPayService weixinPayService;
    @Autowired
    private ProductOrderService orderService;

    @RequestMapping(value = "/pc", method = RequestMethod.POST)
    public void pcPay2(Product product, HttpSession session, HttpServletResponse response) {
        QiniuUtil qiniuUtil = new QiniuUtil();
        String filePath = QiniuUtil.LOCAL_FILE_PATH;
        logger.info("二维码支付(模式二)");
        product.setTotalFee(String.valueOf(Double.parseDouble(product.getTotalFee()) * 100));
        product.setAttach(filePath);
        logger.info("商品属性{" + product.toString() + "}");
        try {
            String message = weixinPayService.weixinPay2(product);
            String imgPath1 = filePath + product.getOutTradeNo() + ".png";
            if (FileIsExists.judeFileExists(new File(imgPath1))) {
                int width = 150;
                int height = 150;
                BufferedImage image = new BufferedImage(width, height, 1);
                Graphics g = image.getGraphics();
                g.setColor(new Color(204, 204, 204));
                g.fillRect(0, 0, width, height);
                logger.info("图片路径为" + imgPath1);
                response.setContentType("image/jpeg");
                OutputStream output = response.getOutputStream();
                ImageIO.write(image, "jpeg", output);
                output.close();
            } else {
                logger.warn("路径下没有此单号：" + product.getOutTradeNo() + " 的图片信息");
            }
        } catch (Exception e) {
            logger.error(FUNCNAME + " pcPay", e);
        }
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public String weixin_notify(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        // 读取参数
        InputStream inputStream = request.getInputStream();
        StringBuffer sb = new StringBuffer();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();

        // 解析xml成map
        Map<String, String> m = new HashMap<String, String>();
        m = WxpayUtil.doXMLParse(sb.toString());

        // 过滤空 设置 TreeMap
        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        @SuppressWarnings("rawtypes")
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = m.get(parameter);

            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packageParams.put(parameter, v);
        }
        // 账号信息
        String key = WxpayConfig.API_KEY; // key
        // 判断签名是否正确
        if (WxpayUtil.isTenpaySign("UTF-8", packageParams, key)) {
            logger.info("微信支付成功回调");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            String resXml = "";
            if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
                // 这里是支付成功
                String orderNo = (String) packageParams.get("out_trade_no");
                String total_fee = (String) packageParams.get("total_fee");
                logger.info("微信订单号{" + orderNo + "}付款成功");
                // 这里 根据实际业务场景 做相应的操作
                // 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                logger.info("交易支付成功");
                ProductOrder byCode = orderService.getByCode(orderNo);
                byCode.setProductOrder_status((byte) 5);
                boolean yn = orderService.update(byCode);
                if (yn) {
                    logger.info("订单状态--支付成功");
                } else {
                    throw new RuntimeException("订单异常");
                }
            } else {
                logger.info("支付失败,错误信息：{" + packageParams.get("err_code") + "}");
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
            response.sendRedirect("/page/index");
            return null;
        } else {
            logger.info("通知签名验证失败");
        }
        return "redirect:/page/index";
    }

}
