package site.hixview.domain.error.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.service.CompanyService;
import site.hixview.util.test.CompanyTestUtils;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.Word.ERROR;
import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.manager.Layout.REMOVE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.REMOVE_COMPANY_URL;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_COMPANY_URL;
import static site.hixview.domain.vo.manager.ViewName.REMOVE_COMPANY_URL_VIEW;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_COMPANY_VIEW;
import static site.hixview.domain.vo.name.ExceptionName.NOT_FOUND_COMPANY_ERROR;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANIES_SCHEMA;
import static site.hixview.domain.vo.name.ViewName.VIEW_BEFORE_PROCESS;
import static site.hixview.domain.vo.name.ViewName.VIEW_PROCESS;

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
    public void notFoundCodeOrNameCompanyModify() throws Exception {
        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_URL, "codeOrName", ""))
                .andExpectAll(view().name(UPDATE_COMPANY_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_URL, "codeOrName", "000000"))
                .andExpectAll(view().name(UPDATE_COMPANY_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_COMPANY_URL, "codeOrName", INVALID_VALUE))
                .andExpectAll(view().name(UPDATE_COMPANY_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));
    }

    @DisplayName("존재하지 않는 기업 코드 또는 기업명을 사용하는, 기업 없애기")
    @Test
    public void notFoundCodeOrNameCompanyRid() throws Exception {
        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_URL, "codeOrName", ""))
                .andExpectAll(view().name(REMOVE_COMPANY_URL_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_URL, "codeOrName", "000000"))
                .andExpectAll(view().name(REMOVE_COMPANY_URL_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_COMPANY_URL, "codeOrName", INVALID_VALUE))
                .andExpectAll(view().name(REMOVE_COMPANY_URL_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_COMPANY_ERROR)));
    }
}
