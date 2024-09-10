package springsideproject1.springsideproject1build.web.filter;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import springsideproject1.springsideproject1build.web.request.ModifiableHttpServletRequest;

import java.io.IOException;

import static springsideproject1.springsideproject1build.domain.entity.article.Press.containsWithPressValue;
import static springsideproject1.springsideproject1build.domain.entity.article.Press.convertToPress;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.PRESS;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

@NonNullApi
@WebFilter(urlPatterns = {ADD_SINGLE_COMPANY_ARTICLE_URL, UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX,
        ADD_SINGLE_INDUSTRY_ARTICLE_URL, UPDATE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX})
@Order(1)
public class ArticleDtoSupportFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest requestBefore, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(getModifiableHttpServletRequest(requestBefore), response);
    }

    private ModifiableHttpServletRequest getModifiableHttpServletRequest(HttpServletRequest requestBefore) {
        ModifiableHttpServletRequest request = new ModifiableHttpServletRequest(requestBefore);

        String name = request.getParameter(NAME);
        if (name != null) request.setParameter(NAME, name.strip());

        String press = request.getParameter(PRESS);
        if (press != null) {
            request.setParameter(PRESS, press.toUpperCase());
            if (containsWithPressValue(press))
                request.setParameter(PRESS, convertToPress(press).name());
        }
        return request;
    }
}
