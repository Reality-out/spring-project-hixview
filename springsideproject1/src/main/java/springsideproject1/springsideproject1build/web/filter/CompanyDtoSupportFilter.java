package springsideproject1.springsideproject1build.web.filter;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import springsideproject1.springsideproject1build.web.request.ModifiableHttpServletRequest;

import java.io.IOException;

import static springsideproject1.springsideproject1build.domain.entity.company.Country.containsWithCountryValue;
import static springsideproject1.springsideproject1build.domain.entity.company.Country.convertToCountry;
import static springsideproject1.springsideproject1build.domain.entity.company.FirstCategory.containsWithFirstCategoryValue;
import static springsideproject1.springsideproject1build.domain.entity.company.FirstCategory.convertToFirstCategory;
import static springsideproject1.springsideproject1build.domain.entity.company.Scale.containsWithScaleValue;
import static springsideproject1.springsideproject1build.domain.entity.company.Scale.convertToScale;
import static springsideproject1.springsideproject1build.domain.entity.company.SecondCategory.containsWithSecondCategoryValue;
import static springsideproject1.springsideproject1build.domain.entity.company.SecondCategory.convertToSecondCategory;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.*;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;

@NonNullApi
@WebFilter(urlPatterns = {ADD_SINGLE_COMPANY_URL, UPDATE_COMPANY_URL + URL_FINISH_SUFFIX})
public class CompanyDtoSupportFilter extends OncePerRequestFilter {


    @Override
    public void doFilterInternal(HttpServletRequest requestBefore, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(getModifiableHttpServletRequest(requestBefore), response);
    }

    private ModifiableHttpServletRequest getModifiableHttpServletRequest(HttpServletRequest requestBefore) {
        ModifiableHttpServletRequest request = new ModifiableHttpServletRequest(requestBefore);

        String country = request.getParameter(COUNTRY);
        if (country != null){
            request.setParameter(COUNTRY, country.toUpperCase());
            if (containsWithCountryValue(country)) {
                request.setParameter(COUNTRY, convertToCountry(country).name());
            }
        }

        String scale = request.getParameter(SCALE);
        if (scale != null){
            request.setParameter(SCALE, scale.toUpperCase());
            if (containsWithScaleValue(scale))
                request.setParameter(SCALE, convertToScale(scale).name());
        }

        String firstCategory = request.getParameter(FIRST_CATEGORY);
        if (firstCategory != null) {
            request.setParameter(FIRST_CATEGORY, firstCategory.toUpperCase());
            if (containsWithFirstCategoryValue(firstCategory))
                request.setParameter(FIRST_CATEGORY, convertToFirstCategory(firstCategory).name());
        }

        String secondCategory = request.getParameter(SECOND_CATEGORY);
        if (secondCategory != null) {
            request.setParameter(SECOND_CATEGORY, secondCategory.toUpperCase());
            if (containsWithSecondCategoryValue(secondCategory))
                request.setParameter(SECOND_CATEGORY, convertToSecondCategory(secondCategory).name());
        }

        return request;
    }
}
