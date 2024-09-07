package springsideproject1.springsideproject1build.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.PatternMatchUtils;
import springsideproject1.springsideproject1build.web.ModifiableHttpServletRequest;

import java.io.IOException;

import static springsideproject1.springsideproject1build.domain.entity.article.Press.containsWithPressValue;
import static springsideproject1.springsideproject1build.domain.entity.article.Press.convertToPress;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.PRESS;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

public class CompanyArticleDtoSupportFilter implements Filter {

    private final String[] processedURL = {ADD_SINGLE_COMPANY_ARTICLE_URL, UPDATE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();
        ModifiableHttpServletRequest modifiableRequest = new ModifiableHttpServletRequest(request);

        if (PatternMatchUtils.simpleMatch(processedURL, requestURI)) {
            String name = modifiableRequest.getParameter(NAME);
            if (name != null) modifiableRequest.setParameter(NAME, name.strip());

            String press = modifiableRequest.getParameter(PRESS);
            if (press != null) {
                modifiableRequest.setParameter(PRESS, press.toUpperCase());
                if (containsWithPressValue(press))
                    modifiableRequest.setParameter(PRESS, convertToPress(press).name());
            }
        }

        chain.doFilter(modifiableRequest, servletResponse);
    }
}
