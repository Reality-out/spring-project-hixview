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
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticle;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticleBufferSimple;
import springsideproject1.springsideproject1build.domain.service.IndustryArticleService;
import springsideproject1.springsideproject1build.util.test.IndustryArticleTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;

import static java.util.Objects.requireNonNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static springsideproject1.springsideproject1build.domain.vo.EXCEPTION_STRING.*;
import static springsideproject1.springsideproject1build.domain.vo.CLASS.SUBJECT_FIRST_CATEGORY;
import static springsideproject1.springsideproject1build.domain.vo.CLASS.SUBJECT_SECOND_CATEGORY;
import static springsideproject1.springsideproject1build.domain.vo.DATABASE.TEST_INDUSTRY_ARTICLE_TABLE;
import static springsideproject1.springsideproject1build.domain.vo.LAYOUT.*;
import static springsideproject1.springsideproject1build.domain.vo.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.vo.VIEW_NAME.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class IndustryArticleErrorHandleTest implements IndustryArticleTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    IndustryArticleService articleService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public IndustryArticleErrorHandleTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_INDUSTRY_ARTICLE_TABLE, true);
    }

    @DisplayName("기사 리스트의 크기가 링크 리스트의 크기보다 큰, 문자열을 사용하는 산업 기사들 추가")
    @Test
    public void articleListBiggerIndustryArticleAddWithString() throws Exception {
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, testIndustryArticleStringBuffer.getNameDatePressString());
                    put(linkString, testEqualDateIndustryArticle.getLink());
                    put(SUBJECT_FIRST_CATEGORY, testIndustryArticleStringBuffer.getSubjectFirstCategory());
                    put(SUBJECT_SECOND_CATEGORY, testIndustryArticleStringBuffer.getSubjectSecondCategory());
                }}))
                .andExpectAll(view().name(addStringIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, INDEX_OUT_OF_BOUND_ERROR)));
    }

    @DisplayName("링크 리스트의 크기가 기사 리스트의 크기보다 큰, 문자열을 사용하는 산업 기사들 추가")
    @Test
    public void linkListBiggerIndustryArticleAddWithString() throws Exception {
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, IndustryArticleBufferSimple.builder().article(testNewIndustryArticle).build().getNameDatePressString());
                    put(linkString, testIndustryArticleStringBuffer.getLinkString());
                    put(SUBJECT_FIRST_CATEGORY, testIndustryArticleStringBuffer.getSubjectFirstCategory());
                    put(SUBJECT_SECOND_CATEGORY, testIndustryArticleStringBuffer.getSubjectSecondCategory());
                }}))
                .andExpectAll(view().name(addStringIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, INDEX_OUT_OF_BOUND_ERROR)));
    }

    @DisplayName("비어 있는 기사를 사용하는, 문자열을 사용하는 산업 기사들 추가")
    @Test
    public void emptyIndustryArticleAddWithString() throws Exception {
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, "");
                    put(linkString, "");
                    put(SUBJECT_FIRST_CATEGORY, testIndustryArticleStringBuffer.getSubjectFirstCategory());
                    put(SUBJECT_SECOND_CATEGORY, testIndustryArticleStringBuffer.getSubjectSecondCategory());
                }}))
                .andExpectAll(view().name(addStringIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, NOT_BLANK_ARTICLE_ERROR)));
    }

    @DisplayName("서식이 올바르지 않은 입력일 값을 포함하는, 문자열을 사용하는 산업 기사들 추가")
    @Test
    public void dateFormatIndustryArticleAddWithString() throws Exception {
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, testEqualDateIndustryArticleStringBuffer.getNameDatePressString().replace("2024", INVALID_VALUE));
                    put(linkString, testEqualDateIndustryArticleStringBuffer.getLinkString());
                    put(SUBJECT_FIRST_CATEGORY, testIndustryArticleStringBuffer.getSubjectFirstCategory());
                    put(SUBJECT_SECOND_CATEGORY, testIndustryArticleStringBuffer.getSubjectSecondCategory());
                }}))
                .andExpectAll(view().name(
                                URL_REDIRECT_PREFIX + ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX),
                        model().attribute(IS_BEAN_VALIDATION_ERROR, String.valueOf(false)),
                        model().attribute(ERROR_SINGLE, NUMBER_FORMAT_LOCAL_DATE_ERROR)));
    }

    @DisplayName("존재하지 않는 기사 번호 또는 기사명을 사용하여 산업 기사를 검색하는, 산업 기사 변경")
    @Test
    public void NotFoundNumberOrNameIndustryArticleModify() throws Exception {
        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_INDUSTRY_ARTICLE_URL, "numberOrName", ""))
                .andExpectAll(view().name(UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, NOT_FOUND_INDUSTRY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_INDUSTRY_ARTICLE_URL, "numberOrName", "1"))
                .andExpectAll(view().name(UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, NOT_FOUND_INDUSTRY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(UPDATE_INDUSTRY_ARTICLE_URL, "numberOrName", INVALID_VALUE))
                .andExpectAll(view().name(UPDATE_INDUSTRY_ARTICLE_VIEW + VIEW_BEFORE_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, NOT_FOUND_INDUSTRY_ARTICLE_ERROR)));
    }

    @DisplayName("기사명 또는 기사 링크까지 변경을 시도하는, 산업 기사 변경")
    @Test
    public void changeNameOrLinkIndustryArticleModify() throws Exception {
        // given & when
        IndustryArticle article = articleService.registerArticle(testIndustryArticle);

        // then
        requireNonNull(mockMvc.perform(postWithIndustryArticle(modifyIndustryArticleFinishUrl,
                        IndustryArticle.builder().article(article).name(testNewIndustryArticle.getName()).build()))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, (String) null)));

        requireNonNull(mockMvc.perform(postWithIndustryArticle(modifyIndustryArticleFinishUrl,
                        IndustryArticle.builder().article(article).link(testNewIndustryArticle.getLink()).build()))
                .andExpectAll(view().name(modifyIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, UPDATE_PROCESS_PATH),
                        model().attribute(ERROR, (String) null)));
    }

    @DisplayName("존재하지 않는 기사 번호 또는 기사명을 사용하는, 산업 기사 없애기")
    @Test
    public void NotFoundArticleNumberOrNameIndustryArticleRid() throws Exception {
        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_INDUSTRY_ARTICLE_URL, "numberOrName", ""))
                .andExpectAll(view().name(REMOVE_INDUSTRY_ARTICLE_VIEW + VIEW_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_PATH),
                        model().attribute(ERROR, NOT_FOUND_INDUSTRY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_INDUSTRY_ARTICLE_URL, "numberOrName", "1"))
                .andExpectAll(view().name(REMOVE_INDUSTRY_ARTICLE_VIEW + VIEW_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_PATH),
                        model().attribute(ERROR, NOT_FOUND_INDUSTRY_ARTICLE_ERROR)));

        requireNonNull(mockMvc.perform(postWithSingleParam(REMOVE_INDUSTRY_ARTICLE_URL, "numberOrName", INVALID_VALUE))
                .andExpectAll(view().name(REMOVE_INDUSTRY_ARTICLE_VIEW + VIEW_PROCESS_SUFFIX),
                        model().attribute(LAYOUT_PATH, REMOVE_PROCESS_PATH),
                        model().attribute(ERROR, NOT_FOUND_INDUSTRY_ARTICLE_ERROR)));
    }
}
