<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>登录态已失效</title>
    <script>
        $(function () {
            var time = 3;
            setInterval(function () {
                if (time === 0) {
                    location.href = "/mall/admin/login";
                } else {
                    time--;
                    $("#message_time").text(time);
                }
            }, 1000);
        })
    </script>
    <style rel="stylesheet">
        .message_div {
            width: 500px;
            height: 500px;
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
            margin: auto;
            text-align: center;
        }

        #message_time {
            display: inline-block;
            padding-left: 5px;
        }

        #goToLogin {
            display: inline-block;
            padding-left: 5px;
        }
    </style>
</head>
<body>
<div class="message_div">
    <p>登录态已失效，请重新登录...<span class="td_special" id="message_time">3</span><span class="td_wait" id="goToLogin"><a
            href="${pageContext.request.contextPath}/admin/login">转到</a></span></p>
</div>
</body>
</html>
