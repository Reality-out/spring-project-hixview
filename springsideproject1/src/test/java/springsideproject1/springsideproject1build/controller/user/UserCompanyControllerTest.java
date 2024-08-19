package springsideproject1.springsideproject1build.controller.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.company.Company;
import springsideproject1.springsideproject1build.service.CompanyService;
import springsideproject1.springsideproject1build.utility.test.CompanyTestUtility;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.config.constant.LAYOUT_CONFIG.BASIC_LAYOUT_PATH;
import static springsideproject1.springsideproject1build.config.constant.LAYOUT_CONFIG.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.*;
import static springsideproject1.springsideproject1build.utility.ConstantUtils.COMPANY;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserCompanyControllerTest implements CompanyTestUtility {

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
        resetTable(jdbcTemplateTest, companyTable);
    }

    @DisplayName("기업 서브 페이지 접속")
    @Test
    public void accessCompanySubPage() throws Exception {
        mockMvc.perform(get("/company"))
                .andExpectAll(status().isOk(),
                        view().name(USER_COMPANY_VIEW + VIEW_SUB_SUFFIX),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT_PATH));
    }

    @DisplayName("기업 코드로 검색")
    @Test
    public void searchCompanyByCode() throws Exception {
        // given
        Company company = createSamsungElectronics();

        // when
        companyService.registerCompany(company);

        // then
        assertThat(requireNonNull(mockMvc.perform(get("/company/" + company.getCode()))
                .andExpectAll(status().isOk(),
                        view().name(USER_COMPANY_VIEW + VIEW_SHOW_SUFFIX),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT_PATH))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(company);
    }

    @DisplayName("기업 이름으로 검색")
    @Test
    public void searchCompanyByName() throws Exception {
        // given
        Company company = createSamsungElectronics();

        // when
        companyService.registerCompany(company);

        // then
        assertThat(requireNonNull(mockMvc.perform(get("/company/" + company.getName()))
                .andExpectAll(status().isOk(),
                        view().name(USER_COMPANY_VIEW + VIEW_SHOW_SUFFIX),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT_PATH))
                .andReturn().getModelAndView()).getModelMap().get(COMPANY))
                .usingRecursiveComparison()
                .isEqualTo(company);
    }
}