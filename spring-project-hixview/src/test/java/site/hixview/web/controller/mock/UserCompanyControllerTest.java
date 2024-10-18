package site.hixview.web.controller.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.service.CompanyService;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.CompanyTestUtils;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.name.EntityName.Company.COMPANY;
import static site.hixview.domain.vo.name.ViewName.VIEW_SUB;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.RequestUrl.COMPANY_SEARCH_URL;
import static site.hixview.domain.vo.user.RequestUrl.COMPANY_SUB_URL;
import static site.hixview.domain.vo.user.ViewName.COMPANY_VIEW;

@OnlyRealControllerContext
class UserCompanyControllerTest implements CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyService companyService;

    @DisplayName("기업 서브 페이지 접속")
    @Test
    void accessCompanySubPage() throws Exception {
        mockMvc.perform(get(COMPANY_SUB_URL))
                .andExpectAll(status().isOk(),
                        view().name(COMPANY_VIEW + VIEW_SUB),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT));
    }

    @DisplayName("기업 검색")
    @Test
    void searchCompany() throws Exception {
        // given
        Company company = samsungElectronics;
        when(companyService.findCompanyByCode(company.getCode())).thenReturn(Optional.of(company));
        doNothing().when(companyService).registerCompany(company);
        ObjectMapper objectMapper = new ObjectMapper();

        // when
        companyService.registerCompany(company);

        // then
        assertThat(Objects.requireNonNull(mockMvc.perform(get(COMPANY_SEARCH_URL + company.getCode()))
                .andExpect(status().isOk())
                .andReturn().getModelAndView()).getModelMap().getAttribute(COMPANY))
                .usingRecursiveComparison().isEqualTo(company);
    }
}