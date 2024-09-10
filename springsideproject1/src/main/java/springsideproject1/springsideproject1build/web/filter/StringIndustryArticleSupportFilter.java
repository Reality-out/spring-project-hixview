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

import static springsideproject1.springsideproject1build.domain.entity.company.FirstCategory.containsWithFirstCategoryValue;
import static springsideproject1.springsideproject1build.domain.entity.company.FirstCategory.convertToFirstCategory;
import static springsideproject1.springsideproject1build.domain.entity.company.SecondCategory.containsWithSecondCategoryValue;
import static springsideproject1.springsideproject1build.domain.entity.company.SecondCategory.convertToSecondCategory;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.SUBJECT_FIRST_CATEGORY;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.SUBJECT_SECOND_CATEGORY;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.ADD_INDUSTRY_ARTICLE_WITH_STRING_URL;

@NonNullApi
@WebFilter(urlPatterns = ADD_INDUSTRY_ARTICLE_WITH_STRING_URL)
@Order(2)
public class StringIndustryArticleSupportFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest requestBefore, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(getModifiableHttpServletRequest(requestBefore), response);
    }

    private ModifiableHttpServletRequest getModifiableHttpServletRequest(HttpServletRequest requestBefore) {
        ModifiableHttpServletRequest request = new ModifiableHttpServletRequest(requestBefore);

        String subjectFirstCategory = request.getParameter(SUBJECT_FIRST_CATEGORY);
        if (subjectFirstCategory != null) {
            request.setParameter(SUBJECT_FIRST_CATEGORY, subjectFirstCategory.toUpperCase());
            if (containsWithFirstCategoryValue(subjectFirstCategory))
                request.setParameter(SUBJECT_FIRST_CATEGORY, convertToFirstCategory(subjectFirstCategory).name());
        }

        String subjectSecondCategory = request.getParameter(SUBJECT_SECOND_CATEGORY);
        if (subjectSecondCategory != null) {
            request.setParameter(SUBJECT_SECOND_CATEGORY, subjectSecondCategory.toUpperCase());
            if (containsWithSecondCategoryValue(subjectSecondCategory))
                request.setParameter(SUBJECT_SECOND_CATEGORY, convertToSecondCategory(subjectSecondCategory).name());
        }

        return request;
    }
}
