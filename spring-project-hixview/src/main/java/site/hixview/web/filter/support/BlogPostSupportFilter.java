package site.hixview.web.filter.support;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import site.hixview.domain.entity.Classification;
import site.hixview.web.request.ModifiableHttpServletRequest;

import java.io.IOException;

import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.util.FilterUtils.*;

@NonNullApi
@WebFilter(urlPatterns = {ADD_BLOG_POST_URL, UPDATE_BLOG_POST_URL + FINISH_URL})
@Order(1)
public class BlogPostSupportFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest requestBefore, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        ModifiableHttpServletRequest request = new ModifiableHttpServletRequest(requestBefore);
        applyStrip(request, NAME);
        applyUppercaseAndConvertToEnumWithString(request, Classification.class, CLASSIFICATION);
        addTargetImagePathPrefixSuffix(request);
        chain.doFilter(request, response);
    }

    private static void addTargetImagePathPrefixSuffix(ModifiableHttpServletRequest request) {
        String imagePath = request.getParameter(TARGET_IMAGE_PATH);
        if (imagePath != null) {
            if (!imagePath.startsWith(IMAGE_PATH_PREFIX)) {
                imagePath = IMAGE_PATH_PREFIX + imagePath;
            }
            if (!imagePath.endsWith(IMAGE_PATH_SUFFIX)) {
                imagePath = imagePath + IMAGE_PATH_SUFFIX;
            }
            request.setParameter(TARGET_IMAGE_PATH, imagePath);
        }
    }
}
