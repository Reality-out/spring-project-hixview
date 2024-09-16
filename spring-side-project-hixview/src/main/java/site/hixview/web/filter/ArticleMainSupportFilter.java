package site.hixview.web.filter;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import site.hixview.domain.entity.ArticleClassName;
import site.hixview.web.request.ModifiableHttpServletRequest;

import java.io.IOException;

import static site.hixview.domain.vo.name.EntityName.Article.ARTICLE_CLASS_NAME;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.manager.RequestURL.ADD_ARTICLE_MAIN_URL;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_ARTICLE_MAIN_URL;
import static site.hixview.util.FilterUtils.applyStrip;
import static site.hixview.util.FilterUtils.applyUppercaseAndConvertToEnum;

@NonNullApi
@WebFilter(urlPatterns = {ADD_ARTICLE_MAIN_URL, UPDATE_ARTICLE_MAIN_URL + FINISH_URL})
@Order(1)
public class ArticleMainSupportFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest requestBefore, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        ModifiableHttpServletRequest request = new ModifiableHttpServletRequest(requestBefore);
        applyStrip(request, NAME);
        applyUppercaseAndConvertToEnum(request, ArticleClassName.class, ARTICLE_CLASS_NAME);
        chain.doFilter(request, response);
    }
}
