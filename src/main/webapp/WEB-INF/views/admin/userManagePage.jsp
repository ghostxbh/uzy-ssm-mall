<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <script>
        //检索数据集
        var dataList = {
            "user_name": null,
            "user_gender_array": null,
            "orderBy": null,
            "isDesc": true
        };
        $(function () {
            /******
             * event
             ******/
            //点击查询按钮时
            $("#btn_user_submit").click(function () {
                var user_name = $.trim($("#input_user_name").val());
                //用户性别数组
                var gender_array = [];
                $(".radio_gender:checked").each(function () {
                    gender_array.push($(this).val());
                });
                //封装数据
                dataList.user_name = encodeURI(user_name);
                dataList.user_gender_array = gender_array;

                getData($(this), "admin/user/0/10", dataList);
            });
            //点击刷新按钮时
            $("#btn_user_refresh").click(function () {
                //清除数据
                dataList.user_name = null;
                dataList.user_gender_array = null;
                dataList.orderBy = null;
                dataList.isDesc = true;
                //获取数据
                getData($(this), "admin/user/0/10", null);
                //清除排序样式
                var table = $("#table_user_list");
                table.find("span.orderByDesc,span.orderByAsc").css("opacity","0");
                table.find("th.data_info").attr("data-sort","asc");
            });
            //点击th排序时
            $("th.data_info").click(function () {
                var table = $("#table_user_list");
                if(table.find(">tbody>tr").length <= 1){
                    return;
                }
                //获取排序字段
                dataList.orderBy = $(this).attr("data-name");
                //是否倒序排序
                dataList.isDesc = $(this).attr("data-sort")==="asc";

                getData($(this), "admin/user/0/10", dataList);
                //设置排序
                table.find("span.orderByDesc,span.orderByAsc").css("opacity","0");
                if(dataList.isDesc){
                    $(this).attr("data-sort","desc").children(".orderByAsc.orderBySelect").removeClass("orderBySelect").css("opacity","1");
                    $(this).children(".orderByDesc").addClass("orderBySelect").css("opacity","1");
                } else {
                    $(this).attr("data-sort","asc").children(".orderByDesc.orderBySelect").removeClass("orderBySelect").css("opacity","1");
                    $(this).children(".orderByAsc").addClass("orderBySelect").css("opacity","1");
                }
            });
            //点击table中的数据时
            $("#table_user_list").find(">tbody>tr").click(function () {
                trDataStyle($(this));
            });
        });
        //获取用户数据
        function getData(object,url,dataObject) {
            var table = $("#table_user_list");
            var tbody = table.children("tbody").first();
            $.ajax({
                url: url,
                type: "get",
                data: dataObject,
                traditional: true,
                success: function (data) {
                    //清空原有数据
                    tbody.empty();
                    //设置样式
                    $(".loader").css("display","none");
                    object.attr("disabled",false);
                    //显示用户统计数据
                    $("#user_count_data").text(data.userCount);
                    if (data.userList.length > 0) {
                        for (var i = 0; i < data.userList.length; i++) {
                            var gender;
                            if (data.userList[i].user_gender === 0) {
                                gender = "男";
                            } else {
                                gender = "女";
                            }
                            var user_id = data.userList[i].user_id;
                            var user_name = data.userList[i].user_name;
                            var user_nickname = data.userList[i].user_nickname;
                            var user_realname = data.userList[i].user_realname;
                            var user_birthday = data.userList[i].user_birthday;
                            //显示用户数据
                            tbody.append("<tr><td><input type='checkbox' class='cbx_select' id='cbx_user_select_" + user_id + "'><label for='cbx_user_select_" + user_id + "'></label></td><td title='" + user_name + "'>" + user_name + "</td><td title='" + user_nickname + "'>" + user_nickname + "</td><td title='" + user_realname + "'>" + user_realname + "</td><td title='" + user_birthday + "'>" + user_birthday + "</td><td title='" + gender + "'>" + gender + "</td><td><span class='td_special' title='查看用户详情'><a href='javascript:void(0);' onclick='getChildPage(this)'>详情</a></span></td><td hidden  class='user_id'>" + user_id + "</td></tr>");
                        }
                        //绑定事件
                        tbody.children("tr").click(function () {
                            trDataStyle($(this));
                        });
                        //分页
                        var pageUtil = {
                            index: data.pageUtil.index,
                            count: data.pageUtil.count,
                            total: data.pageUtil.total,
                            totalPage: data.totalPage
                        };
                        createPageDiv($(".loader"), pageUtil);
                    }
                },
                beforeSend: function () {
                    $(".loader").css("display","block");
                    object.attr("disabled",true);
                },
                error: function () {

                }
            });
        }

        //获取用户子界面
        function getChildPage(obj) {
            //设置样式
            $("#div_home_title").children("span").text("用户详情");
            document.title = "柚子云购 - 用户详情";
            //ajax请求页面
            ajaxUtil.getPage("user/" + $(obj).parents("tr").find(".user_id").text(), null, true);
        }

        //获取页码数据
        function getPage(index) {
            getData($(this), "admin/user/" + index + "/10", dataList);
        }

        //删除用户
        function deleteUser() {
            var arr = new Array();
            $("#tbodyId input[type='checkbox']:checked").each(function(index,item){
                arr.push($(this).val());
            });
            console.log(arr);
            if(arr === undefined || arr.length == 0){
                $(".msg1").stop(true, true).animate({
                    opacity: 1
                }, 550, function () {
                    $(".msg1").animate({
                        opacity: 0
                    }, 1500);
                });
            }else{
                if(window.confirm("确认删除？")){
                    $.ajax({
                        url:"admin/user/delete/"+arr,
                        type:"get",
                        date:{"productList":arr},
                        traditional: true,
                        success:function (data) {
                            if(data.success){
                                $(".msg").stop(true, true).animate({
                                    opacity: 1
                                }, 550, function () {
                                    $(".msg").animate({
                                        opacity: 0
                                    }, 1500);
                                });

                            }
                        }
                    })
                }
            }
        }
    </script>
    <style rel="stylesheet">
        #lbl_user_name,#lbl_user_gender{
            width: 65px;
        }
        .msg {
            opacity:0;
            position: absolute;
            top: 150px;
            left: 0;
            right: 0;
            bottom: 0;
            margin: auto;
            width: 230px;
            height: 70px;
            line-height: 70px;
            color: white;
            border-radius: 5px;
            text-align: center;
            background-color: rgba(0, 0, 0, 0.75);
            font-size: 16px;
            -moz-user-select: none;
            -webkit-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }
        .msg1 {
            opacity:0;
            position: absolute;
            top: 150px;
            left: 0;
            right: 0;
            bottom: 0;
            margin: auto;
            width: 230px;
            height: 70px;
            line-height: 70px;
            color: white;
            border-radius: 5px;
            text-align: center;
            background-color: rgba(0, 0, 0, 0.75);
            font-size: 16px;
            -moz-user-select: none;
            -webkit-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }
    </style>
</head>
<body>
<div class="frm_div text_info">
    <div class="frm_group">
        <label class="frm_label" id="lbl_user_name" for="input_user_name">用户名/昵称</label>
        <input class="frm_input" id="input_user_name" type="text" maxlength="50"/>
        <input class="frm_btn" id="btn_user_submit" type="button" value="查询"/>
        <input class="frm_btn frm_clear" id="btn_clear" type="button" value="重置"/>
    </div>
    <div class="frm_group">
        <label class="frm_label" id="lbl_user_gender" for="checkbox_user_gender_man">用户性别</label>
        <input class="frm_radio radio_gender" id="checkbox_user_gender_man" name="checkbox_user_gender" type="checkbox" value="0" checked>
        <label class="frm_label" id="lbl_user_gender_man" for="checkbox_user_gender_man">男</label>
        <input class="frm_radio radio_gender" id="checkbox_user_gender_woman" name="checkbox_user_gender" type="checkbox" value="1" checked>
        <label class="frm_label" id="lbl_user_gender_woman" for="checkbox_user_gender_woman">女</label>
    </div>
    <div class="frm_group_last">
        <%--<input class="frm_btn frm_add" id="btn_category_add" type="button" value="添加一个用户" onclick=""/>--%>
        <input class="frm_btn frm_refresh" id="btn_user_refresh" type="button" value="刷新用户列表"/>
        <input class="frm_btn frm_danger" id="btn_product_delete" type="button" value="删除选中用户" onclick="deleteUser()"/>
    </div>
</div>
<div class="data_count_div text_info">
    <svg class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2522" width="16"
         height="16">
        <path d="M401.976676 735.74897c-88.721671 0-172.124196-34.635845-234.843656-97.526197-62.724577-62.86784-97.271394-146.453537-97.271394-235.358379s34.546817-172.490539 97.276511-235.361449c62.715367-62.887282 146.117892-97.522104 234.838539-97.522104 88.719624 0 172.135452 34.633798 234.881518 97.522104 62.704111 62.875003 97.235578 146.4607 97.235578 235.361449 0 88.901773-34.530444 172.487469-97.231485 235.358379C574.112128 701.116195 490.6963 735.74897 401.976676 735.74897zM401.976676 121.204479c-75.012438 0-145.533584 29.290093-198.572568 82.474386-109.585861 109.834524-109.585861 288.539602-0.004093 398.36901 53.043077 53.188386 123.564223 82.47848 198.577684 82.47848 75.015507 0 145.553027-29.291117 198.620663-82.47848C710.126918 492.220514 710.126918 313.511343 600.593246 203.678866 547.530726 150.496619 476.992183 121.204479 401.976676 121.204479z"
              p-id="2523" fill="#FF7874">
        </path>
        <path d="M932.538427 958.228017c-6.565533 0-13.129019-2.508123-18.132986-7.52437L606.670661 642.206504c-9.989515-10.014074-9.969049-26.231431 0.045025-36.220946s26.230408-9.969049 36.220946 0.045025l307.73478 308.497143c9.989515 10.014074 9.969049 26.231431-0.045025 36.220946C945.627537 955.735244 939.081447 958.228017 932.538427 958.228017z"
              p-id="2524" fill="#FF7874">
        </path>
    </svg>
    <span class="data_count_title">查看合计</span>
    <span>用户总数:</span>
    <span class="data_count_value" id="user_count_data">${requestScope.userCount}</span>
    <span class="data_count_unit">位</span>
</div>
<div class="table_normal_div">
    <table class="table_normal" id="table_user_list">
        <thead class="text_info">
        <tr>
            <th><input type="checkbox" class="cbx_select" id="cbx_select_all"><label for="cbx_select_all"></label></th>
            <th class="data_info" data-sort="asc" data-name="user_name">
                <span>用户名</span>
                <span class="orderByDesc"></span>
                <span class="orderByAsc orderBySelect"></span>
            </th>
            <th class="data_info" data-sort="asc" data-name="user_nickname">
                <span>昵称</span>
                <span class="orderByDesc"></span>
                <span class="orderByAsc orderBySelect"></span>
            </th>
            <th class="data_info" data-sort="asc" data-name="user_realname">
                <span>姓名</span>
                <span class="orderByDesc"></span>
                <span class="orderByAsc orderBySelect"></span>
            </th>
            <th class="data_info" data-sort="asc" data-name="user_birthday">
                <span>出生日期</span>
                <span class="orderByDesc"></span>
                <span class="orderByAsc orderBySelect"></span>
            </th>
            <th class="data_info" data-sort="asc" data-name="user_gender">
                <span>性别</span>
                <span class="orderByDesc"></span>
                <span class="orderByAsc orderBySelect"></span>
            </th>
            <th>操作</th>
            <th hidden>用户ID</th>
        </tr>
        </thead>
        <tbody id="tbodyId">
        <c:forEach items="${requestScope.userList}" var="user">
            <tr>
                <td><input type="checkbox" class="cbx_select" value="${user.user_id}" id="cbx_user_select_${user.user_id}"><label for="cbx_user_select_${user.user_id}"></label></td>
                <td title="${user.user_name}">${user.user_name}</td>
                <td title="${user.user_nickname}">${user.user_nickname}</td>
                <td title="${user.user_realname}">${user.user_realname}</td>
                <td title="${user.user_birthday}">${user.user_birthday}</td>
                <td>
                    <c:choose>
                        <c:when test="${user.user_gender==0}">男</c:when>
                        <c:otherwise>女</c:otherwise>
                    </c:choose>
                </td>
                <td><span class="td_special" title="查看用户详情"><a href='javascript:void(0)'
                                                               onclick='getChildPage(this)'>详情</a></span></td>
                <td hidden class="user_id">${user.user_id}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <%@ include file="include/page.jsp" %>
    <div class="loader"></div>
</div>
<div class="msg">
    <span>删除成功</span>
</div>
<div class="msg1">
    <span>无效删除</span>
</div>
</body>
</html>
