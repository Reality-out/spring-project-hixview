package site.hixview.web.filter;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import site.hixview.domain.entity.Country;
import site.hixview.domain.entity.FirstCategory;
import site.hixview.domain.entity.Scale;
import site.hixview.domain.entity.SecondCategory;
import site.hixview.web.request.ModifiableHttpServletRequest;

import java.io.IOException;

import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.manager.RequestURL.ADD_SINGLE_COMPANY_URL;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_COMPANY_URL;
import static site.hixview.domain.vo.name.EntityName.Company.*;
import static site.hixview.util.FilterUtils.applyUppercaseAndConvertToEnumWithString;

@NonNullApi
@WebFilter(urlPatterns = {ADD_SINGLE_COMPANY_URL, UPDATE_COMPANY_URL + FINISH_URL})
@Order(1)
public class CompanyDtoSupportFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest requestBefore, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        ModifiableHttpServletRequest request = new ModifiableHttpServletRequest(requestBefore);
        applyUppercaseAndConvertToEnumWithString(request, Country.class, COUNTRY);
        applyUppercaseAndConvertToEnumWithString(request, Scale.class, SCALE);
        applyUppercaseAndConvertToEnumWithString(request, FirstCategory.class, FIRST_CATEGORY);
        applyUppercaseAndConvertToEnumWithString(request, SecondCategory.class, SECOND_CATEGORY);
        chain.doFilter(request, response);
    }
}
