package springsideproject1.springsideproject1build.web.filter;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import springsideproject1.springsideproject1build.domain.entity.ArticleClassName;
import springsideproject1.springsideproject1build.web.request.ModifiableHttpServletRequest;

import java.io.IOException;

import static springsideproject1.springsideproject1build.domain.vo.CLASS.ARTICLE_CLASS_NAME;
import static springsideproject1.springsideproject1build.domain.vo.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.vo.WORD.NAME;
import static springsideproject1.springsideproject1build.util.FilterUtils.applyStrip;
import static springsideproject1.springsideproject1build.util.FilterUtils.applyUppercaseAndConvertToEnum;

@NonNullApi
@WebFilter(urlPatterns = {ADD_ARTICLE_MAIN_URL, UPDATE_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX})
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
