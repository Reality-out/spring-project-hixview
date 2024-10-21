package site.hixview.domain.validator.article.nomock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.entity.article.IndustryArticleBufferSimple;
import site.hixview.domain.entity.article.dto.IndustryArticleDto;
import site.hixview.domain.service.IndustryArticleService;
import site.hixview.support.property.TestSchemaName;
import site.hixview.support.util.IndustryArticleTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.RequestUrl.REDIRECT_URL;
import static site.hixview.domain.vo.Word.ERROR_SINGLE;
import static site.hixview.domain.vo.manager.RequestURL.ADD_INDUSTRY_ARTICLE_WITH_STRING_URL;
import static site.hixview.domain.vo.name.EntityName.Article.SUBJECT_FIRST_CATEGORY;
import static site.hixview.domain.vo.name.EntityName.Article.SUBJECT_SECOND_CATEGORY;
import static site.hixview.domain.vo.name.ExceptionName.IS_BEAN_VALIDATION_ERROR;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
@AutoConfigureMockMvc
@TestSchemaName
@Transactional
class IndustryArticleValidationErrorTest implements IndustryArticleTestUtils {

    private static final Logger log = LoggerFactory.getLogger(IndustryArticleValidationErrorTest.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IndustryArticleService articleService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    IndustryArticleValidationErrorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_INDUSTRY_ARTICLES_SCHEMA, true);
    }

    @DisplayName("기사 입력일이 유효하지 않은, 문자열을 사용하는 산업 기사들 추가")
    @Test
    void invalidDateIndustryArticleAddWithString() throws Exception {
        // given & when
        IndustryArticleDto articleDto = createTestIndustryArticleDto();
        IndustryArticleBufferSimple articleBuffer = IndustryArticleBufferSimple.builder()
                .nameDatePressString(testIndustryArticleBuffer.getNameDatePressString()
                        .replace("2024", "2000")
                        .replace("8", "2"))
                .linkString(testIndustryArticleBuffer.getLinkString())
                .importance(testIndustryArticleBuffer.getImportance())
                .subjectFirstCategory(testIndustryArticleBuffer.getSubjectFirstCategory())
                .subjectSecondCategories(testIndustryArticleBuffer.getSubjectSecondCategories()).build();

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, articleBuffer.getNameDatePressString());
                    put(linkString, articleBuffer.getLinkString());
                    put(SUBJECT_FIRST_CATEGORY, articleBuffer.getSubjectFirstCategory());
                    put(SUBJECT_SECOND_CATEGORY, articleBuffer.getSubjectSecondCategories());
                }}))
                .andExpectAll(view().name(
                                REDIRECT_URL + ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + FINISH_URL),
                        model().attribute(IS_BEAN_VALIDATION_ERROR, String.valueOf(false)),
                        model().attribute(ERROR_SINGLE, (String) null)));
    }

    @DisplayName("중복 기사명 또는 기사 링크를 사용하는, 문자열을 사용하는 산업 기사들 추가")
    @Test
    void duplicatedNameOrLinkIndustryArticleAddWithString() throws Exception {
        // given
        IndustryArticleBufferSimple articleBufferDuplicatedName = IndustryArticleBufferSimple.builder().article(IndustryArticle.builder()
                .article(testEqualDateIndustryArticle).link(testIndustryArticle.getLink()).build()).build();
        IndustryArticleBufferSimple articleBufferDuplicatedLink = IndustryArticleBufferSimple.builder().article(IndustryArticle.builder()
                .article(testEqualDateIndustryArticle).name(testIndustryArticle.getName()).build()).build();

        // when
        articleService.registerArticle(testEqualDateIndustryArticle);

        // then
        for (IndustryArticleBufferSimple articleBuffer : List.of(articleBufferDuplicatedName, articleBufferDuplicatedLink)) {
            requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                        put(nameDatePressString, articleBuffer.getNameDatePressString());
                        put(linkString, articleBuffer.getLinkString());
                        put(SUBJECT_FIRST_CATEGORY, articleBuffer.getSubjectFirstCategory());
                        put(SUBJECT_SECOND_CATEGORY, articleBuffer.getSubjectSecondCategories());
                    }}))
                    .andExpectAll(view().name(
                                    REDIRECT_URL + ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + FINISH_URL),
                            model().attribute(IS_BEAN_VALIDATION_ERROR, String.valueOf(false)),
                            model().attribute(ERROR_SINGLE, (String) null)));
        }
    }
}
