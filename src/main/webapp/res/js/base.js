//Cookie工具
var cookieUtil = {
    //设置Cookie
    setCookie:
        function (name,value,days) {
            var date = new Date();
            date.setDate(date.getDate() + days);
            document.cookie = name + "=" + decodeURI(value) + ";expires=" + date.toUTCString();
            return this;
        },
    //获取Cookie
    getCookie:
        function (name) {
            var arr;
            var reg = new RegExp("(^| )" + name + "=([^;]*)($|;)");
            if (arr = document.cookie.match(reg)) {
                return decodeURIComponent(arr[2]);
            }
            return null;
        },
    //移除Cookie
    removeCookie:
        function (name) {
            cookieUtil.setCookie(name, "", -1);
            return this;
        }
};
//样式统一工具
var styleUtil = {
    //显示表单验证错误提示
    errorShow:
        function (obj,text) {
            if (obj !== undefined) {
                obj.text(text).attr("title", text);
                if (obj.css("opacity") !== "1") {
                    obj.animate({
                        left: "0",
                        opacity: 1
                    }, 200);
                } else {
                    obj
                        .css("opacity", "0.5")
                        .animate({
                            opacity: 1
                        }, 100);
                }
                return this;
            }
        },
    //隐藏表单验证错误提示
    errorHide:
        function (obj) {
            if (obj !== undefined) {
                if (obj.css("opacity") !== "0") {
                    obj.animate({
                        left: "20px",
                        opacity: 0
                    }, 200);
                }
                return this;
            }
        },
    //显示基础的表单验证错误提示
    basicErrorShow:
        function (obj) {
            obj
                .css("color","#c33")
                .css("opacity", "0.5")
                .animate({
                    opacity: 1
            }, 100)
                .next("input,textarea")
                .css("border-color", "#c33");
            return this;
        },
    //显示一种特殊的基础表单验证错误提示
    specialBasicErrorShow:
        function (obj) {
            obj
                .css("color", "#c33")
                .css("opacity", "0.5")
                .animate({
                    opacity: 1
                }, 100)
                .next("span").next("input,textarea")
                .css("border-color", "#c33");
            return this;
        },
    //隐藏基础的表单验证错误提示
    basicErrorHide:
        function (obj) {
            obj
                .css("color","#666")
                .css("opacity","1")
                .next("input,textarea")
                .css("border-color", "");
            return this;
        },
    //隐藏一种特殊的基础表单验证错误提示
    specialBasicErrorHide:
        function (obj) {
            obj
                .css("color", "#333")
                .css("opacity", "1")
                .next("span")
                .next("input,textarea")
                .css("border-color","");
            return this;
        }
};
//页面和数据交互工具
var ajaxUtil = {
    getPage:
        function (url,data,isChild) {
            if(url !== null && url !== ""){
                $.ajax({
                    url: "/mall/admin/"+url,
                    type: "get",
                    data: data,
                    contentType: "text/html;charset=UTF-8",
                    success : function (data) {
                        $("#div_home_context_main").html(data);
                        window.scrollTo(0, 0);
                        if(!isChild){
                            /******
                             * event
                             ******/
                            //获得表单元素焦点时
                            $(".frm_input,.frm_radio").focus(function () {
                                $(".frm_error_msg").each(function () {
                                    styleUtil.errorHide($(this));
                                });
                            });
                            //点击table中的全选框时
                            $("#cbx_select_all").click(function () {
                                if($(this).prop("checked")){
                                    $("td>.cbx_select").prop("checked",true);
                                } else {
                                    $("td>.cbx_select").prop("checked",false);
                                }
                                styleUtil.errorHide($("#text_tools_msg"));
                            });
                            //点击table中的选框时
                            $("td>.cbx_select").click(function () {
                                styleUtil.errorHide($("#text_tools_msg"));
                            });
                            //点击重置按钮时
                            $('#btn_clear').click(function () {
                                $(".frm_div :input")
                                    .not(':button',':submit',':reset','hidden')
                                    .val('')
                                    .prop("checked",true);
                                //刷新下拉框
                                $('.selectpicker').selectpicker('refresh');
                            });
                        } else {
                            //清除已选中的li样式
                            $(".menu_li_select").removeClass("menu_li_select");
                        }
                    },
                    beforeSend: function () {
                        $(".loader").css("display","block");
                    },
                    error: function () {

                    }
                });
                console.debug(url);
            }
        }
};