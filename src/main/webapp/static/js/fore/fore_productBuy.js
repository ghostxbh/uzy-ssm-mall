$(function () {
    //刷新下拉框
    $('#select_order_address_province').selectpicker('refresh');
    $('#select_order_address_city').selectpicker('refresh');
    $('#select_order_address_district').selectpicker('refresh');
    //改变订单信息时
    $('#select_order_address_province').change(function () {
        $.ajax({
            type: "GET",
            url: "/mall/address/" + $(this).val(),
            data: null,
            dataType: "json",
            success: function (data) {
                $(".loader").hide();
                if (data.success) {
                    $("#select_order_address_city").empty();
                    $("#select_order_address_district").empty();
                    for (var i = 0; i < data.addressList.length; i++) {
                        var address_id = data.addressList[i].address_areaId;
                        var address_name = data.addressList[i].address_name;
                        $("#select_order_address_city").append("<option value='" + address_id + "'>" + address_name + "</option>")
                    }
                    for (var j = 0; j < data.childAddressList.length; j++) {
                        var childAddress_id = data.childAddressList[j].address_areaId;
                        var childAddress_name = data.childAddressList[j].address_name;
                        $("#select_order_address_district").append("<option value='" + childAddress_id + "'>" + childAddress_name + "</option>")
                    }
                    $('#select_order_address_city').selectpicker('refresh');
                    $("#select_order_address_district").selectpicker('refresh');
                    $("span.address_province").text($("#select_order_address_province").find("option:selected").text());
                    $("span.address_city").text($("#select_order_address_city").find("option:selected").text());
                    $("span.address_district").text($("#select_order_address_district").find("option:selected").text());
                } else {
                    alert("加载地区信息失败，请刷新页面再试！")
                }
            },
            beforeSend: function () {
                $(".loader").show();
            },
            error: function () {
                alert("加载地区信息失败，请刷新页面再试！")
            }
        });
    });
    $("#select_order_address_city").change(function () {
        $.ajax({
            type: "GET",
            url: "/mall/address/" + $(this).val(),
            data: null,
            dataType: "json",
            success: function (data) {
                $(".loader").hide();
                if (data.success) {
                    $("#select_order_address_district").empty();
                    for (var i = 0; i < data.addressList.length; i++) {
                        var address_id = data.addressList[i].address_areaId;
                        var address_name = data.addressList[i].address_name;
                        $("#select_order_address_district").append("<option value='" + address_id + "'>" + address_name + "</option>")
                    }
                    $('#select_order_address_district').selectpicker('refresh');
                    $("span.address_city").text($("#select_order_address_city").find("option:selected").text());
                    $("span.address_district").text($("#select_order_address_district").find("option:selected").text());
                } else {
                    alert("加载地区信息失败，请刷新页面再试！")
                }
            },
            beforeSend: function () {
                $(".loader").show();
            },
            error: function () {
                alert("加载地区信息失败，请刷新页面再试！")
            }
        });
    });
    $("#select_order_address_district").change(function () {
        $("span.address_district").text($(this).find("option:selected").text());
    });
    $("#textarea_details_address").bind('input propertychange', function () {
        $(".address_details").text($(this).val());
    });
    $("#input_order_receiver").bind('input propertychange', function () {
        $(".user-name").text($(this).val());
    });
    $("#input_order_phone").bind('input propertychange', function () {
        $(".user-phone").text($(this).val());
    });
    $("input,textarea").focus(function () {
        styleUtil.specialBasicErrorHide($(this).prev("span").prev("label"));
    });

    //搜索框验证
    $('form').submit(function () {
        if ($(this).find("input[name='product_name']").val() === "") {
            alert("请输入关键字！");
            return false;
        }
    });
});