package cn.ejfy.web.tools.context;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 执行时间过滤器
 */
public class ProcessTimeFilter implements Filter {
	/**
	 * 请求执行开始时间
	 */
	public static final String START_TIME = "_start_time";

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse rep,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rep;
        String url = request.getServletPath();
        String uri = request.getRequestURI();
        // 不允许jsp被执行，防止被挂马
        if (url.endsWith(".jsp") || uri.endsWith(".jsp")) {
            return;
        }
        // 不允许properties被执行，防止泄漏隐私
        if (url.endsWith(".properties") || uri.endsWith(".properties")) {
            return;
        }
        long time = System.currentTimeMillis();
        request.setAttribute(START_TIME, time);
        
        /**实现anywhere*/
        ThreadContextHolder.setHttpRequest(request);
        ThreadContextHolder.setHttpResponse(response);
        ThreadContextHolder.setSession(request.getSession());
        
        chain.doFilter(request, response);
        time = System.currentTimeMillis() - time;
//        if(log.isInfoEnabled()){
//        	log.info("process in {} ms: {}", time, request.getRequestURI());
//        }
    }

	public void init(FilterConfig arg0) throws ServletException {
	}
}
