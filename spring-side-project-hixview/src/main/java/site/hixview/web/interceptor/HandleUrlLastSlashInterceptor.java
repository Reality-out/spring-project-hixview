package site.hixview.web.interceptor;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@NonNullApi
@Component
public class HandleUrlLastSlashInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("GET") && request.getQueryString() != null) {
            String requestURI = request.getRequestURI();
            if (String.valueOf(requestURI.charAt(requestURI.length() - 1)).equals("/")) {
                response.sendRedirect(requestURI.substring(0, requestURI.length() - 2));
                return false;
            }
        }
        return true;
    }
}