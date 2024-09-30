package site.hixview.domain.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.domain.service.CompanyService;
import site.hixview.support.util.CompanyTestUtils;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static site.hixview.domain.vo.Word.ERROR;
import static site.hixview.domain.vo.name.ExceptionName.NOT_EXIST_COMPANY_ERROR;
import static site.hixview.domain.vo.name.ExceptionName.NOT_FOUND_COMPANY_ERROR;
import static site.hixview.domain.vo.user.RequestUrl.COMPANY_SEARCH_URL;
import static site.hixview.domain.vo.user.RequestUrl.COMPANY_SUB_URL;

@OnlyRealControllerContext
class UserCompanyErrorHandleTest implements CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyService companyService;

    @DisplayName("비어 있는 값을 사용하는 기업 검색")
    @Test
    void emptyCompanySearch() throws Exception {
        requireNonNull(mockMvc.perform(getWithNoParam(COMPANY_SEARCH_URL))
                .andExpectAll(redirectedUrl(COMPANY_SUB_URL),
                        flash().attribute(ERROR, NOT_EXIST_COMPANY_ERROR)));
    }

    @DisplayName("존재하지 않는 기업 코드 또는 기업명을 사용하는 기업 검색")
    @Test
    void notFoundCodeOrNameCompanySearch() throws Exception {
        // given & when
        when(companyService.findCompanyByCodeOrName(any())).thenReturn(Optional.empty());

        // then
        requireNonNull(mockMvc.perform(getWithNoParam(COMPANY_SEARCH_URL + "000000"))
                .andExpectAll(redirectedUrl(COMPANY_SUB_URL),
                        flash().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));

        requireNonNull(mockMvc.perform(getWithNoParam(COMPANY_SEARCH_URL + INVALID_VALUE))
                .andExpectAll(redirectedUrl(COMPANY_SUB_URL),
                        flash().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));
    }
}
