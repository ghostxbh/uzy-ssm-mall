<%@ page contentType="text/html;charset=UTF-8" %>
<nav id="nav_main" class="text_info">
    <svg class="home_logo" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1010" width="32" height="32">
        <path d="M867.271111 967.111111H157.013333c-65.422222 0-109.909333-66.332444-99.84-145.009778l86.641778-676.977777A103.537778 103.537778 0 0 1 244.622222 56.888889h535.495111a103.537778 103.537778 0 0 1 100.579556 88.519111L967.111111 822.101333c10.069333 78.677333-34.474667 145.009778-99.84 145.009778zM739.555556 170.666667a56.888889 56.888889 0 0 0-39.253334 97.792A198.428444 198.428444 0 0 1 512 440.888889a198.428444 198.428444 0 0 1-188.302222-172.430222 56.888889 56.888889 0 1 0-63.032889 10.296889A264.874667 264.874667 0 0 0 512 512a264.874667 264.874667 0 0 0 251.335111-233.244444A56.888889 56.888889 0 0 0 739.555556 170.666667z" p-id="1011" fill="#ff7874">
        </path>
    </svg>
    <span id="txt_home_title" class="nav_text">柚子云购数据管理后台</span>
    <i id="i_nickname_slide"></i>
    <span id="txt_home_nickname"><c:choose><c:when test="${requestScope.admin.admin_nickname != ''}">${requestScope.admin.admin_nickname}</c:when><c:otherwise>${requestScope.admin.admin_name}</c:otherwise></c:choose></span>
    <img id="img_home_profile_picture"
         src="${pageContext.request.contextPath}/res/images/item/adminProfilePicture/${requestScope.admin.admin_profile_picture_src}"
         onerror="this.src='${pageContext.request.contextPath}/res/images/admin/homePage/default_profile_picture-32x32.png'"
         alt="头像" title="头像" width="32px" height="32px">
    <input id="admin_id" type="hidden" value="${requestScope.admin.admin_id}"/>
    <ul id="nav_tools">
        <li id="nav_tools_admin_manage">账号管理</li>
        <li id="nav_tools_admin_logout">注销</li>
    </ul>
</nav>