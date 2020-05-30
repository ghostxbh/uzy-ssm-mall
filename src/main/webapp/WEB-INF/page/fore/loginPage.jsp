<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="include/header.jsp" %>
<head>
    <script src="${pageContext.request.contextPath}/res/js/fore/fore_login.js"></script>
    <link href="${pageContext.request.contextPath}/res/css/fore/fore_login.css" rel="stylesheet">
    <style rel="stylesheet">
        #baseNavigator {
            padding: 10px 0;
            width: 1190px;
            height: 20px;
            margin: auto;
        }

        #baseNavigator img {
            width: 190px;
            height:120px;
            margin-top: 8px;
            display:block;
        }

        /*#baseNavigator {
            width: auto;
            height: 20px;
            font-family: "Microsoft YaHei UI", Tahoma, serif;
            font-size: 12px;
            position: relative !important;
            background: #f2f2f2;
            z-index: 999;
            border-bottom: 1px solid #e5e5e5;
        }*/
    </style>
    <title>柚子云购 -- 登录</title>
</head>
<body>
<%@ include file="include/navigator.jsp" %>
<nav id="baseNavigator">
   <a href="${pageContext.request.contextPath}" target="_self">
        <img src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/logo.png" style="margin-top:-30px;"/>
  </a>
</nav>
<div class="content">
    <div class="contentMain"></div>
    <div class="loginDiv">
        <div class="loginSwitch" id="loginSwitch"></div>
        <div class="loginMessage">
            <div class="loginMessageMain">
                <div class="poptip-arrow"><em></em><span></span></div>
                <img src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/scan-safe.png"/><span>扫码登录更安全</span>
            </div>
        </div>
        <div class="pwdLogin">
            <span class="loginTitle">密码登录</span>
            <form method="post" class="loginForm">
                <div class="loginInputDiv">
                    <label for="name" class="loginLabel"><img
                            src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/2018-04-27_235518.png"
                            width="38px" height="39px" title="会员名"/></label>
                    <input type="text" name="name" id="name" class="loginInput" placeholder="会员名/邮箱/手机号">
                </div>
                <div class="loginInputDiv">
                    <label for="password" class="loginLabel"><img
                            src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/2018-04-27_235533.png"
                            width="38px" height="39px" title="登录密码"/></label>
                    <input type="password" name="password" id="password" class="loginInput">
                </div>
                <input type="submit" class="loginButton" value="登 录">
            </form>
            <div class="loginLinks">
                <a href="#">忘记密码</a>
                <a href="#">忘记会员名</a>
                <a href="${pageContext.request.contextPath}/register">免费注册</a>
            </div>
            <div class="error_message">
                <p id="error_message_p"></p>
            </div>
        </div>
        <div class="qrcodeLogin">
            <span class="loginTitle">手机扫码，安全登录</span>
            <div class="qrcodeMain">
                <img src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/login_qrcode.png"
                     id="qrcodeA"/>
                <img src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/login_qrcodeB.png"
                     id="qrcodeB"/>
            </div>
            <div class="qrcodeFooter">
                <img src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/scan_icon2.png">
                <p>打开 <a href="https://mall.uzykj.com">手机</a> | <a
                        href="https://uzykj.com/m">手机</a>扫一扫登录</p>
            </div>
            <div class="loginLinks">
                <a href="JavaScript:void(0)" id="pwdLogin">密码登录</a>
                <a href="#">免费注册</a>
            </div>
        </div>
    </div>
</div>
<%-- <%@ include file="include/footer.jsp" %> --%>
<link href="${pageContext.request.contextPath}/res/css/fore/fore_foot_special.css" rel="stylesheet"/>
<%@ include file="include/footer_two.jsp" %>
<%@ include file="include/footer.jsp" %>
</body>
