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

import static springsideproject1.springsideproject1build.domain.entity.Country.containedWithCountryValue;
import static springsideproject1.springsideproject1build.domain.entity.Country.convertToCountry;
import static springsideproject1.springsideproject1build.domain.entity.FirstCategory.containedWithFirstCategoryValue;
import static springsideproject1.springsideproject1build.domain.entity.FirstCategory.convertToFirstCategory;
import static springsideproject1.springsideproject1build.domain.entity.Scale.containedWithScaleValue;
import static springsideproject1.springsideproject1build.domain.entity.Scale.convertToScale;
import static springsideproject1.springsideproject1build.domain.entity.SecondCategory.containedWithSecondCategoryValue;
import static springsideproject1.springsideproject1build.domain.entity.SecondCategory.convertToSecondCategory;
import static springsideproject1.springsideproject1build.domain.vo.CLASS.*;
import static springsideproject1.springsideproject1build.domain.vo.REQUEST_URL.*;

@NonNullApi
@WebFilter(urlPatterns = {ADD_SINGLE_COMPANY_URL, UPDATE_COMPANY_URL + URL_FINISH_SUFFIX})
@Order(1)
public class CompanyDtoSupportFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest requestBefore, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(getModifiableHttpServletRequest(requestBefore), response);
    }

    private ModifiableHttpServletRequest getModifiableHttpServletRequest(HttpServletRequest requestBefore) {
        ModifiableHttpServletRequest request = new ModifiableHttpServletRequest(requestBefore);

        String country = request.getParameter(COUNTRY);
        if (country != null) {
            request.setParameter(COUNTRY, country.toUpperCase());
            if (containedWithCountryValue(country)) {
                request.setParameter(COUNTRY, convertToCountry(country).name());
            }
        }

        String scale = request.getParameter(SCALE);
        if (scale != null) {
            request.setParameter(SCALE, scale.toUpperCase());
            if (containedWithScaleValue(scale))
                request.setParameter(SCALE, convertToScale(scale).name());
        }

        String firstCategory = request.getParameter(FIRST_CATEGORY);
        if (firstCategory != null) {
            request.setParameter(FIRST_CATEGORY, firstCategory.toUpperCase());
            if (containedWithFirstCategoryValue(firstCategory))
                request.setParameter(FIRST_CATEGORY, convertToFirstCategory(firstCategory).name());
        }

        String secondCategory = request.getParameter(SECOND_CATEGORY);
        if (secondCategory != null) {
            request.setParameter(SECOND_CATEGORY, secondCategory.toUpperCase());
            if (containedWithSecondCategoryValue(secondCategory))
                request.setParameter(SECOND_CATEGORY, convertToSecondCategory(secondCategory).name());
        }

        return request;
    }
}
