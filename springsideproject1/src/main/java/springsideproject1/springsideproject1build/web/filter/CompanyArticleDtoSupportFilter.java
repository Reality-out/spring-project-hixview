package springsideproject1.springsideproject1build.web.filter;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import springsideproject1.springsideproject1build.web.ModifiableHttpServletRequest;

import java.io.IOException;

import static springsideproject1.springsideproject1build.domain.entity.article.Press.containsWithPressValue;
import static springsideproject1.springsideproject1build.domain.entity.article.Press.convertToPress;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.PRESS;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

@NonNullApi
@Order(1)
@WebFilter(urlPatterns = {ADD_SINGLE_COMPANY_ARTICLE_URL, UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX})
public class CompanyArticleDtoSupportFilter extends OncePerRequestFilter {


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        ModifiableHttpServletRequest modifiableRequest = new ModifiableHttpServletRequest(request);

        String name = modifiableRequest.getParameter(NAME);
        if (name != null) modifiableRequest.setParameter(NAME, name.strip());

        String press = modifiableRequest.getParameter(PRESS);
        if (press != null) {
            modifiableRequest.setParameter(PRESS, press.toUpperCase());
            if (containsWithPressValue(press))
                modifiableRequest.setParameter(PRESS, convertToPress(press).name());
        }

        chain.doFilter(modifiableRequest, response);
    }
}
