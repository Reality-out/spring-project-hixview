package site.hixview.domain.error;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;
import site.hixview.util.test.ArticleMainTestUtils;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.Word.ERROR;
import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.manager.Layout.REMOVE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.UPDATE_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.REMOVE_ARTICLE_MAIN_URL;
import static site.hixview.domain.vo.manager.RequestURL.UPDATE_ARTICLE_MAIN_URL;
import static site.hixview.domain.vo.manager.ViewName.REMOVE_ARTICLE_MAIN_VIEW;
import static site.hixview.domain.vo.manager.ViewName.UPDATE_ARTICLE_MAIN_VIEW;
import static site.hixview.domain.vo.name.ExceptionName.NOT_FOUND_ARTICLE_MAIN_ERROR;
import static site.hixview.domain.vo.name.SchemaName.TEST_ARTICLE_MAINS_SCHEMA;
import static site.hixview.domain.vo.name.ViewName.VIEW_BEFORE_PROCESS;
import static site.hixview.domain.vo.name.ViewName.VIEW_PROCESS;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ManagerArticleMainErrorHandleTest implements ArticleMainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleService articleService;

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public ManagerArticleMainErrorHandleTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_ARTICLE_MAINS_SCHEMA, true);
    }

    @DisplayName("존재하지 않는 기사 번호 또는 기사명을 사용하여 기사 메인을 검색하는, 기사 메인 변경")
    @Test
    public void notFoundNumberOrNameArticleMainModify() throws Exception {
        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_ARTICLE_MAIN_URL, "numberOrName", ""))
                .andExpectAll(view().name(UPDATE_ARTICLE_MAIN_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ARTICLE_MAIN_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_ARTICLE_MAIN_URL, "numberOrName", "1"))
                .andExpectAll(view().name(UPDATE_ARTICLE_MAIN_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ARTICLE_MAIN_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_ARTICLE_MAIN_URL, "numberOrName", INVALID_VALUE))
                .andExpectAll(view().name(UPDATE_ARTICLE_MAIN_VIEW + VIEW_BEFORE_PROCESS),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ARTICLE_MAIN_ERROR)));
    }

    @DisplayName("존재하지 않는 기사 번호 또는 기사명을 사용하는, 기사 메인 없애기")
    @Test
    public void notFoundArticleNumberOrNameCompanyArticleRid() throws Exception {
        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_ARTICLE_MAIN_URL, "numberOrName", ""))
                .andExpectAll(view().name(REMOVE_ARTICLE_MAIN_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ARTICLE_MAIN_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_ARTICLE_MAIN_URL, "numberOrName", "1"))
                .andExpectAll(view().name(REMOVE_ARTICLE_MAIN_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ARTICLE_MAIN_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_ARTICLE_MAIN_URL, "numberOrName", INVALID_VALUE))
                .andExpectAll(view().name(REMOVE_ARTICLE_MAIN_VIEW + VIEW_PROCESS),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT),
                        model().attribute(ERROR, NOT_FOUND_ARTICLE_MAIN_ERROR)));
    }
}
