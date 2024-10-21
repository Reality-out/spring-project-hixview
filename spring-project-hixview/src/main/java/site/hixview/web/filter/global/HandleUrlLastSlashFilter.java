package site.hixview.web.filter.global;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import site.hixview.web.request.ModifiableHttpServletRequest;

import java.io.IOException;
import java.util.List;

import static site.hixview.domain.vo.user.RequestUrl.COMPANY_SEARCH_URL;

@NonNullApi
@WebFilter(urlPatterns = "/*")
@Order(0)
public class HandleUrlLastSlashFilter extends OncePerRequestFilter {

    private final List<String> excludedURL = List.of(COMPANY_SEARCH_URL);

    @Override
    protected void doFilterInternal(HttpServletRequest requestBefore, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        ModifiableHttpServletRequest request = new ModifiableHttpServletRequest(requestBefore);
        if (request.getMethod().equals(HttpMethod.GET.toString()) && request.getQueryString() == null
                && request.getRequestURI().endsWith("/") && !excludedURL.contains(request.getRequestURI())) {
            String requestURI = request.getRequestURI();
            StringBuffer requestURL = request.getRequestURL();
            request.setRequestURI(requestURI.substring(0, requestURI.length() - 1));
            request.setRequestURL(new StringBuffer(requestURL.substring(0, requestURL.length() - 1)));
        }
        chain.doFilter(request, response);
    }
}