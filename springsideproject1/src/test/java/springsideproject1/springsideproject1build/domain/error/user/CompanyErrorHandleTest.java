package springsideproject1.springsideproject1build.domain.error.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.service.CompanyService;
import springsideproject1.springsideproject1build.util.test.CompanyTestUtils;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static springsideproject1.springsideproject1build.domain.vo.EXCEPTION_STRING.*;
import static springsideproject1.springsideproject1build.domain.vo.DATABASE.TEST_COMPANY_TABLE;
import static springsideproject1.springsideproject1build.domain.vo.REQUEST_URL.COMPANY_SEARCH_URL;
import static springsideproject1.springsideproject1build.domain.vo.REQUEST_URL.COMPANY_SUB_URL;

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
        resetTable(jdbcTemplateTest, TEST_COMPANY_TABLE);
    }

    @DisplayName("비어 있는 값을 사용하는 기업 검색")
    @Test
    public void emptyCompanySearch() throws Exception {
        requireNonNull(mockMvc.perform(getWithNoParam(COMPANY_SEARCH_URL))
                .andExpectAll(redirectedUrl(COMPANY_SUB_URL),
                        flash().attribute(ERROR, NOT_EXIST_COMPANY_ERROR)));
    }

    @DisplayName("존재하지 않는 기업 코드 또는 기업명을 사용하는 기업 검색")
    @Test
    public void NotFoundCodeOrNameCompanySearch() throws Exception {
        requireNonNull(mockMvc.perform(getWithNoParam(COMPANY_SEARCH_URL + "000000"))
                .andExpectAll(redirectedUrl(COMPANY_SUB_URL),
                        flash().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));

        requireNonNull(mockMvc.perform(getWithNoParam(COMPANY_SEARCH_URL + INVALID_VALUE))
                .andExpectAll(redirectedUrl(COMPANY_SUB_URL),
                        flash().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));
    }
}
