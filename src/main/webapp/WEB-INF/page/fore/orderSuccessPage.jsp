<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="include/header.jsp" %>
<head>
    <link href="${pageContext.request.contextPath}/res/css/fore/fore_orderSuccessPage.css" rel="stylesheet"/>
    <title>交易成功</title>
</head>
<body>
<nav>
    <%@ include file="include/navigator.jsp" %>
</nav>
<div class="header">
    <div id="mallLogo">
        <a href="${pageContext.request.contextPath}"><img
                src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/logo-small2.png"></a>
    </div>
    <div class="shopSearchHeader">
        <form action="${pageContext.request.contextPath}/product" method="get">
            <div class="shopSearchInput">
                <input type="text" class="searchInput" name="product_name" placeholder="搜索 "
                       maxlength="50">
                <input type="submit" value="搜 索" class="searchBtn">
            </div>
        </form>
    </div>
</div>
<div class="content">
    <div class="take-delivery">
        <div class="summary-status">
            <h2>交易已经成功，卖家将收到您的货款。</h2>
            <c:if test="${requestScope.product != null}">
                <div class="successInfo">
                    <ul class="info-rate-coin">
                        <li>
                            <span class="review_msg">认真填写商品评价，就有机会获得经验值！</span>
                            <a class="J_makePoint"
                               href="${pageContext.request.contextPath}/review/${requestScope.orderItem.productOrderItem_id}">
                                <img src="${requestScope.product.singleProductImageList[0].productImage_src}"
                                     width="100px" height="100px"/>
                                <p class="product_name"
                                   title="${requestScope.product.product_name}">${requestScope.product.product_name}</p>
                                <span class="vi-btn">立即评价</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </c:if>
            <p>您可以查看：<a href="${pageContext.request.contextPath}/order/0/10">已买到的产品</a></p>
        </div>
    </div>
</div>
<%@include file="include/footer_two.jsp" %>
<%@include file="include/footer.jsp" %>
</body>