<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<style rel="stylesheet">
    .J_details {
        width: 100%;
        display: block;
    }

    .J_details > .J_details_list {
        margin-bottom: 10px;
        border: 1px solid #e6e6e6;
        border-top: none;
    }

    .J_details_list > .J_details_list_header {
        display: block;
        padding: 8px 20px 10px;
        border-top: 1px solid #e6e6e6;
        height: 40px;
        line-height: 40px;
        color: #666;
        margin: 0;
        font-family: "Microsoft YaHei UI", serif;
        font-size: 10px;
    }

    .J_details_list_header > span {
        color: #333333;
        font-family: "Microsoft YaHei UI", serif;
    }

    .J_details_list > .J_details_list_title {
        margin: 0;
        padding: 5px 20px;
        line-height: 22px;
        color: #999;
        font-weight: 700;
        font-family: "Microsoft YaHei UI", serif;
        font-size: 10px;
    }

    .J_details_list > .J_details_list_body {
        padding: 0 20px 18px;
        border-top: 1px solid #ffffff;
        margin: 0;
        zoom: 1;
    }

    .J_details_list_body > li {
        display: inline;
        float: left;
        width: 220px;
        height: 18px;
        overflow: hidden;
        margin: 10px 15px 0 0;
        line-height: 18px;
        vertical-align: top;
        white-space: nowrap;
        text-overflow: ellipsis;
        color: #666;
        font-family: "Microsoft YaHei UI", serif;
        font-size: 10px;
    }

    .J_details_list_body:after {
        display: block;
        content: "\0020";
        clear: both;
        visibility: hidden;
    }
</style>
<div class="J_details">
    <div class="J_details_list">
        <p class="J_details_list_header">产品名称：<span>${requestScope.product.product_name}</span></p>
        <p class="J_details_list_title">产品参数：</p>
        <ul class="J_details_list_body">
            <c:forEach items="${requestScope.propertyList}" var="property">
                <c:if test="${property.propertyValueList[0].propertyValue_value != null}">
                    <li title="${property.propertyValueList[0].propertyValue_value}">${property.property_name}：${property.propertyValueList[0].propertyValue_value}</li>
                </c:if>
            </c:forEach>
        </ul>
    </div>
</div>

