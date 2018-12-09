<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>页面找不到</title>
    <style rel="stylesheet">
        html {
            font-family: sans-serif;
            -webkit-text-size-adjust: 100%;
            -ms-text-size-adjust: 100%
        }

        body {
            margin: 0
        }

        article, aside, details, figcaption, figure, footer, header, hgroup, main, menu, nav, section, summary {
            display: block
        }

        audio, canvas, progress, video {
            display: inline-block;
            vertical-align: baseline
        }

        audio:not([controls]) {
            display: none;
            height: 0
        }

        [hidden], template {
            display: none
        }

        a {
            background-color: transparent
        }

        a:active, a:hover {
            outline: 0
        }

        abbr[title] {
            border-bottom: 1px dotted
        }

        b, strong {
            font-weight: 700
        }

        dfn {
            font-style: italic
        }

        h1 {
            margin: .67em 0;
            font-size: 2em
        }

        a {
            color: #337ab7;
            text-decoration: none;
        }

        .btn {
            display: inline-block;
            padding: 6px 12px;
            margin-bottom: 0;
            font-size: 14px;
            font-weight: 400;
            line-height: 1.42857143;
            text-align: center;
            white-space: nowrap;
            vertical-align: middle;
            -ms-touch-action: manipulation;
            touch-action: manipulation;
            cursor: pointer;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
            background-image: none;
            border: 1px solid transparent;
            border-radius: 4px;
        }

        .btn {
            background: #333;
            color: white;
            height: 50px;
            line-height: 50px;
            padding: 0 20px;
            text-align: center;
            font-size: 18px;
            border-radius: 0;
            border: none;
            cursor: pointer;
        }

        .btn.focus, .btn:focus, .btn:hover {
            background: #464648;
            color: white;
        }
    </style>
</head>
<body>
<div style="text-align: center">
    <img style="max-width: 100%; margin: 8% auto 0; padding: 20px;"
         src="${pageContext.request.contextPath}/res/images/fore/WebsiteImage/404.png">
    <h4 style="font-size: 40px; color: #333; margin-top: 75px;">啊呀，不小心飞出地球了！</h4>
    <p style="font-size: 14px; color: #777; margin-top: 25px;">此页面已无法找到，可能已经被删除或地址错误</p>
    <a class="btn" style="width: 180px; margin-top: 55px;" href="${pageContext.request.contextPath}">光速返回</a>
</div>
</body>
</html>