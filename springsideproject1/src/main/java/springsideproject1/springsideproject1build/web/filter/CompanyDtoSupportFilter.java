package springsideproject1.springsideproject1build.web.filter;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import springsideproject1.springsideproject1build.domain.entity.Country;
import springsideproject1.springsideproject1build.domain.entity.FirstCategory;
import springsideproject1.springsideproject1build.domain.entity.Scale;
import springsideproject1.springsideproject1build.domain.entity.SecondCategory;
import springsideproject1.springsideproject1build.web.request.ModifiableHttpServletRequest;

import java.io.IOException;

import static springsideproject1.springsideproject1build.domain.vo.CLASS.*;
import static springsideproject1.springsideproject1build.domain.vo.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.util.FilterUtils.applyUppercaseAndConvertToEnum;

@NonNullApi
@WebFilter(urlPatterns = {ADD_SINGLE_COMPANY_URL, UPDATE_COMPANY_URL + URL_FINISH_SUFFIX})
@Order(1)
public class CompanyDtoSupportFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest requestBefore, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        ModifiableHttpServletRequest request = new ModifiableHttpServletRequest(requestBefore);
        applyUppercaseAndConvertToEnum(request, Country.class, COUNTRY);
        applyUppercaseAndConvertToEnum(request, Scale.class, SCALE);
        applyUppercaseAndConvertToEnum(request, FirstCategory.class, FIRST_CATEGORY);
        applyUppercaseAndConvertToEnum(request, SecondCategory.class, SECOND_CATEGORY);
        chain.doFilter(request, response);
    }
}
