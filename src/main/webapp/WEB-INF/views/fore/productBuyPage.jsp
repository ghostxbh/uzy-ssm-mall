<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="include/header.jsp" %>
<head>
    <script src="${pageContext.request.contextPath}/static/js/fore/fore_productBuy.js"></script>
    <link href="${pageContext.request.contextPath}/static/css/fore/fore_productBuyPage.css" rel="stylesheet"/>
    <title>确认订单</title>
    <script>
        $(function () {
            $("span.address_province").text($("#select_order_address_province").find("option:selected").text());
            $("span.address_city").text($("#select_order_address_city").find("option:selected").text());
            $("span.address_district").text($("#select_order_address_district").find("option:selected").text());
        })
    </script>
</head>
<body>
<nav>
    <%@ include file="include/navigator.jsp" %>
</nav>
<div class="headerLayout">
    <div class="headerContext">
        <div class="mallLogo">
            <span class="mlogo">
                <a href="${pageContext.request.contextPath}">
                    <s></s>
                </a>
            </span>
        </div>
        <ol class="header-extra">
            <li class="step-done">
                <div class="step-name">拍下商品</div>
                <div class="step-no_first"></div>
            </li>
            <li class="step-no">
                <div class="step-name">付款到支付宝</div>
                <div class="step-no">2</div>
            </li>
            <li class="step-no">
                <div class="step-name">确认收货</div>
                <div class="step-no">3</div>
            </li>
            <li class="step-no">
                <div class="step-name">评价</div>
                <div class="step-no_last">4</div>
            </li>
        </ol>
    </div>
</div>
<div class="content">
    <div class="order_address">
        <h2>输入收货地址</h2>
        <label for="select_order_address_province">所在地区</label><span class="mustValue">*</span>
        <select class="selectpicker" id="select_order_address_province" data-size="8" data-live-search="true">
            <c:forEach items="${requestScope.addressList}" var="address" varStatus="i">
                <option value="${address.address_areaId}"
                        <c:if test="${requestScope.addressId==address.address_areaId}">selected</c:if>>${address.address_name}</option>
            </c:forEach>
        </select>
        <select class="selectpicker" id="select_order_address_city" data-size="8" data-live-search="true">
            <c:forEach items="${requestScope.cityList}" var="address" varStatus="i">
                <option value="${address.address_areaId}"
                        <c:if test="${requestScope.cityAddressId==address.address_areaId}">selected</c:if>>${address.address_name}</option>
            </c:forEach>
        </select>
        <select class="selectpicker" id="select_order_address_district" data-size="8" data-live-search="true">
            <c:forEach items="${requestScope.districtList}" var="address" varStatus="i">
                <option value="${address.address_areaId}"
                        <c:if test="${requestScope.districtAddressId==address.address_areaId}">selected</c:if>>${address.address_name}</option>
            </c:forEach>
        </select>
        <div class="br"></div>
        <label for="textarea_details_address" id="label_details_address">详细地址</label><span class="mustValue">*</span>
        <textarea id="textarea_details_address">${requestScope.detailsAddress}</textarea>
        <div class="br"></div>
        <label for="input_order_post" style="min-width: 80px;" id="label_order_post">邮政编码</label><span></span>
        <input id="input_order_post" type="text" value="${requestScope.order_post}" maxlength="6"/>
        <div class="br"></div>
        <label for="input_order_receiver" id="label_order_receiver">收货人姓名</label><span class="mustValue">*</span>
        <input id="input_order_receiver" type="text" value="${requestScope.order_receiver}" maxlength="20"/>
        <div class="br"></div>
        <label for="input_order_phone" id="label_order_phone">手机号码</label><span class="mustValue">*</span>
        <input id="input_order_phone" type="text" value="${requestScope.order_phone}" maxlength="11"/>
    </div>
    <div class="order_info">
        <h2>确认订单信息</h2>
        <table class="table_order_orderItem">
            <thead>
            <tr>
                <th>书籍名称</th>
                <th>单价</th>
                <th>数量</th>
                <th>小计</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${requestScope.orderItemList}" var="orderItem" varStatus="i">
                <tr class="tr_shop">
                        <%-- <td><span class="span_shopTitle">店铺：</span><span
                                class="span_shopName">贤趣${orderItem.productOrderItem_product.product_category.category_name}旗舰店</span>
                        </td> --%>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr class="tr_product_info">
                    <td><img
                            src="${orderItem.productOrderItem_product.singleProductImageList[0].productImage_src}"
                            style="width: 50px;height: 50px;"/><span class="span_product_name"><a
                            href="${pageContext.request.contextPath}/product/${orderItem.productOrderItem_product.product_id}">${orderItem.productOrderItem_product.product_name}</a></span>
                    </td>
                    <td><span
                            class="span_product_sale_price">${orderItem.productOrderItem_product.product_sale_price}0</span>
                    </td>
                    <td><span class="span_productOrderItem_number">${orderItem.productOrderItem_number}</span></td>
                    <td><span class="span_productOrderItem_price">${orderItem.productOrderItem_price}0</span></td>
                </tr>
                <tr class="tr_userMessage">
                    <td><label for="input_userMessage_${i.count}">备注：</label><textarea maxlength="500"
                                                                                       id="input_userMessage_${i.count}"
                                                                                       placeholder="选填:填写内容已和卖家协商确认"
                                                                                       class="input_userMessage"></textarea>
                    </td>
                    <td></td>
                    <td></td>
                    <td colspan="4"><input type="hidden" class="input_orderItem_id"
                                           value="${orderItem.productOrderItem_id}"/>
                </tr>
                <tr class="tr_orderCount">
                    <td colspan="3"></td>
                    <td><span class="span_price_name">合计(含运费)</span><span
                            class="span_price_value">￥${orderItem.productOrderItem_price}0</span></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="order_count_div">
        <div class="order_count_div_main">
            <div class="order_count_div_content">
                <h1 class="order_count_div_price">
                    <span class="order-title">实付款：</span><span class="realPay-price_unit">￥</span><span
                        class="realPay-price">${requestScope.orderTotalPrice}0</span>
                </h1>
                <h1 class="order_count_div_address">
                    <span class="order-title">寄送至：</span><span class="order-value address_province"></span><span
                        class="order-value address_city"></span><span class="order-value address_district"></span><span
                        class="order-value address_details">${requestScope.detailsAddress}</span>
                </h1>
                <h1 class="order_count_div_phone">
                    <span class="order-title">收货人：</span><span
                        class="order-value user-name">${requestScope.order_receiver}</span><span
                        class="order-value user-phone">${requestScope.order_phone}</span>
                </h1>
            </div>
        </div>
    </div>
    <script>
        function payOne() {
            var addressId = $("#select_order_address_province").val();
            var cityAddressId = $("#select_order_address_city").val();
            var districtAddressId = $("#select_order_address_district").val();
            var productOrder_detail_address = $.trim($("#textarea_details_address").val());
            var productOrder_post = $.trim($("#input_order_post").val());
            var productOrder_receiver = $.trim($("#input_order_receiver").val());
            var productOrder_mobile = $.trim($("#input_order_phone").val());
            var userMessage = $.trim($("#input_userMessage_1").val());
            var orderItem_product_id = parseInt('${requestScope.orderItemList[0].productOrderItem_product.product_id}');
            var orderItem_number = parseInt('${requestScope.orderItemList[0].productOrderItem_number}');

            var yn = true;
            if (productOrder_detail_address === "") {
                styleUtil.specialBasicErrorShow($("#label_details_address"));
                yn = false;
            }
            if (productOrder_receiver === "") {
                styleUtil.specialBasicErrorShow($("#label_order_receiver"));
                yn = false;
            }
            var re = /^(1[3-8][0-9]{9})$/;
            if (!re.test(productOrder_mobile)) {
                styleUtil.specialBasicErrorShow($("#label_order_phone"));
                yn = false;
            }
            re = /^[1-9][0-9]{5}$/;
            if (!re.test(productOrder_post) && productOrder_post !== "") {
                styleUtil.specialBasicErrorShow($("#label_order_post"));
                yn = false;
            }
            if (!yn) {
                window.scrollTo(0, 0);
                return false;
            }
            $.ajax({
                url: "/mall/order",
                type: "POST",
                data: {
                    "addressId": addressId,
                    "cityAddressId": cityAddressId,
                    "districtAddressId": districtAddressId,
                    "productOrder_detail_address": productOrder_detail_address,
                    "productOrder_post": productOrder_post,
                    "productOrder_receiver": productOrder_receiver,
                    "productOrder_mobile": productOrder_mobile,
                    "userMessage": userMessage,
                    "orderItem_product_id": orderItem_product_id,
                    "orderItem_number": orderItem_number
                },
                dataType: "json",
                success: function (data) {
                    data = typeof data === "string" ? JSON.parse(data) : data;
                    if (data.success) {
                        location.href = "/mall" + data.url;
                    } else {
                        alert("订单创建失败，请稍后再试！");
                        location.reload(true);
                    }
                },
                beforeSend: function () {

                },
                error: function () {
                    alert("订单提交出现问题，请重新提交！");
                    location.reload(true);
                }
            });
        }

        function payList() {
            var addressId = $("#select_order_address_province").val();
            var cityAddressId = $("#select_order_address_city").val();
            var districtAddressId = $("#select_order_address_district").val();
            var productOrder_detail_address = $.trim($("#textarea_details_address").val());
            var productOrder_post = $.trim($("#input_order_post").val());
            var productOrder_receiver = $.trim($("#input_order_receiver").val());
            var productOrder_mobile = $.trim($("#input_order_phone").val());

            var yn = true;
            if (productOrder_detail_address === "") {
                styleUtil.specialBasicErrorShow($("#label_details_address"));
                yn = false;
            }
            if (productOrder_receiver === "") {
                styleUtil.specialBasicErrorShow($("#label_order_receiver"));
                yn = false;
            }
            var re = /^(13[0-9]{9})|(15[89][0-9]{8})$/;
            if (!re.test(productOrder_mobile)) {
                styleUtil.specialBasicErrorShow($("#label_order_phone"));
                yn = false;
            }
            re = /^[1-9][0-9]{5}$/;
            if (!re.test(productOrder_post) && productOrder_post !== "") {
                styleUtil.specialBasicErrorShow($("#label_order_post"));
                yn = false;
            }
            if (!yn) {
                window.scrollTo(0, 0);
                return false;
            }
            var orderItemMap = {};
            var tr = $(".tr_userMessage");
            tr.each(function () {
                var orderItem_id = $(this).find(".input_orderItem_id").val();
                if (isNaN(orderItem_id) || orderItem_id === "") {
                    location.reload(true);
                    return false;
                }
                orderItemMap[orderItem_id] = $(this).find(".input_userMessage").val();
            });
            $.ajax({
                url: "/mall/order/list",
                type: "POST",
                data: {
                    "addressId": addressId,
                    "cityAddressId": cityAddressId,
                    "districtAddressId": districtAddressId,
                    "productOrder_detail_address": productOrder_detail_address,
                    "productOrder_post": productOrder_post,
                    "productOrder_receiver": productOrder_receiver,
                    "productOrder_mobile": productOrder_mobile,
                    "orderItemJSON": JSON.stringify(orderItemMap)
                },
                traditional: true,
                success: function (data) {
                    if (data.success) {
                        location.href = "/mall" + data.url;
                        return true;
                    } else {
                        alert("订单创建失败，请稍后再试！");
                        location.reload(true);
                    }
                },
                beforeSend: function () {
                },
                error: function () {
                    alert("订单创建失败，请稍后再试！");
                    location.reload(true);
                }
            });
        }
    </script>
    <div class="order_info_last">
        <c:choose>
            <c:when test="${requestScope.orderItemList[0].productOrderItem_id != null}">
                <a href="javascript:void(0)" title="提交订单" class="go-btn" onclick="payList()">提交订单</a>
            </c:when>
            <c:otherwise>
                <a href="javascript:void(0)" title="提交订单" class="go-btn" onclick="payOne()">提交订单</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<%@include file="include/footer_two.jsp" %>
<%@include file="include/footer.jsp" %>
<div class="loader"></div>
</body>
