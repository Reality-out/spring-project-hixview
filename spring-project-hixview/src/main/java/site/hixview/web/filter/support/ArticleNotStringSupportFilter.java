package site.hixview.web.filter.support;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import site.hixview.domain.entity.Press;
import site.hixview.web.request.ModifiableHttpServletRequest;

import java.io.IOException;

import static site.hixview.domain.vo.RequestPath.FINISH_PATH;
import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.Word.PRESS;
import static site.hixview.domain.vo.manager.RequestPath.*;
import static site.hixview.util.FilterUtils.applyStrip;
import static site.hixview.util.FilterUtils.applyUppercaseAndConvertToEnumWithString;

@NonNullApi
@WebFilter(urlPatterns = {ADD_SINGLE_COMPANY_ARTICLE_PATH, UPDATE_COMPANY_ARTICLE_PATH + FINISH_PATH,
        ADD_SINGLE_INDUSTRY_ARTICLE_PATH, UPDATE_INDUSTRY_ARTICLE_PATH + FINISH_PATH,
        ADD_SINGLE_ECONOMY_ARTICLE_PATH, UPDATE_ECONOMY_ARTICLE_PATH + FINISH_PATH})
@Order(1)
public class ArticleNotStringSupportFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest requestBefore, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        ModifiableHttpServletRequest request = new ModifiableHttpServletRequest(requestBefore);
        applyStrip(request, NAME);
        applyUppercaseAndConvertToEnumWithString(request, Press.class, PRESS);
        chain.doFilter(request, response);
    }

}
