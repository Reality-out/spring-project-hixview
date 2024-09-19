package site.hixview.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.service.CompanyService;
import site.hixview.util.test.CompanyTestUtils;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.name.EntityName.Company.COMPANY;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANIES_SCHEMA;
import static site.hixview.domain.vo.name.ViewName.VIEW_SHOW;
import static site.hixview.domain.vo.name.ViewName.VIEW_SUB;
import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.RequestUrl.COMPANY_SEARCH_URL;
import static site.hixview.domain.vo.user.RequestUrl.COMPANY_SUB_URL;
import static site.hixview.domain.vo.user.ViewName.COMPANY_VIEW;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserCompanyControllerTest implements CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public UserCompanyControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANIES_SCHEMA);
    }

    @DisplayName("기업 서브 페이지 접속")
    @Test
    public void accessCompanySubPage() throws Exception {
        mockMvc.perform(get(COMPANY_SUB_URL))
                .andExpectAll(status().isOk(),
                        view().name(COMPANY_VIEW + VIEW_SUB),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT),
                        model().attribute("companySearch", COMPANY_SEARCH_URL));
    }

    @DisplayName("기업 검색")
    @Test
    public void searchCompany() throws Exception {
        // given
        Company company = samsungElectronics;

        // when
        companyService.registerCompany(company);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(COMPANY_SEARCH_URL + company.getCode()))
                .andExpectAll(status().isOk(),
                        view().name(COMPANY_VIEW + VIEW_SHOW),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(company);

        assertThat(requireNonNull(mockMvc.perform(get(COMPANY_SEARCH_URL + company.getName()))
                .andExpectAll(status().isOk(),
                        view().name(COMPANY_VIEW + VIEW_SHOW),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(company);
    }
}