package springsideproject1.springsideproject1build.web.filter;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import springsideproject1.springsideproject1build.domain.entity.Press;
import springsideproject1.springsideproject1build.web.request.ModifiableHttpServletRequest;

import java.io.IOException;

import static springsideproject1.springsideproject1build.domain.vo.EntityName.Article.PRESS;
import static springsideproject1.springsideproject1build.domain.vo.RequestUrl.FINISH_URL;
import static springsideproject1.springsideproject1build.domain.vo.Word.NAME;
import static springsideproject1.springsideproject1build.domain.vo.manager.RequestUrl.*;
import static springsideproject1.springsideproject1build.util.FilterUtils.applyStrip;
import static springsideproject1.springsideproject1build.util.FilterUtils.applyUppercaseAndConvertToEnum;

@NonNullApi
@WebFilter(urlPatterns = {ADD_SINGLE_COMPANY_ARTICLE_URL, UPDATE_COMPANY_ARTICLE_URL + FINISH_URL,
        ADD_SINGLE_INDUSTRY_ARTICLE_URL, UPDATE_INDUSTRY_ARTICLE_URL + FINISH_URL})
@Order(1)
public class NotStringArticleSupportFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest requestBefore, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        ModifiableHttpServletRequest request = new ModifiableHttpServletRequest(requestBefore);
        applyStrip(request, NAME);
        applyUppercaseAndConvertToEnum(request, Press.class, PRESS);
        chain.doFilter(request, response);
    }

}
