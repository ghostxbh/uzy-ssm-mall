<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="include/header.jsp" %>
<head>
    <link href="${pageContext.request.contextPath}/res/css/fore/fore_orderPay.css" rel="stylesheet"/>

    <title>网上支付</title>
</head>
<style>
    h3 {
        color: #000;
        line-height: 84px;
        margin: 15px 0 10px;
    }
</style>
<body>
<nav>
    <%@ include file="include/navigator.jsp" %>
    <div class="header">
        <div id="mallLogo">
            <a href="${pageContext.request.contextPath}"><img
                    src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/logo-small2.png"></a>
        </div>
    </div>
</nav>
<div class="content">
    <form action="${pageContext.request.contextPath}/pay/goAlipay" method="post">
        <div class="order_div">
            <h3>支付信息</h3>
            <hr/>
            <c:choose>
                <c:when test="${fn:length(requestScope.productOrder.productOrderItemList)==1}">
                    <div class="order_name">专家名称：
                        <span>${requestScope.productOrder.productOrderItemList[0].productOrderItem_product.product_name}</span>
                    </div>
                    <div class="order_shop_name">
                            <%-- <span>卖家昵称：天猫${requestScope.productOrder.productOrderItemList[0].productOrderItem_product.product_category.category_name}旗舰店</span> --%>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="order_name">
                        <span>优账云 ： 合并订单：${fn:length(requestScope.productOrder.productOrderItemList)}笔</span>
                    </div>
                </c:otherwise>
            </c:choose>
            <div class="order_price">服务价格：
                <span class="price_value">${requestScope.orderTotalPrice}</span>
                <span class="price_unit">元</span>
            </div>
            <div class="order_code">订单编号：
                <span>${requestScope.productOrder.productOrder_code}</span>
            </div>
            <input id="productID" type="hidden" name="productId"
                   value="${requestScope.productOrder.productOrderItemList[0].productOrderItem_product.product_id}">
            <input id="orderNum" type="hidden" name="orderNum" value="${requestScope.productOrder.productOrder_code}">
            <input id="orderName" type="hidden" name="orderName"
                   value="${requestScope.productOrder.productOrderItemList[0].productOrderItem_product.product_name}">
            <input id="orderPrice" type="hidden" name="orderPrice" value="${requestScope.orderTotalPrice}">
            <input id="body" type="hidden" name="body"
                   value="${requestScope.productOrder.productOrderItemList[0].productOrderItem_product.product_name}">
            <input id="spbillCreateIp" type="hidden" name="spbillCreateIp" value="">
            <hr/>
            <h3>常用支付方式</h3>
            <hr/>
            <div class="pay_box">
                <table class="payment_table" border="0" cellspacing="0" cellpadding="0">
                    <tbody>
                    <tr>
                        <td>
                            <a href="javascript:void(0);" class="selPayType1 current" lay-id="user1" paytype="51">
                                <img src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/zhifubao.gif"
                                     title="支付宝" width="140" height="40">
                                <span></span>
                            </a>
                            <p></p>
                        </td>
                        <%--<td>
                            <a href="javascript:void(0);" class="selPayType1" lay-id="user2" paytype="84" >
                                <img src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/weixin.gif" title="微信支付" width="140" height="40">
                                <span></span>
                            </a>
                            <p></p>
                        </td>--%>
                    </tr>
                    </tbody>
                </table>
                <!--花呗分期选择-->
                <div id="divAlipayhb" class="instalments" style="display: none;"></div>
                <!--end/花呗分期选择-->
                <!-- <a  type="submit" class=" btn btn_big" value="立即支付">立即支付</a> -->
                <input id="hidCommonPayType" type="hidden" value="51">
            </div>
            <a href="${pageContext.request.contextPath}/order/0/10" class="btn btn-danger">立即支付</a>
        </div>
    </form>
    <!-- <div class="order_pay_div">
        <a id="order_pay" class="order_pay_btn" href="javascript:void(0)" onclick="pay()">确认支付</a>
    </div> -->
</div>
<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">模态框（Modal）标题</h4>
            </div>
            <div class="modal-body" id="erweima"></div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary">提交更改</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<script src="http://pv.sohu.com/cityjson"></script>
<script type="text/javascript">
    $(document).ready(function () {
        //获取客户端ip
        var localIp = returnCitySN["cip"]
        if (localIp == "" || localIp == null) {
            localIp = "101.40.88.152";
        }
        $("#spbillCreateIp").val(localIp);
        /* var img =
        ${requestScope.img}+"";
	    if (img != null && img != "") {	    	
	    	$("#erweima").append("<img alt='微信支付二维码' src='/AccountingService"+img+"'/>");
	    	$("#myModal").modal("show");  
		} */
        //获取客户端的省市
        /*  $.ajax({
                url: 'http://api.map.baidu.com/location/ip?ak=ia6HfFL660Bvh43exmH9LrI6',
                type: 'POST',
                dataType: 'jsonp',
                success:function(data) {
                    //$('#city').html(data.content.address_detail.province + "," + data.content.address_detail.city)
                    alert(data.content.address_detail.province + "," + data.content.address_detail.city);
                }
           }); */

    });
    //切换支付方式
    var paysrc = "user1";
    $(".selPayType1").on("click", function () {
        paysrc = $(this).attr("lay-id");
        $('.selPayType1').removeClass('current');
        $(this).addClass('current').siblings().removeClass('current');
        /*alert(paysrc);*/
        console.log(paysrc);
    });

    //获取当前通信路径
    function getRootPath() {
        var curPath = window.document.location.href;
        var pathName = window.document.location.pathname;
        var pos = curPath.indexOf(pathName);
        var localhostPath = curPath.substring(0, pos);
        //var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
        return localhostPath;
    }

    //console.log(getRootPath());
    $("#submit").click(function () {
        url = "";
        if (paysrc == "user1") {
            url = getRootPath() + "/mall/alipay/pcPay";
            $("#formId").attr("action", url).submit();
        } else {
            //H5支付
            /*if (window.screen.width <= 768) {
                url = getRootPath()+"/mall/weixinMobile/h5pay";
                $("#formId").attr("action",url).submit();
            } else {
                url = getRootPath()+"/mall/wxpay/pcPay";
                $("#formId").attr("action",url).submit();
            }*/
            url = getRootPath() + "/mall/wxpay/pcPay";
            $("#formId").attr("action", url).submit();
        }
    })

    function pay() {
        $.ajax({
            url: "${pageContext.request.contextPath}/order/pay/${requestScope.productOrder.productOrder_code}",
            type: "PUT",
            data: null,
            dataType: "json",
            success: function (data) {
                if (data.success !== true) {
                    alert("订单处理异常，请稍候再试！");
                }
                location.href = "/mall/" + data.url;
            },
            error: function () {
                alert("订单支付出现问题，请重新支付！");
            }
        });
    }
</script>
<%@ include file="include/footer_two.jsp" %>
<%@include file="include/footer.jsp" %>
</body>