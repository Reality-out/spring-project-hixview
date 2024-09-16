package springsideproject1.springsideproject1build.domain.error.manager;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static springsideproject1.springsideproject1build.domain.vo.ExceptionString.NOT_FOUND_COMPANY_ERROR;
import static springsideproject1.springsideproject1build.domain.vo.SchemaName.TEST_COMPANIES_SCHEMA;
import static springsideproject1.springsideproject1build.domain.vo.ViewName.BEFORE_PROCESS_VIEW;
import static springsideproject1.springsideproject1build.domain.vo.ViewName.PROCESS_VIEW;
import static springsideproject1.springsideproject1build.domain.vo.Word.ERROR;
import static springsideproject1.springsideproject1build.domain.vo.Word.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.vo.manager.Layout.REMOVE_PROCESS_LAYOUT;
import static springsideproject1.springsideproject1build.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static springsideproject1.springsideproject1build.domain.vo.manager.RequestUrl.REMOVE_COMPANY_URL;
import static springsideproject1.springsideproject1build.domain.vo.manager.RequestUrl.UPDATE_COMPANY_URL;
import static springsideproject1.springsideproject1build.domain.vo.manager.ViewName.REMOVE_COMPANY_URL_VIEW;
import static springsideproject1.springsideproject1build.domain.vo.manager.ViewName.UPDATE_COMPANY_VIEW;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CompanyErrorHandleTest implements CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyErrorHandleTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANIES_SCHEMA);
    }

    @DisplayName("존재하지 않는 기업 코드 또는 기업명을 사용하여 기업을 검색하는, 기업 변경")
    @Test
    public void NotFoundCodeOrNameCompanyModify() throws Exception {
        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_URL, "codeOrName", ""))
                .andExpectAll(view().name(UPDATE_COMPANY_VIEW + BEFORE_PROCESS_VIEW),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_URL, "codeOrName", "000000"))
                .andExpectAll(view().name(UPDATE_COMPANY_VIEW + BEFORE_PROCESS_VIEW),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_URL, "codeOrName", INVALID_VALUE))
                .andExpectAll(view().name(UPDATE_COMPANY_VIEW + BEFORE_PROCESS_VIEW),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));
    }

    @DisplayName("기업 코드 또는 기업명까지 변경을 시도하는, 기업 변경")
    @Test
    public void changeCodeOrNameCompanyModify() throws Exception {
        // given & when
        companyService.registerCompany(samsungElectronics);

        // then
        requireNonNull(mockMvc.perform(postWithCompany(modifyCompanyFinishUrl,
                        Company.builder().company(samsungElectronics).code(skHynix.getCode()).build()))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null)));

        requireNonNull(mockMvc.perform(postWithCompany(modifyCompanyFinishUrl,
                        Company.builder().company(samsungElectronics).name(skHynix.getName()).build()))
                .andExpectAll(view().name(modifyCompanyProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, (String) null)));
    }

    @DisplayName("존재하지 않는 기업 코드 또는 기업명을 사용하는, 기업 없애기")
    @Test
    public void NotFoundCodeOrNameCompanyRid() throws Exception {
        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_URL, "codeOrName", ""))
                .andExpectAll(view().name(REMOVE_COMPANY_URL_VIEW + PROCESS_VIEW),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_URL, "codeOrName", "000000"))
                .andExpectAll(view().name(REMOVE_COMPANY_URL_VIEW + PROCESS_VIEW),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_URL, "codeOrName", INVALID_VALUE))
                .andExpectAll(view().name(REMOVE_COMPANY_URL_VIEW + PROCESS_VIEW),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));
    }
}
