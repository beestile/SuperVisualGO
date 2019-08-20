package com.zetta.common.utils;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CommonInterceptor extends HandlerInterceptorAdapter{
    
	public zLogger logger = new zLogger(getClass());
 
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    	
    	Enumeration param = request.getParameterNames();
    	String strParam = "";
    	while(param.hasMoreElements()) {
    		String name = String.valueOf(param.nextElement());
    		String value = String.valueOf(request.getParameter(name));
    		strParam +=name +"="+value+"&";
    	}
    	
    	logger.info("strParam::" +strParam);
    	
    	HttpSession session = request.getSession(true);
    	String userId = (String)session.getAttribute("userId");
    	
    	if (request.getRequestURI().endsWith("index_admin.do")){
    		if(userId == null || userId != "admin") {
    			response.sendRedirect("./../login.jsp?"+strParam);
    		}
    		return false;
    	}
    	
		if(userId == null) {
			session.setAttribute("userId", "guest");
			session.setAttribute("userNm", "사용자그룹");
			session.setAttribute("userSessionMap", null);
			session.setAttribute("ticketStr", null);
		}
		
		
    	
        return super.preHandle(request, response, handler);
    }
 
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
//    	logger.info("===================        request END        ===================\n");
    }
     
}