package site.hixview.web.filter.support;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import site.hixview.domain.entity.Country;
import site.hixview.web.request.ModifiableHttpServletRequest;

import java.io.IOException;

import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.SUBJECT_COUNTRY;
import static site.hixview.domain.vo.manager.RequestURL.ADD_SINGLE_ECONOMY_ARTICLE_URL;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_ECONOMY_ARTICLE_URL;
import static site.hixview.util.FilterUtils.applyUppercaseAndConvertToEnumWithString;

@NonNullApi
@WebFilter(urlPatterns = {ADD_SINGLE_ECONOMY_ARTICLE_URL, UPDATE_ECONOMY_ARTICLE_URL + FINISH_URL})
@Order(2)
public class EconomyArticleDtoSupportFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest requestBefore, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        ModifiableHttpServletRequest request = new ModifiableHttpServletRequest(requestBefore);
        applyUppercaseAndConvertToEnumWithString(request, Country.class, SUBJECT_COUNTRY);
        chain.doFilter(request, response);
    }
}
