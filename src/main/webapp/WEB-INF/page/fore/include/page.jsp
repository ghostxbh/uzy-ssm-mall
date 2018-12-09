<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<script>
    $(function () {
        $(".disabled>a,.pageThis>a").attr("onclick", null);
    })
</script>
<div id="pageDiv">
    <ul>
        <li data-name="firstPage" <c:if test="${!requestScope.pageUtil.isHasPrev()}">class="disabled" </c:if>>
            <a href="javascript:void(0)" onclick="getPage(0)" aria-label="首页"><span
                    aria-hidden="true">&laquo;</span></a>
        </li>
        <li data-name="prevPage" <c:if test="${!requestScope.pageUtil.isHasPrev()}">class="disabled" </c:if>>
            <a href="javascript:void(0)" onclick="getPage(${requestScope.pageUtil.index-1})" aria-label="上一页"><span
                    aria-hidden="true">&lsaquo;</span></a>
        </li>

        <c:forEach begin="1" end="${requestScope.pageUtil.getTotalPage()}" varStatus="status">
            <c:if test="${status.count-requestScope.pageUtil.index>=-5 && status.count-requestScope.pageUtil.index<=5}">
                <li <c:if test="${status.count==requestScope.pageUtil.index+1}"> class="pageThis" </c:if>>
                    <a href="javascript:void(0)" onclick="getPage(${status.count-1})">${status.count}</a>
                </li>
            </c:if>
        </c:forEach>

        <li data-name="nextPage" <c:if test="${!requestScope.pageUtil.isHasNext()}">class="disabled" </c:if>>
            <a href="javascript:void(0)" onclick="getPage(${requestScope.pageUtil.index+1})" aria-label="下一页"><span
                    aria-hidden="true">&rsaquo;</span></a>
        </li>
        <li data-name="lastPage" <c:if test="${!requestScope.pageUtil.isHasNext()}">class="disabled" </c:if>>
            <a href="javascript:void(0)" onclick="getPage(${requestScope.pageUtil.getTotalPage()-1})" aria-label="尾页">&raquo;</a>
        </li>
    </ul>
</div>

