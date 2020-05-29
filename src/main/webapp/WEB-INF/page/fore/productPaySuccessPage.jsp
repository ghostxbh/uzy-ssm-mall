<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="include/header.jsp" %>
<head>
    <link href="${pageContext.request.contextPath}/res/css/fore/fore_orderPaySuccess.css" rel="stylesheet"/>
    <title>网上支付</title>
</head>
<body>
<nav>
    <%@ include file="include/navigator.jsp" %>
    <div class="header">
        <div id="mallLogo">
            <a href="${pageContext.request.contextPath}"><img
                    src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/logo-small2.png"></a>
        </div>
        <div class="shopSearchHeader">
            <form action="${pageContext.request.contextPath}/product" method="get">
                <div class="shopSearchInput">
                    <input type="text" class="searchInput" name="product_name" placeholder="搜索 "
                           value="${requestScope.searchValue}" maxlength="50">
                    <input type="submit" value="搜 索" class="searchBtn">
                </div>
            </form>
            <ul>
                <c:forEach items="${requestScope.categoryList}" var="category" varStatus="i">
                    <li>
                        <a href="${pageContext.request.contextPath}/product?category_id=${category.category_id}">${category.category_name}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</nav>
<div class="content">
    <div class="content_main">
        <div id="J_AmountList">
            <h2>您已成功付款</h2>
            <div class="summary_pay_done">
                <ul>
                    <li>
                        收货地址：<span>${requestScope.productOrder.productOrder_detail_address} ${requestScope.productOrder.productOrder_receiver} ${requestScope.productOrder.productOrder_mobile}</span>
                    </li>
                    <li>实付款：<span><em>￥${requestScope.orderTotalPrice}</em></span></li>
                </ul>
            </div>
        </div>
        <div id="J_ButtonList">
            <span class="info">您可以 </span>
            <a class="J_MakePoint" href="${pageContext.request.contextPath}/order/0/10">查看已买到的宝贝</a>
        </div>
        <div id="J_RemindList">
            <ul>
                <li class="alertLi">
                    <p>
                        <strong>安全提醒：</strong>
                        <span class="info">下单后，</span>
                        <span class="warn">客服会和您确认订单，请您注意查看！</span>
                        <span class="info">如有订单异常等问题，请您及时联系我们的客服！</span>
                    </p>
                </li>
            </ul>
        </div>
        <div id="J_Qrcode">
            <div class="mui-tm">
                <%-- <a target="_blank" href="http://pages.tmall.com/wow/portal/act/app-download">
                    <img class="type2-info"
                         src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/TB1c1dwRFXXXXaMapXXXXXXXXXX-259-81.png"/>
                    <img class="type2-qrcode"
                         src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/TB1A2aISXXXXXX4XXXXwu0bFXXX.png"/>
                </a> --%>
            </div>
        </div>
    </div>
</div>
<%@include file="include/footer_two.jsp" %>
<%@include file="include/footer.jsp" %>
</body>