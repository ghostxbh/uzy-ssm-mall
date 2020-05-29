$(function () {
    var ul = $(".context_ul_goodsList").children("ul");

    $(".J_GoodsDetails").addClass("tab-selected");
    $(".context_img_li").eq(0).addClass("context_img_li_hover");

    //搜索框验证
    $('form').submit(function () {
        if ($(this).find("input[name='product_name']").val() === "") {
            alert("请输入关键字！");
            return false;
        }
    });
    //移入预览图片列表时
    $(".context_img_li").mouseenter(function () {
        var img = $(this).children("img");
        $(".context_img_main").attr("src", img.attr("src"));
        $(".context_img_ks").children("img").attr("src", img.attr("src"));
        $(".context_img_li").removeClass("context_img_li_hover");
        $(this).addClass("context_img_li_hover");
    });
    //产品数量框验证
    $(".amount_value_up").click(function () {
        var number = parseInt($(".context_buymember").val());
        number++;
        $(".context_buymember").val(number);
    });
    $(".amount_value-down").click(function () {
        var number = parseInt($(".context_buymember").val());
        if (number > 1) {
            number--;
            $(".context_buymember").val(number);
        }
    });
    $(".context_buymember").on("input", function () {
        if ($(this).val() === "") {
            $(this).val(1);
        }
        if (parseInt($("#stock").val()) < parseInt($(this).val())) {
            $(".context_buyNow").addClass("context_notBuy").attr("disabled", "disabled");
            $(".context_addBuyCar").addClass("context_notCar").attr("disabled", "disabled");
        } else {
            $(".context_buyNow").removeClass("context_notBuy").attr("disabled", null);
            $(".context_addBuyCar").removeClass("context_notCar").attr("disabled", null);
        }
    });
    //点击猜你喜欢翻页按钮时
    $(".ul_trigger_up").click(function () {
        var ulTop = parseInt(ul.css("top"));
        var fTop = ulTop + 480;
        if (fTop > 0) {
            ul.animate({
                top: ulTop + 40
            }, 100, function () {
                ul.animate({
                    top: 0
                }, 100);
            });
        } else {
            ul.animate({
                top: fTop
            }, 200);
        }
    });
    $(".ul_trigger_down").click(function () {
        var ulTop = parseInt(ul.css("top"));
        var fTop = ulTop - 480;
        if (ul.height() < 2880) {
            getGuessLoveProducts();
        }
        if (fTop < -2400) {
            ul.animate({
                top: ulTop - 40
            }, 100, function () {
                ul.animate({
                    top: -2400
                }, 100);
            });
        } else {
            ul.animate({
                top: fTop
            }, 200);
        }
    });
    //放大镜逻辑
    $(".context_img_main").mouseenter(function () {
        $(".context_img_winSelector").show();
        $(".context_img_ks").show().children("img").attr("src", $(this).attr("src"));
    });
    $(".context_img_winSelector").mouseleave(function () {
        $(".context_img_winSelector").hide();
        $(".context_img_ks").hide();
    });
    $(".context_img_main,.context_img_winSelector").mousemove(function (e) {
        SelectorMousemove(e);
    });
    //模态窗口登录
    $(".loginForm").unbind("submit").submit(function () {
        var yn = true;
        $(this).find(":text,:password").each(function () {
            if ($.trim($(this).val()) === "") {
                styleUtil.errorShow($("#error_message_p"), "请输入用户名和密码！");
                yn = false;
                return yn;
            }
        });
        if (yn) {
            $.ajax({
                type: "POST",
                url: "/mall/login/doLogin",
                data: {"username": $.trim($("#name").val()), "password": $.trim($("#password").val())},
                dataType: "json",
                success: function (data) {
                    $(".loginButton").val("登 录");
                    if (data.success) {
                        location.reload();
                    } else {
                        styleUtil.errorShow($("#error_message_p"), "用户名和密码错误！");
                    }
                },
                error: function (data) {
                    $(".loginButton").val("登 录");
                    styleUtil.errorShow($("#error_message_p"), "服务器异常，请刷新页面再试！");
                },
                beforeSend: function () {
                    $(".loginButton").val("正在登录...");
                }
            });
        }
        return false;
    });
    //关闭模态窗口
    $(".closeLoginDiv").click(function () {
        $(".loginModel").hide();
        $(".loginDiv").hide();
    });
});

function getDetailsPage(obj, className) {
    $(".J_TabBarBox").find("li").removeClass("tab-selected");
    $(obj).parent("li").addClass("tab-selected");
    $(".J_choose").children("div").hide();
    $("." + className).show();
}

function SelectorMousemove(e) {
    var $img = $(".context_img_main");
    var $selector = $(".context_img_winSelector");
    var $imgWidth = $img.width();
    var $imgHeight = $img.height();
    var $selectorWidth = $selector.width();
    var $selectorHeight = $selector.height();
    /*扫描器的定位*/
    //获取光标正中位置
    var x = e.pageX - $img.offset().left - $selectorWidth / 2;
    var y = e.pageY - $img.offset().top - $selectorHeight / 2;
    x = x < 0 ? 0 : x;
    y = y < 0 ? 0 : y;
    x = x > $imgWidth - $selectorWidth ? $imgWidth - $selectorWidth : x;
    y = y > $imgHeight - $selectorHeight ? $imgHeight - $selectorHeight : y;
    $selector.css({left: x, top: y});
    var naturalNumber = $('.context_img_ks').width() / $selectorWidth;
    //1.917为转换系数
    $('.context_img_ks>img').css({
        left: -x * 1.917,
        top: -y * 1.917
    });
}

function getGuessLoveProducts() {
    $.ajax({
        type: "GET",
        url: "/mall/guess/" + $("#tid").val(),
        data: {"guessNumber": $("#guessNumber").val()},
        dataType: "json",
        success: function (data) {
            if (data.success) {
                $("#guessNumber").val(data.guessNumber);
                for (var i = 0; i < data.loveProductList.length; i++) {
                    var src = data.loveProductList[i].singleProductImageList[0].productImage_src;
                    var product_id = data.loveProductList[i].product_id;
                    var product_sale_price = data.loveProductList[i].product_sale_price;
                    $(".context_ul_goodsList").children("ul").append("<li class='context_ul_main'><div class='context_ul_img'>" +
                        "<a href='/mall/product/" + product_id + "'><img src='/mall/res/images/item/productSinglePicture/" + src + "'/></a><p>¥" + product_sale_price + ".00</p></div></li>"
                    );
                }
            }
        }
    });
}