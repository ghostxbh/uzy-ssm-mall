$(function () {
    //搜索框验证
    $('form').submit(function () {
        if ($(this).find('input[name="product_name"]').val() === '') {
            alert('请输入关键字！');
            return false;
        }
    });
    //点击li排序时
    $('.context_menu li').click(function () {
        //获取排序字段
        var orderBy = $(this).attr('data-name');
        //判断排序顺序及样式设置
        var isDesc = true;
        if (orderBy === 'product_sale_price') {
            if ($(this).children(".orderByDesc").hasClass("orderBySelect")) {
                isDesc = false;
            }
        }
        //检索
        if ($(this).parent('ul').attr('data-value') === undefined) {
            location.href = '/mall/product/0/20?orderBy=' + orderBy + "&isDesc=" + isDesc + "&category_id=" + $(this).parent('ul').attr('data-type');
        } else {
            location.href = '/mall/product/0/20?orderBy=' + orderBy + "&isDesc=" + isDesc + "&product_name=" + $(this).parent('ul').attr('data-value');
        }
    });
    //点击产品图片时
    $(".context_product_imgList>li").click(function () {
        var url = $(this).children("img").attr("src");
        if (url !== undefined) {
            $(this).parent("ul").prev("a").children(".context_product_imgMain").attr("src", url);
        }
    });
});