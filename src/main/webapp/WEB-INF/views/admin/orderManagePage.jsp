<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <script>
        //检索数据集
        var dataList = {
            "productOrder_code": null,
            "productOrder_post": null,
            "productOrder_status_array": null,
            "orderBy": null,
            "isDesc": true
        };
        $(function () {
            /******
             * event
             ******/
            //点击查询按钮时
            $("#btn_productOrder_submit").click(function () {
                var productOrder_code = $.trim($("#input_productOrder_code").val());
                var productOrder_post = $.trim($("#input_productOrder_post").val());
                //订单状态数组
                var productOrder_status_array = [];
                $(".radio_productOrder_status:checked").each(function () {
                    productOrder_status_array.push($(this).val());
                });
                //校验数据合法性
                if (isNaN(productOrder_code)) {
                    styleUtil.errorShow($('#text_productOrder_msg'), "订单号输入格式有误！");
                    return;
                } else if (isNaN(productOrder_post)) {
                    styleUtil.errorShow($('#text_productOrder_msg'), "邮政编码输入格式有误！");
                    return;
                }
                //封装数据
                dataList.productOrder_code = productOrder_code.toString();
                dataList.productOrder_post = productOrder_post.toString();
                dataList.productOrder_status_array = productOrder_status_array;

                getData($(this), "admin/order/0/10", dataList);
            });
            //点击刷新按钮时
            $("#btn_productOrder_refresh").click(function () {
                //清除数据
                dataList.productOrder_code = null;
                dataList.productOrder_post = null;
                dataList.productOrder_status_array = null;
                dataList.orderBy = null;
                dataList.isDesc = true;
                //获取数据
                getData($(this), "admin/order/0/10", null);
                //清除排序样式
                var table = $("#table_productOrder_list");
                table.find("span.orderByDesc,span.orderByAsc").css("opacity", "0");
                table.find("th.data_info").attr("data-sort", "asc");
            });
            //点击th排序时
            $("th.data_info").click(function () {
                var table = $("#table_productOrder_list");
                if (table.find(">tbody>tr").length <= 1) {
                    return;
                }
                //获取排序字段
                dataList.orderBy = $(this).attr("data-name");
                //是否倒序排序
                dataList.isDesc = $(this).attr("data-sort") === "asc";

                getData($(this), "admin/order/0/10", dataList);
                //设置排序
                table.find("span.orderByDesc,span.orderByAsc").css("opacity", "0");
                if (dataList.isDesc) {
                    $(this).attr("data-sort", "desc").children(".orderByAsc.orderBySelect").removeClass("orderBySelect").css("opacity", "1");
                    $(this).children(".orderByDesc").addClass("orderBySelect").css("opacity", "1");
                } else {
                    $(this).attr("data-sort", "asc").children(".orderByDesc.orderBySelect").removeClass("orderBySelect").css("opacity", "1");
                    $(this).children(".orderByAsc").addClass("orderBySelect").css("opacity", "1");
                }
            });
            //点击table中的数据时
            $("#table_productOrder_list").find(">tbody>tr").click(function () {
                trDataStyle($(this));
            });
        });

        //获取订单数据
        function getData(object, url, dataObject) {
            var table = $("#table_productOrder_list");
            var tbody = table.children("tbody").first();
            $.ajax({
                url: url,
                type: "get",
                data: dataObject,
                traditional: true,
                success: function (data) {
                    //清空原有数据
                    tbody.empty();
                    //设置样式
                    $(".loader").css("display", "none");
                    object.attr("disabled", false);
                    //显示订单统计数据
                    $("#productOrder_count_data").text(data.productOrderCount);
                    if (data.productOrderList.length > 0) {
                        for (var i = 0; i < data.productOrderList.length; i++) {
                            var productOrderStatusClass;
                            var productOrderStatusTitle;
                            var productOrderStatus;
                            switch (data.productOrderList[i].productOrder_status) {
                                case 0:
                                    productOrderStatusClass = "td_await";
                                    productOrderStatusTitle = "等待买家付款";
                                    productOrderStatus = "等待买家付款";
                                    break;
                                case 1:
                                    productOrderStatusClass = "td_warn";
                                    productOrderStatusTitle = "买家已付款，等待卖家发货";
                                    productOrderStatus = "等待卖家发货";
                                    break;
                                case 2:
                                    productOrderStatusClass = "td_wait";
                                    productOrderStatusTitle = "卖家已发货，等待买家确认";
                                    productOrderStatus = "等待买家确认";
                                    break;
                                case 3:
                                    productOrderStatusClass = "td_success";
                                    productOrderStatusTitle = "交易成功";
                                    productOrderStatus = "交易成功";
                                    break;
                                default:
                                    productOrderStatusClass = "td_error";
                                    productOrderStatusTitle = "交易关闭";
                                    productOrderStatus = "交易关闭";
                                    break;
                            }
                            var productOrder_id = data.productOrderList[i].productOrder_id;
                            var productOrder_code = data.productOrderList[i].productOrder_code;
                            var productOrder_post = data.productOrderList[i].productOrder_post;
                            var productOrder_receiver = data.productOrderList[i].productOrder_receiver;
                            var productOrder_mobile = data.productOrderList[i].productOrder_mobile;
                            var productOrder_userMessage = data.productOrderList[i].productOrder_userMessage;
                            //显示用户数据
                            tbody.append("<tr><td><input type='checkbox' class='cbx_select' value='" + productOrder_id + "' id='cbx_productOrder_select_" + productOrder_id + "'><label for='cbx_productOrder_select_" + productOrder_id + "'></label></td><td title='" + productOrder_code + "'>" + productOrder_code + "</td><td title='" + productOrder_post + "'>" + productOrder_post + "</td><td title='" + productOrder_receiver + "'>" + productOrder_receiver + "</td><td title='" + productOrder_mobile + "'>" + productOrder_mobile + "</td><td><span class='" + productOrderStatusClass + "' title= '" + productOrderStatusTitle + "'>" + productOrderStatus + "</span></td><td><span class='td_special' title='查看订单详情'><a href='javascript:void(0)' onclick='getChildPage(this)'>详情</a></span></td><td hidden class='order_id'>" + productOrder_id + "</td></tr>");
                        }
                        //绑定事件
                        tbody.children("tr").click(function () {
                            trDataStyle($(this));
                        });
                        //分页
                        var pageUtil = {
                            index: data.pageUtil.index,
                            count: data.pageUtil.count,
                            total: data.pageUtil.total,
                            totalPage: data.totalPage
                        };
                        createPageDiv($(".loader"), pageUtil);
                    }
                },
                beforeSend: function () {
                    $(".loader").css("display", "block");
                    object.attr("disabled", true);
                },
                error: function () {

                }
            });
        }

        //获取订单子界面
        function getChildPage(obj) {
            //设置样式
            $("#div_home_title").children("span").text("订单详情");
            document.title = "柚子云购 - 订单详情";
            //ajax请求页面
            ajaxUtil.getPage("order/" + $(obj).parents("tr").find(".order_id").text(), null, true);
        }

        //获取页码数据
        function getPage(index) {
            getData($(this), "admin/order/" + index + "/10", dataList);
        }

        //删除订单
        function deleteOrder() {
            var arr = new Array();
            $("#tbodyId input[type='checkbox']:checked").each(function (index, item) {
                arr.push($(this).val());
            });
            console.log(arr);
            if (arr === undefined || arr.length == 0) {
                $(".msg1").stop(true, true).animate({
                    opacity: 1
                }, 550, function () {
                    $(".msg1").animate({
                        opacity: 0
                    }, 1500);
                });
            } else {
                if (window.confirm("确认删除？")) {
                    $.ajax({
                        url: "admin/order/delete/" + arr,
                        type: "get",
                        date: {"array": arr},
                        traditional: true,
                        success: function (data) {
                            if (data.success) {
                                $(".msg").stop(true, true).animate({
                                    opacity: 1
                                }, 550, function () {
                                    $(".msg").animate({
                                        opacity: 0
                                    }, 1500);
                                });
                            }
                        }
                    })
                }
            }
        }
    </script>
    <style rel="stylesheet">
        #text_productOrder_msg {
            margin-left: 10px;
        }

        .msg {
            opacity: 0;
            position: absolute;
            top: 150px;
            left: 0;
            right: 0;
            bottom: 0;
            margin: auto;
            width: 230px;
            height: 70px;
            line-height: 70px;
            color: white;
            border-radius: 5px;
            text-align: center;
            background-color: rgba(0, 0, 0, 0.75);
            font-size: 16px;
            -moz-user-select: none;
            -webkit-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }

        .msg1 {
            opacity: 0;
            position: absolute;
            top: 150px;
            left: 0;
            right: 0;
            bottom: 0;
            margin: auto;
            width: 230px;
            height: 70px;
            line-height: 70px;
            color: white;
            border-radius: 5px;
            text-align: center;
            background-color: rgba(0, 0, 0, 0.75);
            font-size: 16px;
            -moz-user-select: none;
            -webkit-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }
    </style>
</head>
<body>
<div class="frm_div text_info">
    <div class="frm_group">
        <label class="frm_label" id="lbl_productOrder_code" for="input_productOrder_code">订单号</label>
        <input class="frm_input" id="input_productOrder_code" type="text" maxlength="20"/>
        <label class="frm_label" id="lbl_productOrder_post" for="input_productOrder_post">邮政编码</label>
        <input class="frm_input" id="input_productOrder_post" type="text" maxlength="6"/>
        <input class="frm_btn" id="btn_productOrder_submit" type="button" value="查询"/>
        <input class="frm_btn frm_clear" id="btn_clear" type="button" value="重置"/>
    </div>
    <div class="frm_group">
        <label class="frm_label" id="lbl_productOrder_status" for="checkbox_productOrder_status_waitPay">订单状态</label>
        <input class="frm_radio radio_productOrder_status" id="checkbox_productOrder_status_waitPay"
               name="checkbox_productOrder_status" type="checkbox" value="0" checked>
        <label class="frm_label" id="lbl_productOrder_status_waitPay" for="checkbox_productOrder_status_waitPay"
               title="等待买家付款">待付款</label>
        <input class="frm_radio radio_productOrder_status" id="checkbox_productOrder_status_waitDelivery"
               name="checkbox_productOrder_status" type="checkbox" value="1" checked>
        <label class="frm_label" id="lbl_productOrder_status_waitDelivery"
               for="checkbox_productOrder_status_waitDelivery" title="买家已付款，等待卖家发货">待发货</label>
        <input class="frm_radio radio_productOrder_status" id="checkbox_productOrder_status_waitConfirm"
               name="checkbox_productOrder_status" type="checkbox" value="2" checked>
        <label class="frm_label" id="lbl_productOrder_status_waitConfirm" for="checkbox_productOrder_status_waitConfirm"
               title="卖家已发货，等待买家确认">待确认</label>
        <input class="frm_radio radio_productOrder_status" id="checkbox_productOrder_status_success"
               name="checkbox_productOrder_status" type="checkbox" value="3" checked>
        <label class="frm_label" id="lbl_productOrder_status_success" for="checkbox_productOrder_status_success"
               title="交易成功">交易成功</label>
        <input class="frm_radio radio_productOrder_status" id="checkbox_productOrder_status_close"
               name="checkbox_productOrder_status" type="checkbox" value="4" checked>
        <label class="frm_label" id="lbl_productOrder_status_close" for="checkbox_productOrder_status_close"
               title="交易关闭">交易关闭</label>
        <span class="frm_error_msg" id="text_productOrder_msg"></span>
    </div>
    <div class="frm_group_last">
        <%--<input class="frm_btn frm_add" id="btn_product_add" type="button" value="添加一件产品" onclick="getChildPage(null)"/>--%>
        <input class="frm_btn frm_refresh" id="btn_productOrder_refresh" type="button" value="刷新订单列表"/>
        <input class="frm_btn frm_danger" id="btn_product_delete" type="button" value="删除选中订单" onclick="deleteOrder()"/>
    </div>
</div>
<div class="data_count_div text_info">
    <svg class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2522" width="16"
         height="16">
        <path d="M401.976676 735.74897c-88.721671 0-172.124196-34.635845-234.843656-97.526197-62.724577-62.86784-97.271394-146.453537-97.271394-235.358379s34.546817-172.490539 97.276511-235.361449c62.715367-62.887282 146.117892-97.522104 234.838539-97.522104 88.719624 0 172.135452 34.633798 234.881518 97.522104 62.704111 62.875003 97.235578 146.4607 97.235578 235.361449 0 88.901773-34.530444 172.487469-97.231485 235.358379C574.112128 701.116195 490.6963 735.74897 401.976676 735.74897zM401.976676 121.204479c-75.012438 0-145.533584 29.290093-198.572568 82.474386-109.585861 109.834524-109.585861 288.539602-0.004093 398.36901 53.043077 53.188386 123.564223 82.47848 198.577684 82.47848 75.015507 0 145.553027-29.291117 198.620663-82.47848C710.126918 492.220514 710.126918 313.511343 600.593246 203.678866 547.530726 150.496619 476.992183 121.204479 401.976676 121.204479z"
              p-id="2523" fill="#FF7874">
        </path>
        <path d="M932.538427 958.228017c-6.565533 0-13.129019-2.508123-18.132986-7.52437L606.670661 642.206504c-9.989515-10.014074-9.969049-26.231431 0.045025-36.220946s26.230408-9.969049 36.220946 0.045025l307.73478 308.497143c9.989515 10.014074 9.969049 26.231431-0.045025 36.220946C945.627537 955.735244 939.081447 958.228017 932.538427 958.228017z"
              p-id="2524" fill="#FF7874">
        </path>
    </svg>
    <span class="data_count_title">查看合计</span>
    <span>订单总数:</span>
    <span class="data_count_value" id="productOrder_count_data">${requestScope.productOrderCount}</span>
    <span class="data_count_unit">个</span>
</div>
<div class="table_normal_div">
    <table class="table_normal" id="table_productOrder_list">
        <thead class="text_info">
        <tr>
            <th><input type="checkbox" class="cbx_select" id="cbx_select_all"><label for="cbx_select_all"></label></th>
            <th class="data_info" data-sort="asc" data-name="productOrder_code">
                <span>订单号</span>
                <span class="orderByDesc"></span>
                <span class="orderByAsc orderBySelect"></span>
            </th>
            <th class="data_info" data-sort="asc" data-name="productOrder_post">
                <span>邮政编码</span>
                <span class="orderByDesc"></span>
                <span class="orderByAsc orderBySelect"></span>
            </th>
            <th>收货人</th>
            <th>联系方式</th>
            <th class="data_info" data-sort="asc" data-name="productOrder_status">
                <span>订单状态</span>
                <span class="orderByDesc"></span>
                <span class="orderByAsc orderBySelect"></span>
            </th>
            <th>操作</th>
            <th hidden>订单ID</th>
        </tr>
        </thead>
        <tbody id="tbodyId">
        <c:forEach items="${requestScope.productOrderList}" var="productOrder">
            <tr>
                <td><input type="checkbox" class="cbx_select" value="${productOrder.productOrder_id}"
                           id="cbx_productOrder_select_${productOrder.productOrder_id}"><label
                        for="cbx_productOrder_select_${productOrder.productOrder_id}"></label></td>
                <td title="${productOrder.productOrder_code}">${productOrder.productOrder_code}</td>
                <td title="${productOrder.productOrder_post}">${productOrder.productOrder_post}</td>
                <td title="${productOrder.productOrder_receiver}">${productOrder.productOrder_receiver}</td>
                <td title="${productOrder.productOrder_mobile}">${productOrder.productOrder_mobile}</td>
                <td>
                    <c:choose>
                        <c:when test="${productOrder.productOrder_status==0}">
                            <span class="td_await" title="等待买家付款">等待买家付款</span>
                        </c:when>
                        <c:when test="${productOrder.productOrder_status==1}">
                            <span class="td_warn" title="买家已付款，等待卖家发货">等待卖家发货</span>
                        </c:when>
                        <c:when test="${productOrder.productOrder_status==2}">
                            <span class="td_wait" title="卖家已发货，等待买家确认">等待买家确认</span>
                        </c:when>
                        <c:when test="${productOrder.productOrder_status==3}">
                            <span class="td_success" title="交易成功">交易成功</span>
                        </c:when>
                        <c:otherwise><span class="td_error" title="交易关闭">交易关闭</span></c:otherwise>
                    </c:choose>
                </td>
                <td><span class="td_special" title="查看订单详情"><a href="javascript:void(0)"
                                                               onclick="getChildPage(this)">详情</a></span>
                </td>
                <td hidden class="order_id">${productOrder.productOrder_id}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <%@ include file="include/page.jsp" %>
    <div class="loader"></div>
</div>
<div class="msg">
    <span>删除成功</span>
</div>
<div class="msg1">
    <span>无效删除</span>
</div>
</body>
</html>
