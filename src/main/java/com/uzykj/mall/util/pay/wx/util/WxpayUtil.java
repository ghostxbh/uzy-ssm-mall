package com.uzykj.mall.util.pay.wx.util;

import com.uzykj.mall.util.Md5Util;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author ghostxbh
 * @date 2020/6/10
 * @description
 */
public class WxpayUtil {

    /**
     * 取出一个指定长度大小的随机正整数
     *
     * @return int
     */
    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /**
     * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    public static boolean isTenpaySign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<Object, Object>> es = packageParams.entrySet();
        for (Map.Entry<Object, Object> e : es) {
            String k = (String) ((Map.Entry<?, ?>) e).getKey();
            String v = (String) ((Map.Entry<?, ?>) e).getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k).append("=").append(v).append("&");
            }
        }
        sb.append("key=").append(API_KEY);
        //算出摘要
        String mysign = Md5Util.MD5Encode(sb.toString(), characterEncoding).toLowerCase();
        String tenpaySign = ((String) packageParams.get("sign")).toLowerCase();
        return tenpaySign.equals(mysign);
    }

    /**
     * sign签名
     *
     * @return String
     */
    public static String createSign(String characterEncoding, SortedMap<Object, Object> packageParams, String api_key) {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<Object, Object>> es = packageParams.entrySet();
        for (Map.Entry<Object, Object> e : es) {
            String k = (String) ((Map.Entry<?, ?>) e).getKey();
            String v = (String) ((Map.Entry<?, ?>) e).getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k).append("=").append(v).append("&");
            }
        }
        sb.append("key=").append(api_key);
        return Md5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
    }

    /**
     * 将请求参数转换为xml格式的string
     *
     * @return String
     */
    public static String getRequestXml(SortedMap<Object, Object> parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        Set<Map.Entry<Object, Object>> es = parameters.entrySet();
        for (Map.Entry<Object, Object> e : es) {
            String k = (String) ((Map.Entry<?, ?>) e).getKey();
            String v = (String) ((Map.Entry<?, ?>) e).getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
                sb.append("<").append(k).append(">").append("<![CDATA[").append(v).append("]]></").append(k).append(">");
            } else {
                sb.append("<").append(k).append(">").append(v).append("</").append(k).append(">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     *
     * @param strxml
     * @return Map
     */
    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if ("".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();

        InputStream in = new ByteArrayInputStream(strxml.getBytes(StandardCharsets.UTF_8));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        for (Object o : list) {
            Element e = (Element) o;
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }
        // 关闭流
        in.close();
        return m;
    }

    /**
     * 获取子结点的xml
     *
     * @param children
     * @return String
     */
    public static String getChildrenText(List children) {
        StringBuilder sb = new StringBuilder();
        if (!children.isEmpty()) {
            for (Object child : children) {
                Element e = (Element) child;
                String name = e.getName();
                //获取文本内容
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<").append(name).append(">");
                if (!list.isEmpty()) {
                    //如果子节点不为空则递归调用 getChildrenText 方法获取其子节点的文本内容，并将其拼接到当前节点的字符串中。
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</").append(name).append(">");
            }
        }
        return sb.toString();
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");// 去掉多余的0
            s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
        }
        return s;
    }
}
