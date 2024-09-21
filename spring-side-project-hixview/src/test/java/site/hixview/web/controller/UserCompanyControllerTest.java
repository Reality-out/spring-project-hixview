package site.hixview.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.service.CompanyService;
import site.hixview.util.test.CompanyTestUtils;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.name.EntityName.Company.COMPANY;
import static site.hixview.domain.vo.name.ViewName.VIEW_SHOW;
import static site.hixview.domain.vo.name.ViewName.VIEW_SUB;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.RequestUrl.COMPANY_SEARCH_URL;
import static site.hixview.domain.vo.user.RequestUrl.COMPANY_SUB_URL;
import static site.hixview.domain.vo.user.ViewName.COMPANY_VIEW;

@SpringBootTest(properties = {"junit.jupiter.execution.parallel.mode.classes.default=concurrent"})
@AutoConfigureMockMvc
@Transactional
@ExtendWith(MockitoExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class UserCompanyControllerTest implements CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserCompanyController userCompanyController;

    @MockBean
    private CompanyService companyService;

    @DisplayName("기업 서브 페이지 접속")
    @Test
    void accessCompanySubPage() throws Exception {
        mockMvc.perform(get(COMPANY_SUB_URL))
                .andExpectAll(status().isOk(),
                        view().name(COMPANY_VIEW + VIEW_SUB),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT),
                        model().attribute("companySearch", COMPANY_SEARCH_URL));
    }

    @DisplayName("기업 검색")
    @Test
    void searchCompany() throws Exception {
        // given
        Company company = samsungElectronics;
        when(companyService.findCompanyByCodeOrName(company.getCode())).thenReturn(Optional.of(company));
        when(companyService.findCompanyByCodeOrName(company.getName())).thenReturn(Optional.of(company));
        doNothing().when(companyService).registerCompany(company);

        // when
        companyService.registerCompany(company);

        // then
        for (String str : List.of(company.getCode(), company.getName())) {
            assertThat(requireNonNull(mockMvc.perform(get(COMPANY_SEARCH_URL + str))
                    .andExpectAll(status().isOk(),
                            view().name(COMPANY_VIEW + VIEW_SHOW),
                            model().attribute(LAYOUT_PATH, BASIC_LAYOUT))
                    .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                    .usingRecursiveComparison()
                    .isEqualTo(company);
        }
    }
}