package site.hixview.web.filter.support;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;
import site.hixview.domain.entity.Classification;
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.SubjectCountry;
import site.hixview.web.request.ModifiableHttpServletRequest;

import java.io.IOException;
import java.util.Locale;

import static site.hixview.domain.vo.RequestPath.FINISH_PATH;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.RequestPath.ADD_BLOG_POST_PATH;
import static site.hixview.domain.vo.manager.RequestPath.UPDATE_BLOG_POST_PATH;
import static site.hixview.util.EnumUtils.inEnumConstants;
import static site.hixview.util.FilterUtils.*;

@NonNullApi
@WebFilter(urlPatterns = {ADD_BLOG_POST_PATH, UPDATE_BLOG_POST_PATH + FINISH_PATH})
@Order(1)
public class BlogPostSupportFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(BlogPostSupportFilter.class);

    @Override
    public void doFilterInternal(HttpServletRequest requestBefore, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        ModifiableHttpServletRequest request = new ModifiableHttpServletRequest(requestBefore);
        applyStrip(request, NAME);
        applyUppercaseAndConvertToEnumWithString(request, Classification.class, CLASSIFICATION);
        applyUppercaseAndConvertToEnumWithString(request, FirstCategory.class, TARGET_NAME);
        applyUppercaseAndConvertToEnumWithString(request, SubjectCountry.class, TARGET_NAME);
        addTargetImagePathPrefixSuffix(request);
        chain.doFilter(request, response);
    }

    private static void addTargetImagePathPrefixSuffix(ModifiableHttpServletRequest request) {
        String imagePath = request.getParameter(TARGET_IMAGE_PATH);
        String targetName = request.getParameter(TARGET_NAME);
        if (imagePath != null && !imagePath.isBlank()) {
            if (!imagePath.startsWith(IMAGE_PATH_PREFIX)) {
                imagePath = IMAGE_PATH_PREFIX + imagePath;
            }
            if (!inEnumConstants(FirstCategory.class, targetName) && !inEnumConstants(SubjectCountry.class, targetName) && !imagePath.endsWith(IMAGE_PATH_SUFFIX)) {
                imagePath = imagePath + IMAGE_PATH_SUFFIX;
            }
            request.setParameter(TARGET_IMAGE_PATH, imagePath);
        }
        if (request.getMethod().equals(HttpMethod.POST.name()) && (imagePath == null || imagePath.isBlank())) {
            if (inEnumConstants(FirstCategory.class, targetName)) {
                imagePath = INDUSTRY_IMAGE_PATH_PREFIX + targetName.toLowerCase(Locale.ROOT);
            } else if (inEnumConstants(SubjectCountry.class, targetName)) {
                imagePath = COUNTRY_FLAG_IMAGE_PATH_PREFIX + targetName.toLowerCase(Locale.ROOT) + COUNTRY_FLAG_IMAGE_PATH_SUFFIX;
            }
            request.setParameter(TARGET_IMAGE_PATH, imagePath);
        }
    }
}
