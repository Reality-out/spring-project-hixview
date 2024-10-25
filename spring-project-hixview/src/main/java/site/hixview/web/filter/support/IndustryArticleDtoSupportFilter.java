package site.hixview.web.filter.support;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.SecondCategory;
import site.hixview.web.request.ModifiableHttpServletRequest;

import java.io.IOException;

import static site.hixview.domain.vo.RequestPath.FINISH_PATH;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.RequestPath.ADD_SINGLE_INDUSTRY_ARTICLE_PATH;
import static site.hixview.domain.vo.manager.RequestPath.UPDATE_INDUSTRY_ARTICLE_PATH;
import static site.hixview.util.FilterUtils.applyUppercaseAndConvertToEnumWithMap;
import static site.hixview.util.FilterUtils.applyUppercaseAndConvertToEnumWithString;

@NonNullApi
@WebFilter(urlPatterns = {ADD_SINGLE_INDUSTRY_ARTICLE_PATH, UPDATE_INDUSTRY_ARTICLE_PATH + FINISH_PATH})
@Order(2)
public class IndustryArticleDtoSupportFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest requestBefore, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        ModifiableHttpServletRequest request = new ModifiableHttpServletRequest(requestBefore);
        applyUppercaseAndConvertToEnumWithString(request, FirstCategory.class, SUBJECT_FIRST_CATEGORY);
        applyUppercaseAndConvertToEnumWithMap(request, SecondCategory.class, SUBJECT_SECOND_CATEGORIES, SUBJECT_SECOND_CATEGORY);
        chain.doFilter(request, response);
    }
}
