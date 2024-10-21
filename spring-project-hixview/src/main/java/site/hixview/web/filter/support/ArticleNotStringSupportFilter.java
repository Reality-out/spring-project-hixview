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

import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.name.EntityName.Article.PRESS;
import static site.hixview.util.FilterUtils.applyStrip;
import static site.hixview.util.FilterUtils.applyUppercaseAndConvertToEnumWithString;

@NonNullApi
@WebFilter(urlPatterns = {ADD_SINGLE_COMPANY_ARTICLE_URL, UPDATE_COMPANY_ARTICLE_URL + FINISH_URL,
        ADD_SINGLE_INDUSTRY_ARTICLE_URL, UPDATE_INDUSTRY_ARTICLE_URL + FINISH_URL,
        ADD_SINGLE_ECONOMY_ARTICLE_URL, UPDATE_ECONOMY_ARTICLE_URL + FINISH_URL})
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
