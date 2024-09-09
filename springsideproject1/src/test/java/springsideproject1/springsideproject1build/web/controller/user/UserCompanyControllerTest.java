package springsideproject1.springsideproject1build.web.controller.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.company.Company;
import springsideproject1.springsideproject1build.domain.service.CompanyService;
import springsideproject1.springsideproject1build.util.test.CompanyTestUtils;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.COMPANY;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_COMPANY_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.BASIC_LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.COMPANY_SEARCH_URL;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.COMPANY_SUB_URL;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;

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
        resetTable(jdbcTemplateTest, TEST_COMPANY_TABLE);
    }

    @DisplayName("기업 서브 페이지 접속")
    @Test
    public void accessCompanySubPage() throws Exception {
        mockMvc.perform(get(COMPANY_SUB_URL))
                .andExpectAll(status().isOk(),
                        view().name(USER_COMPANY_VIEW + VIEW_SUB_SUFFIX),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT_PATH),
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
                        view().name(USER_COMPANY_VIEW + VIEW_SHOW_SUFFIX),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT_PATH))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(company);

        assertThat(requireNonNull(mockMvc.perform(get(COMPANY_SEARCH_URL + company.getName()))
                .andExpectAll(status().isOk(),
                        view().name(USER_COMPANY_VIEW + VIEW_SHOW_SUFFIX),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT_PATH))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(company);
    }
}