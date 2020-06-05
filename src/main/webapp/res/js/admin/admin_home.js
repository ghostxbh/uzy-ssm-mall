$(function () {
    /******
     * event
     ******/
    //点击导航栏li时
    $(".menu_li").click(function () {
        var url = $(this).attr("data-toggle");
        //如果已选择li则退出
        if($(this).hasClass("menu_li_select")){
            return;
        } else {
            //清除已选中的li样式
            $(".menu_li_select").removeClass("menu_li_select");
            //设置当前li样式
            $(this).addClass("menu_li_select");
        }
        //ajax请求页面
        ajaxUtil.getPage(url,null,false);
        //设置文本
        var title = $(this).children("span").text();
        $("#div_home_title").children("span").text(title);
        document.title = "柚子云购 - "+title;
    });
    //点击用户昵称或下拉箭头时
    $("#txt_home_nickname,#i_nickname_slide").click(function () {
        $("#nav_tools").slideToggle();
    });
    //点击导航工具选项时
    $("#nav_tools_admin_manage").click(function () {
        $("#nav_tools").hide();
        $(".menu_li[data-toggle=account]").click();
    });
    $("#nav_tools_admin_logout").click(function () {
        location.href = "/mall/admin/account/logout";
    });
});
//tbody中tr的单击样式
function trDataStyle(obj) {
    var checkbox = obj.find(".cbx_select").first();
    if(checkbox.prop("checked")){
        checkbox.prop("checked",false);
    } else {
        checkbox.prop("checked",true);
    }
}

//生成PageDIV
function createPageDiv(obj, pageUtil) {
    $("#pageDiv").detach();
    obj.before("<div id='pageDiv'></div>");
    var pageDiv = $("#pageDiv");

    pageDiv.append("<ul><li data-name='firstPage'><a href='javascript:void(0)' onclick='getPage(0)' aria-label='首页'><span aria-hidden='true'>&laquo;</span></a></li><li data-name='prevPage'><a href='javascript:void(0)' onclick='getPage(" + (pageUtil.index - 1) + ")' aria-label='上一页'><span aria-hidden='true'>&lsaquo;</span></a></li></ul>");
    var pageDivUl = $("#pageDiv>ul");
    for (var i = 1; i <= pageUtil.totalPage; i++) {
        if (i - pageUtil.index >= -5 && i - pageUtil.index <= 5) {
            if (i === pageUtil.index + 1) {
                pageDivUl.append("<li class='pageThis'><a href='javascript:void(0)'>" + i + "</a></li>");
            } else {
                pageDivUl.append("<li><a href='javascript:void(0)' onclick='getPage(" + (i - 1) + ")'>" + i + "</a></li>");
            }
        }
    }
    pageDivUl.append("<li data-name='nextPage'><a href='javascript:void(0)' onclick='getPage(" + (pageUtil.index + 1) + ")' aria-label='下一页'><span aria-hidden='true'>&rsaquo;</span></a></li><li data-name='lastPage'><a href='javascript:void(0)' onclick='getPage(" + (pageUtil.totalPage - 1) + ")' aria-label='尾页'><span aria-hidden='true'>&raquo;</span></a></li>");
    if (pageUtil.index <= 0) {
        $("#pageDiv li[data-name='firstPage'],#pageDiv li[data-name='prevPage']").addClass("disabled").children("a").attr("onclick", null);
    }
    if (pageUtil.index + 1 >= pageUtil.totalPage) {
        $("#pageDiv li[data-name='nextPage'],#pageDiv li[data-name='lastPage']").addClass("disabled").children("a").attr("onclick", null);
    }
}