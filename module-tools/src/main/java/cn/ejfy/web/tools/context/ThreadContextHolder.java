package cn.ejfy.web.tools.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 *  用ThreadLocal来存储,以便实现HttpServletRequest any where 
 */
public class ThreadContextHolder  {

	private static ThreadLocal<HttpSession> SessionThreadLocalHolder = new ThreadLocal<HttpSession>();
	private static ThreadLocal<HttpServletRequest> HttpRequestThreadLocalHolder = new ThreadLocal<HttpServletRequest>();
	private static ThreadLocal<HttpServletResponse> HttpResponseThreadLocalHolder = new ThreadLocal<HttpServletResponse>();
	
	
	public static void setHttpRequest(HttpServletRequest request){
		HttpRequestThreadLocalHolder.set(request);
	}
	
	public static HttpServletRequest getHttpRequest(){
		return  HttpRequestThreadLocalHolder.get();
	}
	
	public static void remove(){
	    SessionThreadLocalHolder.remove();
		HttpRequestThreadLocalHolder.remove();
		HttpResponseThreadLocalHolder.remove();
	}
	
	
	public static void setHttpResponse(HttpServletResponse response){
		HttpResponseThreadLocalHolder.set(response);	
	}
	
	public static HttpServletResponse getHttpResponse(){
		return HttpResponseThreadLocalHolder.get();
	}
	
	
	public static void setSession(HttpSession session) {
        SessionThreadLocalHolder.set(session);
    }
	public static HttpSession getSession() {
        if (SessionThreadLocalHolder.get() == null) {
            HttpServletRequest httpRequest = getHttpRequest();
            SessionThreadLocalHolder.set( (httpRequest != null ) ? httpRequest.getSession() : null );
        }
        return SessionThreadLocalHolder.get();
    }
}
