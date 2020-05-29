<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="include/header.jsp" %>
<head>
    <link href="${pageContext.request.contextPath}/res/css/fore/fore_addReview.css" rel="stylesheet"/>
    <title>添加评论</title>
    <script>
        $(function () {
            $("#review_form").submit(function () {
                var text = $.trim($("#text-review").val());
                if (text === "") {
                    $(this).css("border", "1px solid #FF0036");
                    return false;
                }
            });
            $("#text-review").focus(function () {
                $("#review_form").css("border", "1px solid #d1ccc8");
            });
        });
    </script>
</head>
<body>
<nav>
    <%@ include file="include/navigator.jsp" %>
</nav>
<div class="header">
    <div id="mallLogo">
        <a href="${pageContext.request.contextPath}"><img
                src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/logo-small.png"></a>
    </div>
    <div class="shopSearchHeader">
        <form action="${pageContext.request.contextPath}/product" method="get">
            <div class="shopSearchInput">
                <input type="text" class="searchInput" name="product_name" placeholder="搜索 书名"
                       maxlength="50">
                <input type="submit" value="搜 索" class="searchBtn">
            </div>
        </form>
    </div>
</div>
<div class="content">
    <div class="details_box">
        <div class="db-showpanel">
            <a href="${pageContext.request.contextPath}/product/${orderItem.productOrderItem_product.product_id}"
               target="_blank"><img
                    src="${orderItem.productOrderItem_product.singleProductImageList[0].productImage_src}"></a>
        </div>
        <div class="db-icbu">
            <ol class="ui-form-bd">
                <li class="ui-form-row"><h3>${orderItem.productOrderItem_product.product_name}</h3></li>
                <li class="ui-form-row superstar-price">
                    <label class="ui-form-label">价格</label>
                    <div class="ui-form_right">
                        <em>${orderItem.productOrderItem_product.product_sale_price}</em>
                        <span>元</span>
                    </div>
                </li>
                <li class="ui-from-row">
                    <label class="ui-form-label">配送</label>
                    <div class="ui-form_right"><span class="ul_right_special">快递：0.00</span></div>
                </li>
            </ol>
        </div>
        <div class="banner-totalevolute">
            <div class="tv-leftbox">
                <div class="tv-lb-head"></div>
                <div class="tv-lb-content">
                    <span>累计评价</span>
                    <em class="superstar-ratetotal">${orderItem.productOrderItem_product.product_review_count}</em>
                </div>
                <div class="tv-lb-bottom"></div>
            </div>
            <div class="tv-rightbox">
                <div class="tv-rb-cover"></div>
                <div class="tv-rb-bottom"></div>
            </div>
        </div>
        <div class="rate-compose">
            <form method="post" action="${pageContext.request.contextPath}/review" id="review_form">
                <input type="hidden" class="orderItem_id" value="${orderItem.productOrderItem_id}" name="orderItem_id">
                <div class="compose-main">
                    <div class="compose-header">
                        <span>其他买家，需要你的建议哦！</span>
                    </div>
                    <div class="compose-order">
                        <div class="J_rateInputArea">评价商品</div>
                        <div class="text-input-box">
                            <textarea id="text-review" name="review_content"></textarea>
                        </div>
                    </div>
                </div>
                <div class="compose-submit">
                    <input type="submit" value="提交评价"/>
                </div>
            </form>
        </div>
    </div>
</div>
<%@include file="include/footer_two.jsp" %>
<%@include file="include/footer.jsp" %>
</body>