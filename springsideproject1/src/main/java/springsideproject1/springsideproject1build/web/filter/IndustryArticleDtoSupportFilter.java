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

import static springsideproject1.springsideproject1build.domain.entity.FirstCategory.containedWithFirstCategoryValue;
import static springsideproject1.springsideproject1build.domain.entity.FirstCategory.convertToFirstCategory;
import static springsideproject1.springsideproject1build.domain.entity.SecondCategory.containedWithSecondCategoryValue;
import static springsideproject1.springsideproject1build.domain.vo.CLASS.SUBJECT_FIRST_CATEGORY;
import static springsideproject1.springsideproject1build.domain.vo.CLASS.SUBJECT_SECOND_CATEGORY;
import static springsideproject1.springsideproject1build.domain.vo.REQUEST_URL.*;

@NonNullApi
@WebFilter(urlPatterns = {ADD_SINGLE_INDUSTRY_ARTICLE_URL, UPDATE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX})
@Order(2)
public class IndustryArticleDtoSupportFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest requestBefore, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(getModifiableHttpServletRequest(requestBefore), response);
    }

    private ModifiableHttpServletRequest getModifiableHttpServletRequest(HttpServletRequest requestBefore) {
        ModifiableHttpServletRequest request = new ModifiableHttpServletRequest(requestBefore);

        String subjectFirstCategory = request.getParameter(SUBJECT_FIRST_CATEGORY);
        if (subjectFirstCategory != null) {
            request.setParameter(SUBJECT_FIRST_CATEGORY, subjectFirstCategory.toUpperCase());
            if (containedWithFirstCategoryValue(subjectFirstCategory))
                request.setParameter(SUBJECT_FIRST_CATEGORY, convertToFirstCategory(subjectFirstCategory).name());
        }

        String subjectSecondCategory = request.getParameter(SUBJECT_SECOND_CATEGORY);
        if (subjectSecondCategory != null) {
            request.setParameter(SUBJECT_SECOND_CATEGORY, subjectSecondCategory.toUpperCase());
            if (containedWithSecondCategoryValue(subjectSecondCategory))
                request.setParameter(SUBJECT_SECOND_CATEGORY, convertToFirstCategory(subjectSecondCategory).name());
        }

        return request;
    }
}
