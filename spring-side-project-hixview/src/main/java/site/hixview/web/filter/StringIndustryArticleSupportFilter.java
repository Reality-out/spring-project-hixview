package site.hixview.web.filter;

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

import static site.hixview.domain.vo.name.EntityName.Article.SUBJECT_FIRST_CATEGORY;
import static site.hixview.domain.vo.name.EntityName.Article.SUBJECT_SECOND_CATEGORY;
import static site.hixview.domain.vo.manager.RequestURL.ADD_INDUSTRY_ARTICLE_WITH_STRING_URL;
import static site.hixview.util.FilterUtils.applyUppercaseAndConvertToEnum;

@NonNullApi
@WebFilter(urlPatterns = ADD_INDUSTRY_ARTICLE_WITH_STRING_URL)
@Order(1)
public class StringIndustryArticleSupportFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest requestBefore, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        ModifiableHttpServletRequest request = new ModifiableHttpServletRequest(requestBefore);
        applyUppercaseAndConvertToEnum(request, FirstCategory.class, SUBJECT_FIRST_CATEGORY);
        applyUppercaseAndConvertToEnum(request, SecondCategory.class, SUBJECT_SECOND_CATEGORY);
        chain.doFilter(request, response);
    }
}
