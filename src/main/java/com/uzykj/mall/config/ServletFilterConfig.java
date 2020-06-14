package com.uzykj.mall.config;

import com.uzykj.mall.entity.User;
import com.uzykj.mall.util.qiniu.QiniuUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器
 * @date  2018-10-28 晚上20：07
 * @author gostxbh
 */
public class ServletFilterConfig implements Filter{

	private static final String FILTERED_REQUEST = "@@session_context_filtered_request";


	private static final String[] INHERENT_ESCAPE_URIS = {"/","error", "logout","getsign", "index" };

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpRespose = (HttpServletResponse) response;
		User user = (User) httpRequest.getSession().getAttribute("user");
		//Renter currentRenter = (Renter) httpRequest.getSession().getAttribute("CurrentRenter");
		
		HttpServletResponse _response = (HttpServletResponse) response;
		_response.setHeader("Content-type", "text/html;charset=UTF-8");
		
		
		if (request != null && request.getAttribute(FILTERED_REQUEST) != null) {
			filterChain.doFilter(request, response);
		} else {

			request.setAttribute(FILTERED_REQUEST, Boolean.TRUE);

			if (user == null && !isURILogin(httpRequest.getRequestURI(), httpRequest)) {
				if ( !isURILogin(httpRequest.getRequestURI(), httpRequest)) {
					//========================
					httpRespose.sendRedirect("/login");
					return ;
				}			
			}
			filterChain.doFilter(request, response);
		}

	}

	@Override
	public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
		QiniuUtil.getInstance();
	}

	
	private boolean isURILogin(String requestURI, HttpServletRequest request) {
		if (request.getContextPath().equalsIgnoreCase(requestURI) || (request.getContextPath() + "/").equalsIgnoreCase(requestURI))
			return true;
		for (String uri : INHERENT_ESCAPE_URIS) {
			
			if (requestURI != null && requestURI.indexOf(uri) >= 0) {
				return true;
			}
		}
		return false;
	}
}
