<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <script>
        $(function () {
            if($("#details_product_id").val() === ""){
                //刷新下拉框
                $('#select_product_category').selectpicker('refresh');

                /******
                 * event
                 ******/
                //单击保存按钮时
                $("#btn_product_save").click(function () {
                    var product_category_id = $("#select_product_category").selectpicker("val");
                    var product_isEnabled = $("input[name='radio_product_isEnabled']:checked").val();
                    var product_name = $.trim($("#input_product_name").val());
                    var product_title = $.trim($("#input_product_title").val());
                    var product_price = $.trim($("#input_product_price").val());
                    var product_sale_price = $.trim($("#input_product_sale_price").val());

                    //校验数据合法性
                    var yn = true;
                    if(product_isEnabled === undefined){
                        styleUtil.errorShow($("#text_productState_details_msg"),"请选择产品状态！");
                        yn = false;
                    }
                    if(product_name === ""){
                        styleUtil.basicErrorShow($("#lbl_product_name"));
                        yn = false;
                    }
                    if(product_title === ""){
                        styleUtil.basicErrorShow($("#lbl_product_title"));
                        yn = false;
                    }
                    if(product_price === "" || isNaN(product_price)){
                        styleUtil.basicErrorShow($("#lbl_product_price"));
                        yn = false;
                    }
                    if(product_sale_price === "" || isNaN(product_sale_price)){
                        styleUtil.basicErrorShow($("#lbl_product_sale_price"));
                        yn = false;
                    }
                    if(!yn){
                        return;
                    }

                    //产品属性Map
                    var propertyMap = {};
                    $("input[id^='input_product_property']").each(function () {
                        var value = $.trim($(this).val());
                        if (value === "") {
                            return true;
                        }
                        var key = $(this).attr("id").substring($(this).attr("id").lastIndexOf('_') + 1);
                        propertyMap[key] = value;
                    });

                    //产品图片List
                    var productSingleImageList = [];
                    $("#product_single_list").children("li:not(.details_picList_fileUpload)").each(function () {
                        var img = $(this).children("img");
                        if (img.attr("name") === "new") {
                            productSingleImageList.push(img.attr("src"));
                        }
                    });
                    var productDetailsImageList = [];
                    $("#product_details_list").children("li:not(.details_picList_fileUpload)").each(function () {
                        var img = $(this).children("img");
                        productDetailsImageList.push(img.attr("src"));
                        // if (img.attr("name") === "new") {
                        // }
                    });

                    //数据集
                    var dataList = {
                        "product_category_id": product_category_id,
                        "product_isEnabled" : product_isEnabled,
                        "product_name": product_name,
                        "product_title": product_title,
                        "product_price": product_price,
                        "product_sale_price": product_sale_price,
                        "propertyJson": JSON.stringify(propertyMap),
                        "productSingleImageList": productSingleImageList,
                        "productDetailsImageList": productDetailsImageList
                    };
                    doAction(dataList, "admin/product", "POST");
                });
            } else {
                //设置产品种类值
                $('#select_product_category').selectpicker('val','${requestScope.product.product_category.category_id}');
                //设置产品状态
                var product_isEnabled = '${requestScope.product.product_isEnabled}';
                $("input[name='radio_product_isEnabled']").each(function () {
                    if($(this).val() === product_isEnabled){
                        $(this).prop("checked",true);
                        if($(this).val() === "1"){
                            $("#text_productState_details_msg").text("提示：产品停售时无法进行交易").attr("title","提示：产品停售时无法进行交易").css("left",0).css("opacity","1");
                        }
                        return false;
                    }
                });
                //设置产品编号
                $("#span_product_id").text('${requestScope.product.product_id}');
                //设置产品创建日期
                $("#span_product_create_date").text('${requestScope.product.product_create_date}');
                //判断文件是否允许上传
                checkFileUpload($("#product_single_list"),5);
                checkFileUpload($("#product_details_list"),8);
                //原属性值Map
                var propertyMap = {};
                $("input[id^='input_product_property'][data-pvid]").each(function () {
                    var value_id = $(this).attr("data-pvid");
                    propertyMap[value_id] = $(this).val();
                });

                /******
                 * event
                 ******/
                //单击保存按钮时
                $("#btn_product_save").click(function () {
                    var product_id = $("#details_product_id").val();
                    var product_category_id = $("#select_product_category").selectpicker("val");
                    var product_isEnabled = $("input[name='radio_product_isEnabled']:checked").val();
                    var product_name = $.trim($("#input_product_name").val());
                    var product_title = $.trim($("#input_product_title").val());
                    var product_price = $.trim($("#input_product_price").val());
                    var product_sale_price = $.trim($("#input_product_sale_price").val());

                    //校验数据合法性
                    var yn = true;
                    if (product_isEnabled === undefined) {
                        styleUtil.errorShow($("#text_productState_details_msg"), "请选择产品状态！");
                        yn = false;
                    }
                    if (product_name === "") {
                        styleUtil.basicErrorShow($("#lbl_product_name"));
                        yn = false;
                    }
                    if (product_title === "") {
                        styleUtil.basicErrorShow($("#lbl_product_title"));
                        yn = false;
                    }
                    if (product_price === "" || isNaN(product_price)) {
                        styleUtil.basicErrorShow($("#lbl_product_price"));
                        yn = false;
                    }
                    if (product_sale_price === "" || isNaN(product_sale_price)) {
                        styleUtil.basicErrorShow($("#lbl_product_sale_price"));
                        yn = false;
                    }
                    if (!yn) {
                        return;
                    }

                    //产品属性Map
                    var propertyAddMap = {};
                    var propertyUpdateMap = {};
                    var propertyDeleteList = [];
                    //获取需要更新或删除的产品属性
                    $("input[id^=input_product_property][data-pvid]").each(function () {
                        var value_id = $(this).attr("data-pvid");
                        var value = $.trim($(this).val());
                        if (value === "") {
                            propertyDeleteList.push(value_id);
                        } else if (propertyMap[value_id] !== value) {
                            propertyUpdateMap[value_id] = value;
                        }
                    });
                    //获取需要添加的产品属性
                    $("input[id^=input_product_property]:not([data-pvid])").each(function () {
                        var value = $.trim($(this).val());
                        if (value === "") {
                            return true;
                        } else {
                            var key = $(this).attr("id").substring($(this).attr("id").lastIndexOf('_') + 1);
                            propertyAddMap[key] = value;
                        }
                    });

                    //产品图片List
                    var productSingleImageList = [];
                    $("#product_single_list").children("li:not(.details_picList_fileUpload)").each(function () {
                        var img = $(this).children("img");
                        if (img.attr("name") === "new") {
                            productSingleImageList.push(img.attr("src"));
                        }
                    });
                    var productDetailsImageList = [];
                    $("#product_details_list").children("li:not(.details_picList_fileUpload)").each(function () {
                        var img = $(this).children("img");
                        if (img.attr("name") === "new") {
                            productDetailsImageList.push(img.attr("src"));
                        }
                    });

                    //数据集
                    var dataList = {
                        "product_category_id": product_category_id,
                        "product_isEnabled": product_isEnabled,
                        "product_name": product_name,
                        "product_title": product_title,
                        "product_price": product_price,
                        "product_sale_price": product_sale_price,
                        "propertyAddJson": JSON.stringify(propertyAddMap),
                        "propertyUpdateJson": JSON.stringify(propertyUpdateMap),
                        "propertyDeleteList": propertyDeleteList,
                        "productSingleImageList": productSingleImageList,
                        "productDetailsImageList": productDetailsImageList
                    };
                    doAction(dataList, "admin/product/" + product_id, "PUT");
                });
            }

            /******
             * event
             ******/
            //单击图片列表项时
            $(".details_picList").on("click","li:not(.details_picList_fileUpload)",function () {
                var img = $(this);
                var productImage_id = img.children("img").attr("name");
                var fileUploadInput = $(this).parents("ul").children(".details_picList_fileUpload");
                if (productImage_id === "new") {
                    $("#btn-ok").unbind("click").click(function () {
                        img.remove();
                        fileUploadInput.css("display", "inline-block");
                        $('#modalDiv').modal("hide");
                    });
                } else {
                    $("#btn-ok").unbind("click").click(function () {
                        $.ajax({
                            url: "/mall/admin/productImage/" + productImage_id,
                            type: "delete",
                            data: null,
                            success: function (data) {
                                $("#btn-ok").attr("disabled", false).text("确定");
                                if (data.success) {
                                    img.remove();
                                    fileUploadInput.css("display", "inline-block");
                                    $('#modalDiv').modal("hide");
                                } else {
                                    $('#modalDiv').modal("hide");
                                    alert("图片删除异常！");
                                }
                            },
                            beforeSend: function () {
                                $("#btn-ok").attr("disabled", true).text("操作中...");
                            },
                            error: function () {

                            }
                        });
                    });
                }
                $(".modal-body").text("您确定要删除该产品图片吗？");
                $('#modalDiv').modal();
            });
            //改变产品状态时
            $('input:radio').click(function () {
                if($(this).val() === "1"){
                    styleUtil.errorShow($("#text_productState_details_msg"),"提示：产品停售时无法进行交易");
                } else {
                    styleUtil.errorHide($("#text_productState_details_msg"));
                }
            });
            //单击取消按钮时
            $("#btn_product_cancel").click(function () {
                $(".menu_li[data-toggle=product]").click();
            });
            //更改产品类型列表时
            $("#select_product_category").change(function () {
                $.ajax({
                    url: "admin/property/type/"+$(this).val(),
                    type: "get",
                    data: null,
                    success: function (data) {
                        $(".loader").css("display", "none");
                        //清空原有数据
                        var listDiv = $(".details_property_list");
                        listDiv.empty().append("<span class='details_title text_info'>属性值信息</span>");
                        //显示产品属性数据
                        if(data.propertyList.length > 0){
                            for(var i = 0;i<data.propertyList.length;i++){
                                var propertyId = data.propertyList[i].property_id;
                                var propertyName = data.propertyList[i].property_name;
                                if(data.propertyList[i+1] !== undefined){
                                    var nextPropertyId = data.propertyList[i+1].property_id;
                                    var nextPropertyName = data.propertyList[i+1].property_name;
                                    i++;
                                    listDiv.append("<label class='frm_label lbl_property_name text_info' id='lbl_product_property_" + propertyId + "' for='input_product_property_" + propertyId + "'>" + propertyName + "</label><input class='frm_input' id='input_product_property_" + propertyId + "' type='text' maxlength='50'/><label class='frm_label lbl_property_name text_info' id='lbl_product_property_" + nextPropertyId + "' for='input_product_property_" + nextPropertyId + "'>" + nextPropertyName + "</label><input class='frm_input' id='input_product_property_" + nextPropertyId + "' type='text' maxlength='50'/><div class='br'></div>");
                                } else {
                                    listDiv.append("<label class='frm_label lbl_property_name text_info' id='lbl_product_property_" + propertyId + "' for='input_product_property_" + propertyId + "'>" + propertyName + "</label><input class='frm_input' id='input_product_property_" + propertyId + "' type='text' maxlength='50'/><div class='br'></div>");
                                }
                            }
                        }
                    },
                    beforeSend: function () {
                        $(".loader").css("display", "block");
                    },
                    error: function () {

                    }
                });
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
            if (file.size > 10240000) {
                $("#btn-ok").unbind("click").click(function () {
                    $("#modalDiv").modal("hide");
                });
                $(".modal-body").text("图片大小不能超过1M！");
                $('#modalDiv').modal();
                return;
            }
            var ul = $(fileDom).parents(".details_picList");
            var type;
            if (ul.attr("id") === "product_single_list") {
                type = "single";
            } else {
                type = "details";
            }
            //清空值
            $(fileDom).val('');
            var formData = new FormData();
            formData.append("file", file);
            formData.append("imageType", type);
            //上传图片
            $.ajax({
                url: "/mall/admin/uploadProductImage",
                type: "post",
                data: formData,
                contentType: false,
                processData: false,
                dataType: "json",
                mimeType: "multipart/form-data",
                success: function (data) {
                    $(fileDom).attr("disabled", false).prev("span").text("上传图片");
                    if (data.success) {
                        if (type === "single") {
                            $(fileDom).parent('.details_picList_fileUpload').before("<li><img src='" + data.fileUrl + "' width='128px' height='128px' name='new'/></li>");
                            checkFileUpload(ul, 5);
                        } else {
                            $(fileDom).parent('.details_picList_fileUpload').before("<li><img src='" + data.fileUrl + "' width='128px' height='128px' name='new'/></li>");
                            checkFileUpload(ul, 8);
                        }
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

        //判断是否允许上传文件
        function checkFileUpload(obj, size) {
            if(obj.children("li:not(.details_picList_fileUpload,:hidden)").length>=size){
                obj.children(".details_picList_fileUpload").css("display","none");
            } else {
                obj.children(".details_picList_fileUpload").css("display","inline-block");
            }
        }

        //产品操作
        function doAction(dataList, url, type) {
            $.ajax({
                url: url,
                type: type,
                data: dataList,
                traditional: true,
                success: function (data) {
                    $("#btn_product_save").attr("disabled", false).val("保存");
                    if (data.success) {
                        $("#btn-ok,#btn-close").unbind("click").click(function () {
                            $('#modalDiv').modal("hide");
                            setTimeout(function () {
                                //ajax请求页面
                                ajaxUtil.getPage("product/" + data.product_id, null, true);
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
        .bootstrap-select:not([class*=col-]):not([class*=form-control]):not(.input-group-btn){
            margin: 0 130px 0 0;
        }
        .frm_input{
            margin-right: 130px;
        }
        .frm_error_msg{
            white-space:nowrap;
        }
        .warn_height{
            max-height: 25px;
        }

        div.br {
            height: 20px;
        }

        .details_property_list label {
            margin-left: 10px;
        }
    </style>
</head>
<body>
<div class="details_div_first">
    <input type="hidden" value="${requestScope.product.product_id}" id="details_product_id"/>
    <div class="frm_div_last warn_height">
        <label class="frm_label text_info" id="lbl_product_category_id" for="select_product_category">产品类型</label>
        <select class="selectpicker" id="select_product_category" data-size="8">
            <c:forEach items="${requestScope.categoryList}" var="category">
                <option value="${category.category_id}">${category.category_name}</option>
            </c:forEach>
        </select>
        <label class="frm_label text_info" id="lbl_product_isEnabled" for="radio_product_isEnabled_true">产品状态</label>
        <input id="radio_product_isEnabled_true" name="radio_product_isEnabled" type="radio" value="0" checked>
        <label class="frm_label text_info" id="lbl_product_isEnabled_true" for="radio_product_isEnabled_true">销售中</label>
        <input id="radio_product_isEnabled_false" name="radio_product_isEnabled" type="radio" value="1">
        <label class="frm_label text_info" id="lbl_product_isEnabled_false" for="radio_product_isEnabled_false">停售中</label>
        <input id="radio_product_isEnabled_special" name="radio_product_isEnabled" type="radio" value="2">
        <label class="frm_label text_info" id="lbl_product_isEnabled_special" for="radio_product_isEnabled_special">促销中</label>
        <span class="frm_error_msg" id="text_productState_details_msg"></span>
    </div>
</div>
<div class="details_div">
    <span class="details_title text_info">基本信息</span>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_product_id">产品编号</label>
        <span class="details_value" id="span_product_id">系统指定</span>
        <label class="frm_label text_info" id="lbl_product_create_date">上架日期</label>
        <span class="details_value" id="span_product_create_date">系统指定</span>
    </div>
    <div class="frm_div">
        <label class="frm_label text_info" id="lbl_product_name" for="input_product_name">产品名称</label>
        <input class="frm_input" id="input_product_name" type="text" maxlength="50" value="${requestScope.product.product_name}"/>
        <label class="frm_label text_info" id="lbl_product_title" for="input_product_title">产品标题</label>
        <input class="frm_input" id="input_product_title" type="text" maxlength="50" value="${requestScope.product.product_title}"/>
    </div>
    <div class="frm_div_last">
        <label class="frm_label text_info" id="lbl_product_price" for="input_product_price">产品原价</label>
        <input class="frm_input details_unit"  id="input_product_price" type="text" maxlength="10" value="${requestScope.product.product_price}"/>
        <span class="details_unit text_info">元</span>
        <label class="frm_label text_info" id="lbl_product_sale_price" for="input_product_sale_price">产品促销价</label>
        <input class="frm_input details_unit"  id="input_product_sale_price" type="text" maxlength="10" value="${requestScope.product.product_sale_price}"/>
        <span class="details_unit text_info">元</span>
    </div>
</div>
<div class="details_div">
    <span class="details_title text_info">概述图片</span>
    <ul class="details_picList" id="product_single_list">
        <c:forEach items="${requestScope.product.singleProductImageList}" var="image">
            <li><img
                    src="${image.productImage_src}"
                    id="pic_single_${image.productImage_id}" width="128px" height="128px"
                    name="${image.productImage_id}"/></li>
        </c:forEach>
        <li class="details_picList_fileUpload">
            <svg class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1528"  width="40" height="40">
                <path d="M0 512C0 229.230208 229.805588 0 512 0 794.769792 0 1024 229.805588 1024 512 1024 794.769792 794.194412 1024 512 1024 229.230208 1024 0 794.194412 0 512Z" p-id="1529" fill="#FF7874"></path>
                <path d="M753.301333 490.666667l-219.946667 0L533.354667 270.741333c0-11.776-9.557333-21.333333-21.354667-21.333333-11.776 0-21.333333 9.536-21.333333 21.333333L490.666667 490.666667 270.72 490.666667c-11.776 0-21.333333 9.557333-21.333333 21.333333 0 11.797333 9.557333 21.354667 21.333333 21.354667L490.666667 533.354667l0 219.904c0 11.861333 9.536 21.376 21.333333 21.376 11.797333 0 21.354667-9.578667 21.354667-21.333333l0-219.946667 219.946667 0c11.754667 0 21.333333-9.557333 21.333333-21.354667C774.634667 500.224 765.077333 490.666667 753.301333 490.666667z" p-id="1530" fill="#FFFFFF"></path>
            </svg>
            <span>点击上传</span>
            <input type="file" onchange="uploadImage(this)" accept="image/*">
        </li>
    </ul>
</div>
<div class="details_div">
    <span class="details_title text_info">详情图片</span>
    <ul class="details_picList" id="product_details_list">
        <c:forEach items="${requestScope.product.detailProductImageList}" var="image">
            <li><img
                    src="   ${image.productImage_src}"
                    id="pic_details_${image.productImage_id}" width="128px" height="128px"
                    name="${image.productImage_id}"/></li>
        </c:forEach>
        <li class="details_picList_fileUpload">
            <svg class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1528"  width="40" height="40">
                <path d="M0 512C0 229.230208 229.805588 0 512 0 794.769792 0 1024 229.805588 1024 512 1024 794.769792 794.194412 1024 512 1024 229.230208 1024 0 794.194412 0 512Z" p-id="1529" fill="#FF7874"></path>
                <path d="M753.301333 490.666667l-219.946667 0L533.354667 270.741333c0-11.776-9.557333-21.333333-21.354667-21.333333-11.776 0-21.333333 9.536-21.333333 21.333333L490.666667 490.666667 270.72 490.666667c-11.776 0-21.333333 9.557333-21.333333 21.333333 0 11.797333 9.557333 21.354667 21.333333 21.354667L490.666667 533.354667l0 219.904c0 11.861333 9.536 21.376 21.333333 21.376 11.797333 0 21.354667-9.578667 21.354667-21.333333l0-219.946667 219.946667 0c11.754667 0 21.333333-9.557333 21.333333-21.354667C774.634667 500.224 765.077333 490.666667 753.301333 490.666667z" p-id="1530" fill="#FFFFFF"></path>
            </svg>
            <span>点击上传</span>
            <input type="file" onchange="uploadImage(this)" accept="image/*" class="product_details_image_list">
        </li>
    </ul>
</div>
<div class="details_div details_div_last details_property_list" >
    <span class="details_title text_info">属性值信息</span>
    <c:forEach items="${requestScope.propertyList}" var="property" varStatus="status">
        <c:choose>
            <c:when test="${status.index % 2 == 0}">
                <label class="frm_label lbl_property_name text_info" id="lbl_product_property_${property.property_id}"
                       for="input_product_property_${property.property_id}">${property.property_name}</label>
                <c:choose>
                    <c:when test="${property.propertyValueList != null}">
                        <c:forEach items="${property.propertyValueList}" var="propertyValue">
                            <input class="frm_input" id="input_product_property_${property.property_id}" type="text"
                                   maxlength="50" value="${propertyValue.propertyValue_value}"
                                   data-pvid="${propertyValue.propertyValue_id}"/>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <input class="frm_input" id="input_product_property_${property.property_id}" type="text"
                               maxlength="50"/>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <label class="frm_label lbl_property_name text_info" id="lbl_product_property_${property.property_id}"
                       for="input_product_property_${property.property_id}">${property.property_name}</label>
                <c:choose>
                    <c:when test="${property.propertyValueList != null}">
                        <c:forEach items="${property.propertyValueList}" var="propertyValue">
                            <input class="frm_input" id="input_product_property_${property.property_id}" type="text"
                                   maxlength="50" value="${propertyValue.propertyValue_value}"
                                   data-pvid="${propertyValue.propertyValue_id}"/>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <input class="frm_input" id="input_product_property_${property.property_id}" type="text"
                               maxlength="50"/>
                    </c:otherwise>
                </c:choose>
                <div class="br"></div>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</div>

<div class="details_tools_div">
    <input class="frm_btn" id="btn_product_save" type="button" value="保存"/>
    <input class="frm_btn frm_clear" id="btn_product_cancel" type="button" value="取消"/>
</div>

<%-- 模态框 --%>
<div class="modal fade" id="modalDiv" tabindex="-1" role="dialog" aria-labelledby="modalDiv" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="myModalLabel">提示</h4>
            </div>
            <div class="modal-body">您确定要删除该产品图片吗？</div>
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