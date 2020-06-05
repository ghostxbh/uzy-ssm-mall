package com.uzykj.mall.controller.admin;

import com.uzykj.mall.entity.Admin;
import com.uzykj.mall.entity.OrderGroup;
import com.uzykj.mall.service.ProductOrderService;
import com.uzykj.mall.service.ProductService;
import com.uzykj.mall.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uzykj.mall.controller.BaseController;
import com.uzykj.mall.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 后台管理-主页
 */
@Controller
public class AdminHomeController extends BaseController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private ProductOrderService productOrderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    //转到后台管理-主页
    @RequestMapping(value = "admin", method = RequestMethod.GET)
    public String goToPage(HttpSession session, Map<String, Object> map) throws ParseException {
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "redirect:/admin/login";
        }

        logger.info("获取管理员信息");
        Admin admin = adminService.get(null, Integer.parseInt(adminId.toString()));
        map.put("admin", admin);
        logger.info("获取统计信息");
        Integer productTotal = productService.getTotal(null, new Byte[]{0, 2});
        Integer userTotal = userService.getTotal(null);
        Integer orderTotal = productOrderService.getTotal(null, new Byte[]{3});
        logger.info("获取图表信息");
//        map.put("jsonObject", getChartData(null, null));
        map.put("productTotal", productTotal);
        map.put("userTotal", userTotal);
        map.put("orderTotal", orderTotal);

        logger.info("转到后台管理-主页");
        return "admin/homePage";
    }

    //转到后台管理-主页-ajax
    @RequestMapping(value = "admin/home", method = RequestMethod.GET)
    public String goToPageByAjax(HttpSession session, Map<String, Object> map) throws ParseException {
        logger.info("检查管理员权限");
        Object adminId = checkAdmin(session);
        if (adminId == null) {
            return "admin/include/loginMessage";
        }

        logger.info("获取管理员信息");
        Admin admin = adminService.get(null, Integer.parseInt(adminId.toString()));
        map.put("admin", admin);
        logger.info("获取统计信息");
        Integer productTotal = productService.getTotal(null, new Byte[]{0, 2});
        Integer userTotal = userService.getTotal(null);
//        Integer orderTotal = productOrderService.getTotal(null, new Byte[]{3});
        logger.info("获取图表信息");
//        map.put("jsonObject", getChartData(null, null));
        logger.info("获取图表信息");
//        map.put("jsonObject", getChartData(null, null));
        map.put("productTotal", productTotal);
        map.put("userTotal", userTotal);
//        map.put("orderTotal", orderTotal);
        logger.info("转到后台管理-主页-ajax方式");
        return "admin/homeManagePage";
    }

    //按日期查询图表数据-ajax
    @ResponseBody
    @RequestMapping(value = "admin/home/charts", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String getChartDataByDate(@RequestParam(required = false) String beginDate, @RequestParam(required = false) String endDate) throws ParseException {
        if (beginDate != null && endDate != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return getChartData(simpleDateFormat.parse(beginDate), simpleDateFormat.parse(endDate)).toJSONString();
        } else {
            return getChartData(null, null).toJSONString();
        }
    }

    //获取图表的JSON数据
    private JSONObject getChartData(Date beginDate, Date endDate) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        SimpleDateFormat timeSpecial = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        if (beginDate == null || endDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -7);
            beginDate = time.parse(time.format(cal.getTime()));
            cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            endDate = timeSpecial.parse(time.format(cal.getTime()) + " 23:59:59");
        } else {
            beginDate = time.parse(time.format(beginDate));
            endDate = timeSpecial.parse(time.format(endDate) + " 23:59:59");
        }
        String[] dateStr = new String[7];
        SimpleDateFormat time2 = new SimpleDateFormat("MM/dd", Locale.UK);
        logger.info("获取时间段数组");
        for (int i = 0; i < dateStr.length; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(beginDate);
            cal.add(Calendar.DATE, i);
            dateStr[i] = time2.format(cal.getTime());
        }
        logger.info("获取总交易额订单列表");
        List<OrderGroup> orderGroupList = productOrderService.getTotalByDate(beginDate, endDate);
        logger.info("根据订单状态分类");
        int[] orderTotalArray = new int[7];//总交易订单数组
        int[] orderUnpaidArray = new int[7];//未付款订单数组
        int[] orderNotShippedArray = new int[7];//未发货订单叔祖
        int[] orderUnconfirmedArray = new int[7];//未确认订单数组
        int[] orderSuccessArray = new int[7];//交易成功数组
        for (OrderGroup orderGroup : orderGroupList) {
            int index = 0;
            for (int j = 0; j < dateStr.length; j++) {
                if (dateStr[j].equals(orderGroup.getProductOrder_pay_date())) {
                    index = j;
                }
            }
            switch (orderGroup.getProductOrder_status()) {
                case 0:
                    orderUnpaidArray[index] = orderGroup.getProductOrder_count();
                    break;
                case 1:
                    orderNotShippedArray[index] = orderGroup.getProductOrder_count();
                    break;
                case 2:
                    orderUnconfirmedArray[index] = orderGroup.getProductOrder_count();
                    break;
                case 3:
                    orderSuccessArray[index] = orderGroup.getProductOrder_count();
                    break;
            }
        }
        logger.info("获取总交易订单数组");
        for (int i = 0; i < dateStr.length; i++) {
            orderTotalArray[i] = orderUnpaidArray[i] + orderNotShippedArray[i] + orderUnconfirmedArray[i] + orderSuccessArray[i];
        }
        logger.info("返回结果集map");
        jsonObject.put("orderTotalArray", JSONArray.parseArray(JSON.toJSONString(orderTotalArray)));
        jsonObject.put("orderUnpaidArray", JSONArray.parseArray(JSON.toJSONString(orderUnpaidArray)));
        jsonObject.put("orderNotShippedArray", JSONArray.parseArray(JSON.toJSONString(orderNotShippedArray)));
        jsonObject.put("orderUnconfirmedArray", JSONArray.parseArray(JSON.toJSONString(orderUnconfirmedArray)));
        jsonObject.put("orderSuccessArray", JSONArray.parseArray(JSON.toJSONString(orderSuccessArray)));
        jsonObject.put("dateStr", JSONArray.parseArray(JSON.toJSONString(dateStr)));
        return jsonObject;
    }
}