<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <script>
        //检索数据集
        var dataList = {
            "category_name": null
        };
        $(function () {
            /******
             * event
             * *****/
            //点击查询按钮时
            $("#btn_category_submit").click(function () {
                var category_name = $.trim($("#input_category_name").val());
                //封装数据
                dataList.category_name = encodeURI(category_name);

                getData($(this), "admin/category/0/10", dataList);
            });
            //点击刷新按钮时
            $("#btn_category_refresh").click(function () {
                //清除数据
                dataList.category_name = null;
                //获取数据
                getData($(this), "admin/category/0/10", null);
            });
            //点击table中的数据时
            $("#table_category_list").find(">tbody>tr").click(function () {
                trDataStyle($(this));
            });
        });
        //获取分类数据
        function getData(object, url, dataObject) {
            var table = $("#table_category_list");
            var tbody = table.children("tbody").first();
            $.ajax({
                url: url,
                type: "get",
                data: dataObject,
                success: function (data) {
                    //清空原有数据
                    tbody.empty();
                    //设置样式
                    $(".loader").css("display","none");
                    object.attr("disabled",false);
                    //显示分类统计数据
                    $("#category_count_data").text(data.categoryCount);
                    if(data.categoryList.length > 0) {
                        for (var i = 0; i < data.categoryList.length; i++) {
                            var category_id = data.categoryList[i].category_id;
                            var category_name = data.categoryList[i].category_name;
                            //显示分类数据
                            tbody.append("<tr><td><input type='checkbox' class='cbx_select' value='"+category_id+"' id='cbx_category_select_" + category_id + "'><label for='cbx_category_select_" + category_id + "'></label></td><td title='" + category_name + "'>" + category_name + "</td><td><span class='td_special' title='查看分类详情'><a href='javascript:void(0)' onclick='getChildPage(this)'>详情</a></span></td><td hidden class='category_id'>" + category_id + "</td></tr>");
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
                    $(".loader").css("display", "block");
                    object.attr("disabled",true);
                },
                error: function () {

                }
            });
        }

        // 获取产品分类子界面
        function getChildPage(obj) {
            var url;
            var title;
            if (obj === null) {
                title = "添加分类";
                url = "category/new";
            } else {
                title = "分类详情";
                url = "category/" + $(obj).parents("tr").find(".category_id").text();
            }

            //设置样式
            $("#div_home_title").children("span").text(title);
            document.title = "柚子云购 - " + title;
            //ajax请求页面
            ajaxUtil.getPage(url, null, true);
        }

        //获取页码数据
        function getPage(index) {
            getData($(this), "admin/category/" + index + "/10", dataList);
        }

        //删除分类
        function deleteCategory() {
            var arr = new Array();
            $("#tbodyId input[type='checkbox']:checked").each(function (index, item) {
                arr.push($(this).val());
            });
            console.log(arr);
            if (arr === undefined || arr.length == 0) {
                $(".msg1").stop(true, true).animate({
                    opacity: 1
                }, 550, function () {
                    $(".msg1").animate({
                        opacity: 0
                    }, 1500);
                });
            } else {
                if (window.confirm("确认删除？")) {
                    $.ajax({
                        url: "admin/category/delete/" + arr,
                        type: "get",
                        date: {"array": arr},
                        traditional: true,
                        success: function (data) {
                            if (data.success) {
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
    <style>
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
        <label class="frm_label" id="lbl_category_name" for="input_category_name">分类名称</label>
        <input class="frm_input" id="input_category_name" type="text" maxlength="50"/>
        <input class="frm_btn" id="btn_category_submit" type="button" value="查询"/>
        <input class="frm_btn frm_clear" id="btn_clear" type="button" value="重置"/>
    </div>
    <div class="frm_group_last">
        <input class="frm_btn frm_add" id="btn_category_add" type="button" value="添加一个分类" onclick="getChildPage(null)"/>
        <input class="frm_btn frm_refresh" id="btn_category_refresh" type="button" value="刷新分类列表"/>
        <input class="frm_btn frm_danger" id="btn_product_delete" type="button" value="删除选中分类" onclick="deleteCategory()"/>
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
    <span>分类总数:</span>
    <span class="data_count_value" id="category_count_data">${requestScope.categoryCount}</span>
    <span class="data_count_unit">个</span>
</div>
<div class="table_normal_div">
    <table class="table_normal" id="table_category_list">
        <thead class="text_info">
        <tr>
            <th><input type="checkbox" class="cbx_select" id="cbx_select_all"><label for="cbx_select_all"></label></th>
            <th>分类名称</th>
            <th>操作</th>
            <th hidden class="category_id">分类ID</th>
        </tr>
        </thead>
        <tbody id="tbodyId">
        <c:forEach items="${requestScope.categoryList}" var="category">
            <tr>
                <td><input type="checkbox" class="cbx_select" value="${category.category_id}" id="cbx_category_select_${category.category_id}"><label for="cbx_category_select_${category.category_id}"></label></td>
                <td title="${category.category_name}">${category.category_name}</td>
                <td><span class="td_special" title="查看分类详情"><a href="javascript:void(0)"
                                                               onclick="getChildPage(this)">详情</a></span></td>
                <td hidden><span class="category_id">${category.category_id}</span></td>
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
