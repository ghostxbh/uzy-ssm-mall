<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <script>
        $(function () {

            /******
             * event
             ******/
            //单击取消按钮时
            $("#btn_user_cancel").click(function () {
                $(".menu_li[data-toggle=user]").click();
            });
        });

        //获取产品子界面
        function getChildPage(obj) {
            //设置样式
            $("#div_home_title").children("span").text("产品详情");
            document.title = "柚子云购 - 产品详情";
            //ajax请求页面
            ajaxUtil.getPage("product/" + $(obj).parents("tr").find(".product_id").text(), null, true);
        }
    </script>
    <style rel="stylesheet">
        #user_profile_picture {
            border-radius: 5px;
        }

        #table_orderItem_list th:first-child {
            width: auto;
        }
    </style>
</head>
<body>
<div class="details_div_first">
    <input type="hidden" value="${requestScope.user.user_id}" id="details_user_id"/>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_user_id">用户编号</label>
        <span class="details_value" id="span_user_id">${requestScope.user.user_id}</span>
    </div>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_user_name">用户名</label>
        <span class="details_value" id="span_user_name">${requestScope.user.user_name}</span>
    </div>
</div>
<div class="details_div">
    <span class="details_title text_info">基本信息</span>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_user_profile_picture">用户头像</label>
        <img
                src="${pageContext.request.contextPath}/res/images/item/userProfilePicture/${requestScope.user.user_profile_picture_src}"
                id="user_profile_picture" width="84px" height="84px"
                onerror="this.src='${pageContext.request.contextPath}/res/images/admin/loginPage/default_profile_picture-128x128.png'"/>
    </div>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_user_nickname">用户昵称</label>
        <span class="details_value td_wait" id="span_user_nickname">${requestScope.user.user_nickname}</span>
    </div>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_user_realname">用户姓名</label>
        <span class="details_value" id="span_user_realname">${requestScope.user.user_realname}</span>
    </div>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_user_gender">性别</label>
        <span class="details_value" id="span_user_gender">
            <c:choose>
                <c:when test="${user.user_gender==0}">男</c:when>
                <c:otherwise>女</c:otherwise>
            </c:choose>
        </span>
    </div>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_user_birthday">出生日期</label>
        <span class="details_value" id="span_user_birthday">${requestScope.user.user_birthday}</span>
    </div>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_user_address">所在地</label>
        <span class="details_value details_value_noRows"
              id="span_user_address">${requestScope.user.user_address.address_name}</span>
    </div>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_user_homeplace">家乡</label>
        <span class="details_value details_value_noRows"
              id="span_user_homeplace">${requestScope.user.user_homeplace.address_name}</span>
    </div>
</div>
<div class="details_div details_div_last">
    <span class="details_title text_info">购物车信息</span>
    <table class="table_normal" id="table_orderItem_list">
        <thead class="text_info">
        <tr>
            <th>产品图片</th>
            <th>产品名称</th>
            <th>单价</th>
            <th>数量</th>
            <th>价格</th>
            <th>备注</th>
            <th>操作</th>
            <th hidden class="product_id">产品ID</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${requestScope.user.productOrderItemList}" var="item" varStatus="i">
            <tr>
                <td title="产品图片"><img
                        src="${pageContext.request.contextPath}/res/images/item/productSinglePicture/${item.productOrderItem_product.singleProductImageList[0].productImage_src}"
                        id="pic_single_${item.productOrderItem_product.singleProductImageList[0].productImage_id}"
                        width="42px" height="42px"
                        name="${item.productOrderItem_product.singleProductImageList[0].productImage_id}"/></td>
                <td title="${item.productOrderItem_product.product_name}">${item.productOrderItem_product.product_name}</td>
                <td title="${item.productOrderItem_product.product_sale_price}">${item.productOrderItem_product.product_sale_price}</td>
                <td title="${item.productOrderItem_number}">${item.productOrderItem_number}</td>
                <td title="${item.productOrderItem_price}">${item.productOrderItem_price}</td>
                <td title="${item.productOrderItem_userMessage}">${item.productOrderItem_userMessage}</td>
                <td><span class="td_special" title="查看产品详情"><a href="javascript:void(0)"
                                                               onclick="getChildPage(this)">详情</a></span></td>
                <td hidden><span class="product_id">${item.productOrderItem_product.product_id}</span></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<div class="details_tools_div">
    <input class="frm_btn frm_clear" id="btn_user_cancel" type="button" value="取消"/>
</div>
<div class="loader"></div>
</body>
</html>
