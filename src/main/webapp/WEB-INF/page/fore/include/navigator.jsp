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
<script>
function getRootPath() {
    var curPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curPath.indexOf(pathName);
    var localhostPath = curPath.substring(0, pos);
    //var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    return localhostPath;
}

    $(function () {
        $(".quick_li").find("li").hover(
            function () {
                $(this).find(".sn_menu").addClass("sn_menu_hover");
                $(this).find(".quick_menu,.quick_qrcode,.quick_DirectPromoDiv,.quick_sitmap_div").css("display", "block");
            }, function () {
                $(this).find(".sn_menu").removeClass("sn_menu_hover");
                $(this).find(".quick_menu,.quick_qrcode,.quick_DirectPromoDiv,.quick_sitmap_div").css("display", "none");
            }
        );
    });
    $(document).ready(function(){
    	var username = "${requestScope.user.name}";

    	if(username == null ||username == ""){
    		//alert("2");
    		$.ajax({
    			   type:'get',
    			   url:getRootPath()+'/AccountingOnline/user/getuseraway',
    			   dataType:'jsonp',
    			   jsonp:'jsonpCallback',
    			   async:false,
    			   data:{},
    			   error:function(){
    				   //alert("0");
    			   },
    			   success:function(data){
    				   //alert(data.name);
    				   if(data != null){
    					   location.href = getRootPath()+"/AccountingOnline/user/checkLogin?url=bookstore/getsign";
    					   /* $("#login-head").hide(); */
    		   			     $("#out").show();
    		   			     $("#out").append("<div class='personal-content'><img src='images/tou.png' class='headimg'><div class='personal-account' style='float:left;'><p>账号："+ username+"</p> "+
    		   			     "<p class='hidep'><a href='http://localhost:8080/AccountingOnline/page/index#flag=bookstore' style='margin-left:18px;border:none;background:#fff;font-size:12px; color:#333;'>个人设置</a></p>"+
    		   			     "<p><a href='http://www.jkj521.cn/AccountingOnline/logout' style='float:right;border:none;background:#fff;font-size:12px; color:#333;'>退出</a></p></div> </div>")

    				   }
    			   }
    		   })
		    		   $('#out').mouseover(function(){
				   	    	 $('.personal-content').show();
				   	   })
				   	   $('#out').mouseleave(function(){
				   	    	$('.personal-content').hide();
				   	   })
		    	}
    });

</script>
<div id="nav">
    <div class="nav_main">
        <p id="container_login">
            <c:choose>
                <c:when test="${requestScope.user.name==null}">
                    <em>Ding，欢迎来优账云财税书店</em>
                    <a href="/bookstore/login" style="color:#FA0808">请登录</a>
                    <a href="/bookstore/register">免费注册</a>
                </c:when>
                <c:otherwise>
                    <em>Hi，</em>
                    <a href="${pageContext.request.contextPath}/userDetails" class="userName" target="_blank">${requestScope.user.name}</a>
                    <a href="/bookstore/login/logout">退出</a>
                    <div id="out" style="display:none;"><img src='images/tou.png' class='headimg' style="margin-top:18px;"></div>
                </c:otherwise>
            </c:choose>
        </p>
        <ul class="quick_li">
        <li class="quick_li_cart">
                <a href="http://www.jkj521.cn">优账云首页</a>
            </li>
            <li class="quick_li_MyTaobao">
                <div class="sn_menu">
                 <a href="${pageContext.request.contextPath}/order/0/10">我的书店</a>
                </div>
            </li>
            <%--<li class="quick_li_cart">
                <img src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/buyCar.png">
                <a href="${pageContext.request.contextPath}/cart">购物车</a>
            </li>--%>
        </ul>
    </div>
</div>