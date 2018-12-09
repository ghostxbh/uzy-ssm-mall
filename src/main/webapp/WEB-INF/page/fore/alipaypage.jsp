<%@ page contentType="text/html;charset=UTF-8" %>
<%-- <%@ include file="include/header.jsp" %> --%>
<head>
	<link href="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/favicon.ico" rel="SHORTCUT ICON">
 <%--   	<link href="${pageContext.request.contextPath}/res/css/fore/fore_orderPay.css" rel="stylesheet"/> --%>
   	<script src="${pageContext.request.contextPath}/res/js/jquery-1.11.3.min.js"></script>
   	<script src="${pageContext.request.contextPath}/res/js/fore/fore_soso.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/fore/fore_main.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/fore/iconn.css"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>微信支付</title>
</head>
<style>
nav {
    width: 100%;
    border-bottom: 1px solid #c8baaa;
}
.header {
    width: 1230px;
    margin: 0 auto;
    padding-top: 15px;
    height: 70px;
}

.header > .shopNameHeader {
    display: inline-block;
    height: 38px;
    line-height: 38px;
    font-size: 12px;
    font-weight: bold;
    font-family: '宋体', serif;
    border-left: 1px solid #f0f0f0;
    padding: 0 10px;
}

.header > .shopAssessHeader {
    border-left: 1px dashed #f0f0f0;
    border-right: 1px solid #f0f0f0;
}

.header > .shopSearchHeader {
    float: right;
    font-family: '宋体', serif;
    position: relative;
}

.shopSearchHeader .shopSearchInput {
    width: 455px;
    border: solid #ff0036;
    border-width: 3px 0 3px 3px;
    height: 30px;
    margin-right: 82px;
    position: relative;
}

.shopSearchInput > .searchInput {
    width: 367px;
    padding: 5px 3px 5px 5px;
    color: #000;
    margin: 0;
    height: 20px;
    line-height: 20px;
    outline: 0;
    border: none;
    font-size: 12px;
}

.shopSearchInput > .searchTmall {
    position: absolute;
    top: 0;
    right: 0;
    width: 80px;
    height: 30px;
    background-color: #FF0036;
    border: 0;
    color: #ffffff;
    font-size: 16px;
    font-family: "Microsoft YaHei UI", serif;
}

.shopSearchHeader .searchShop {
    position: absolute;
    top: 0;
    right: 0;
    background-color: #333;
    color: #ffffff;
    font-size: 16px;
    font-family: "Microsoft YaHei UI", serif;
    width: 80px;
    height: 36px;
    border: 0;
}

.shopSearchHeader > ul {
    padding: 6px 0 0;
    height: 16px;
    margin-left: -13px;
    font-size: 13px;
    font-family: '宋体', serif;
    overflow: hidden;
}

.shopSearchHeader li {
    float: left;
    display: inline-block;
    padding: 0 12px;
    line-height: 1.1;
}

.shopSearchHeader li + li {
    border-left: 1px solid #cccccc;
}

.shopSearchHeader li > a {
    color: #999;
    text-decoration: none;
}

.shopSearchHeader li > a:hover {
    text-decoration: underline;
}
.top-div {
    background-color: #FFF;
    height: 70px;
    transition-duration: .5s;
    -webkit-transition-duration: .5s;
}
.erweimadiv{
   width:100%;background:#EFF0F1;min-width:1200px;
}

.ewmadiv{
  width:1200px;
  height:592px;
  background:#fff;
  margin:0 auto;
  padding-top:50px;
}
.containenr{width:500px;height:100%;margin:0 auto;}
.ewmadiv h5{line-height:15px;}
.greensure {
width:19px;height:19px; vertiacal-align:middle;margin-right:10px;
}
.erweimatip{
width:100%;
}
.erweimatip{
width:100%;overflow:hidden;padding-left:72px;
}
.weichat{
 width:120px;height:60px;
}
.erweimatip img{
width:6%;
height:30px;
float:left;
}
.erweimatip p{
 padding-left:20px; 
}
.order_pay_div {
    overflow: hidden;
    margin-top: 50px;
    margin-left: 70px;
}

.order_pay_btn {
    display: inline-block;
    width: 80px;
    height: 32px;
    color: #fff;
    /* border: 1px solid #0ae; */
    background-color: #0ae;
    vertical-align: middle;
    cursor: pointer;
    font-size: 14px;
    font-weight: 700;
    border-radius: 2px;
    padding: 0 20px;
    margin: 0 auto;
    line-height: 32px;
}


</style>
<body>	
<nav>    
    <div class="header">
        <a href="${pageContext.request.contextPath}"><img
                src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/logo-small2.png"></a>
        <%-- <span class="shopNameHeader">贤趣${requestScope.product.product_category.category_name}官方旗舰店</span> --%>
        <input id="tid" type="hidden" value="${requestScope.product.product_category.category_id}"/>
        <%-- <img src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/detailsHeaderA.png"
             class="shopAssessHeader"> --%>
        <div class="shopSearchHeader">
            <form action="${pageContext.request.contextPath}/product" method="get">
                <div class="shopSearchInput">
                    <input type="text" class="searchInput" name="product_name" placeholder="搜索 "
                           maxlength="50">
                    <input type="submit" value="搜索" class="searchTmall">
                </div>
                <!-- <input type="submit" value="搜本店" class="searchShop"> -->
            </form>
            <ul>
                <c:forEach items="${requestScope.categoryList}" var="category" varStatus="i">
                    <li>
                        <%-- <a href="${pageContext.request.contextPath}/product?category_id=${category.category_id}">${category.category_name}</a> --%>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</nav>
<div class="erweimadiv">
    <div class="ewmadiv">
        <div class="containenr"> 
                <img src="${pageContext.request.contextPath}/res/images/fore/home/weichat.png" class="weichat">
             <h5><img src="${pageContext.request.contextPath}/res/images/fore/home/greensure.png" class="greensure">安全设置检测成功！无需短信校验。</h5>
	        <div class="weima"><img alt="微信二维码" src="${pageContext.request.contextPath}${requestScope.img}"></div>
            <div class="erweimatip"><img src="${pageContext.request.contextPath}/res/images/fore/home/shaomiao.png"><p>打开微信二维码<br/>扫一扫继续付款</p></div>
            <div class="order_pay_div">
			    <a id="order_pay" class="order_pay_btn" href="javascript:void(0)" onclick="pay()">已完成付款</a>
			</div> 
         </div>
    </div>
</div>
<%@include file="include/footer_two.jsp" %>
<%@include file="include/footer.jsp" %>
</body>
<script>
	function pay(){ 
	    $.ajax({
	        url: "${pageContext.request.contextPath}/order/pay/${requestScope.outTradeNo}",
	        type: "PUT",
	        data: null,
	        dataType: "json",
	        success: function (data) {
	            if (data.success !== true) {
	                alert("订单处理异常，请稍候再试！");
	            }
	            location.href = "/bookstore/" + data.url;
	        },	         
	        error: function () {
	            alert("订单支付出现问题，请重新支付！");	
	        }
	    });
	} 
    window.onload = function () {
        $(".btn-classify").click(function (e) {
            e.stopPropagation(),
                $(".search-result").hide(),
                $(this).siblings(".search-classify").show()
        });
        $(".search-classify dd").mouseover(function () {
            var _index = $(this).index();
            $(this).children('div').css("display", 'block');
        });
        $(".search-classify dd").mouseout(function () {
            var _index = $(this).index();
            $(this).children('div').css("display", 'none');
        });

        /*  var barTop = $('.qt-header-fixed ').offset().top;
         $(window).on('scroll',function () {
             var scrollTop = $(document).scrollTop();
             if (scrollTop >= barTop) {
                //固定在上方
                 $('.qt-header-fixed ').css({position: 'fixed',top: 0});
                 // 显示替代的条
                 $('.search-fixed').css('display','block');
             } else {
                 $('.qt-header-fixed ').css('position','static');
                 $('.search-fixed').css('display','none');
             }
         }); */


    }


</script>