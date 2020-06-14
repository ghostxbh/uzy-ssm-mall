<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <script>
        $(function () {
            if ($("#details_category_id").val() === "") {
                /******
                 * event
                 ******/
                //单击保存按钮时
                $("#btn_category_save").click(function () {
                    var category_name = $.trim($("#input_category_name").val());
                    var category_image_src = $.trim($("#pic_category").attr("src"));

                    //校验数据合法性
                    var yn = true;
                   /* if (category_image_src === "" || category_image_src === undefined) {
                        yn = false;
                        $("#btn-ok").unbind("click").click(function () {
                            $("#modalDiv").modal("hide");
                        });
                        $(".modal-body").text("请上传分类图片！");
                        $('#modalDiv').modal();
                    }*/
                    if (category_name === "") {
                        styleUtil.basicErrorShow($("#lbl_category_name"));
                        yn = false;
                    }
                    if (!yn) {
                        return;
                    }

                    var dataList = {
                        "category_name": category_name,
                        "category_image_src": category_image_src
                    };
                    doAction(dataList, "admin/category", "POST");
                });
            } else {
                //设置分类编号
                $("#span_category_id").text('${requestScope.category.category_id}');
                //判断文件是否允许上传
                if ($("#pic_category").attr("src") === undefined) {
                    $(".details_picList_fileUpload").css("display", "inline-block");
                } else {
                    $(".details_picList_fileUpload").css("display", "none");
                }
                //单击保存按钮时
                $("#btn_category_save").click(function () {
                    var category_id = $("#details_category_id").val();
                    var category_name = $.trim($("#input_category_name").val());
                    var category_image_src = $.trim($("#pic_category").attr("src"));

                    //校验数据合法性
                    var yn = true;
                    /*if (category_image_src === "") {
                        yn = false;
                        $("#btn-ok").unbind("click").click(function () {
                            $("#modalDiv").modal("hide");
                        });
                        $(".modal-body").text("请上传分类图片！");
                        $('#modalDiv').modal();
                    }*/
                    if (category_name === "") {
                        styleUtil.basicErrorShow($("#lbl_category_name"));
                        yn = false;
                    }
                    if (!yn) {
                        return;
                    }

                    var dataList = {
                        "category_name": category_name,
                        "category_image_src": category_image_src
                    };
                    doAction(dataList, "admin/category/" + category_id, "PUT");
                });
            }

            /******
             * event
             ******/
            //单击图片列表项时
            $(".details_picList").on("click", "li:not(.details_picList_fileUpload)", function () {
                var img = $(this);
                var fileUploadInput = $(this).parents("ul").children(".details_picList_fileUpload");
                $("#btn-ok").unbind("click").click(function () {
                    img.remove();
                    fileUploadInput.css("display", "inline-block");
                    $('#modalDiv').modal("hide");
                });
                $(".modal-body").text("您确定要删除该分类图片吗？");
                $('#modalDiv').modal();
            });
            //单击取消按钮时
            $("#btn_category_cancel").click(function () {
                $(".menu_li[data-toggle=category]").click();
            });
            //获取到输入框焦点时
            $("input:text").focus(function () {
                styleUtil.basicErrorHide($(this).prev("label"));
            });
        });

        //图片上传
        function uploadImage(fileDom) {
            //获取文件
            var file = fileDom.files[0];
            //判断类型
            var imageType = /^image\//;
            if (file === undefined || !imageType.test(file.type)) {
                $("#btn-ok").unbind("click").click(function () {
                    $("#modalDiv").modal("hide");
                });
                $(".modal-body").text("请选择图片！");
                $('#modalDiv').modal();
                return;
            }
            //判断大小
            if (file.size > 512000) {
                $("#btn-ok").unbind("click").click(function () {
                    $("#modalDiv").modal("hide");
                });
                $(".modal-body").text("图片大小不能超过500K！");
                $('#modalDiv').modal();
                return;
            }
            //清空值
            $(fileDom).val('');
            var formData = new FormData();
            formData.append("file", file);
            //上传图片
            $.ajax({
                url: "/mall/admin/uploadCategoryImage",
                type: "post",
                data: formData,
                contentType: false,
                processData: false,
                dataType: "json",
                mimeType: "multipart/form-data",
                success: function (data) {
                    $(fileDom).attr("disabled", false).prev("span").text("上传图片");
                    if (data.success) {
                        $(fileDom).parent('.details_picList_fileUpload').before("<li><img src='${pageContext.request.contextPath}/res/images/store/" + data.fileName + "' id='pic_category'  width='1190px' height='150px'/></li>").css("display", "none");
                    } else {
                        alert("图片上传异常！");
                    }
                },
                beforeSend: function () {
                    $(fileDom).attr("disabled", true).prev("span").text("图片上传中...");
                },
                error: function () {

                }
            });
        }

        //分类操作
        function doAction(dataList, url, type) {
            $.ajax({
                url: url,
                type: type,
                data: dataList,
                traditional: true,
                success: function (data) {
                    $("#btn_category_save").attr("disabled", false).val("保存");
                    if (data.success) {
                        $("#btn-ok,#btn-close").unbind("click").click(function () {
                            $('#modalDiv').modal("hide");
                            setTimeout(function () {
                                //ajax请求页面
                                ajaxUtil.getPage("category/" + data.category_id, null, true);
                            }, 170);
                        });
                        $(".modal-body").text("保存成功！");
                        $('#modalDiv').modal();
                    }
                },
                beforeSend: function () {
                    $("#btn_product_save").attr("disabled", true).val("保存中...");
                },
                error: function () {

                }
            });
        }
    </script>
    <style rel="stylesheet">
        .details_property_list {

        }

        .details_property_list > li {
            list-style: none;
            padding: 5px 0;
        }

        div.br {
            height: 20px;
        }
    </style>
</head>
<body>
<div class="details_div_first">
    <input type="hidden" value="${requestScope.category.category_id}" id="details_category_id"/>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_category_id">分类编号</label>
        <span class="details_value" id="span_category_id">系统指定</span>
    </div>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_category_name" for="input_category_name">分类名称</label>
        <input class="frm_input" id="input_category_name" type="text" maxlength="50"
               value="${requestScope.category.category_name}"/>
    </div>
</div>
<div class="details_div">
    <span class="details_title text_info">分类图片</span>
    <ul class="details_picList" id="category_list">
        <c:if test="${requestScope.category.category_image_src != null}">
            <li><img src="${requestScope.category.category_image_src}"
                    id="pic_category" width="300px" height="435px"/></li>
        </c:if>
        <li class="details_picList_fileUpload">
            <svg class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1528"
                 width="40" height="40">
                <path d="M0 512C0 229.230208 229.805588 0 512 0 794.769792 0 1024 229.805588 1024 512 1024 794.769792 794.194412 1024 512 1024 229.230208 1024 0 794.194412 0 512Z"
                      p-id="1529" fill="#FF7874"></path>
                <path d="M753.301333 490.666667l-219.946667 0L533.354667 270.741333c0-11.776-9.557333-21.333333-21.354667-21.333333-11.776 0-21.333333 9.536-21.333333 21.333333L490.666667 490.666667 270.72 490.666667c-11.776 0-21.333333 9.557333-21.333333 21.333333 0 11.797333 9.557333 21.354667 21.333333 21.354667L490.666667 533.354667l0 219.904c0 11.861333 9.536 21.376 21.333333 21.376 11.797333 0 21.354667-9.578667 21.354667-21.333333l0-219.946667 219.946667 0c11.754667 0 21.333333-9.557333 21.333333-21.354667C774.634667 500.224 765.077333 490.666667 753.301333 490.666667z"
                      p-id="1530" fill="#FFFFFF"></path>
            </svg>
            <span>点击上传</span>
            <input type="file" onchange="uploadImage(this)" accept="image/*">
        </li>
    </ul>
    <span class="frm_error_msg" id="text_category_image_details_msg"></span>
</div>
<div class="details_div details_div_last">
    <c:if test="${fn:length(requestScope.category.propertyList)!=0}">
        <span class="details_title text_info">属性列表</span>
        <c:forEach items="${requestScope.category.propertyList}" var="property" varStatus="status">
            <c:choose>
                <c:when test="${status.index % 2 == 0}">
                    <input class="frm_input" id="input_category_property_${property.property_id}" type="text"
                           maxlength="50" value="${property.property_name}"
                           data-pvid="${property.property_id}"/>
                </c:when>
                <c:otherwise>
                    <input class="frm_input" id="input_category_property_${property.property_id}" type="text"
                           maxlength="50" value="${property.property_name}"
                           data-pvid="${property.property_id}"/>
                    <div class="br"></div>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </c:if>
</div>
<div class="details_tools_div">
    <input class="frm_btn" id="btn_category_save" type="button" value="保存"/>
    <input class="frm_btn frm_clear" id="btn_category_cancel" type="button" value="取消"/>
</div>

<%-- 模态框 --%>
<div class="modal fade" id="modalDiv" tabindex="-1" role="dialog" aria-labelledby="modalDiv" aria-hidden="true"
     data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="myModalLabel">提示</h4>
            </div>
            <div class="modal-body">您确定要删除分类图片吗？</div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-primary" id="btn-ok">确定</button>
                <button type="button" class="btn btn-default" data-dismiss="modal" id="btn-close">关闭</button>
            </div>
        </div>
        <%-- /.modal-content --%>
    </div>
    <%-- /.modal --%>
</div>
<div class="loader"></div>
</body>
</html>
