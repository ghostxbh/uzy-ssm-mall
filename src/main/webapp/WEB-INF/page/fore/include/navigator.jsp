<%@ page contentType="text/html;charset=UTF-8" %>
<meta charset="utf-8"/>
<meta name="renderer" content="webkit"/>
<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/res/css/fore/fore_nav.css"/>
<style>

#out{
  position:relative;float:left;cursor:pointer;width:50px;height:71px;
  }
 .personal-content{cursor:pointer;display:none;
   position:absolute;border:1px solid #ccc;box-sizing:border-box; width:235px;
 top:70px;background:#fff;box-shadow: 0px 2px 2px 0px rgba(173, 187, 200, 0.19);height:85px;
 padding:15px 0px;
 }
 .personal-account{
 width:150px;
 }
 .personal-content p{
   line-height:20px;
  margin-left:10px;
  font-size:12px; color:#333;
 }
  .headimg{
  margin-left:10px;margin-right: 4px; height: 39px; width: 39px;border-radius: 50px;float:left;
  }

   .hidep{
   width:100px;

   }
</style>
<div id="nav">
    <div class="nav_main">
        <p id="container_login">
            <c:choose>
                <c:when test="${requestScope.user.user_name==null}">
                    <em>Ding，欢迎来柚子云购商城</em>
                    <a href="${pageContext.request.contextPath}/login" style="color:#FA0808">请登录</a>
                    <a href="${pageContext.request.contextPath}/register">免费注册</a>
                </c:when>
                <c:otherwise>
                    <em>Hi，</em>
                    <a href="${pageContext.request.contextPath}/userDetails" class="userName" target="_blank">${requestScope.user.user_name}</a>
                    <a href="${pageContext.request.contextPath}/login/logout">退出</a>
                    <div id="out" style="display:none;"><img src='${pageContext.request.contextPath}/res/images/fore/WebsiteImage/images/tou.png' class='headimg' style="margin-top:18px;"></div>
                </c:otherwise>
            </c:choose>
        </p>
        <ul class="quick_li">
        <li class="quick_li_cart">
                <a href="${pageContext.request.contextPath}">首页</a>
            </li>
            <li class="quick_li_MyTaobao">
                <div class="sn_menu">
                 <a href="${pageContext.request.contextPath}/order/0/10">我的订单</a>
                </div>
            </li>
            <li class="quick_li_cart">
                <img src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/buyCar.png">
                <a href="${pageContext.request.contextPath}/cart">购物车</a>
            </li>
        </ul>
    </div>
</div>