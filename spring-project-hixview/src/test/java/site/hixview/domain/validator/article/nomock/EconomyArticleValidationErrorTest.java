package site.hixview.domain.validator.article.nomock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.entity.article.EconomyArticleBufferSimple;
import site.hixview.domain.entity.article.dto.EconomyArticleDto;
import site.hixview.domain.service.EconomyArticleService;
import site.hixview.support.property.TestSchemaName;
import site.hixview.support.util.EconomyArticleTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.RequestPath.FINISH_PATH;
import static site.hixview.domain.vo.RequestPath.RELATIVE_REDIRECT_PATH;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.RequestPath.ADD_ECONOMY_ARTICLE_WITH_STRING_PATH;
import static site.hixview.domain.vo.name.ExceptionName.IS_BEAN_VALIDATION_ERROR;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
@AutoConfigureMockMvc
@TestSchemaName
@Transactional
class EconomyArticleValidationErrorTest implements EconomyArticleTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EconomyArticleService articleService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    EconomyArticleValidationErrorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_ECONOMY_ARTICLES_SCHEMA, true);
    }

    @DisplayName("기사 입력일이 유효하지 않은, 문자열을 사용하는 경제 기사들 추가")
    @Test
    void invalidDateEconomyArticleAddWithString() throws Exception {
        // given & when
        EconomyArticleDto articleDto = createTestEconomyArticleDto();
        EconomyArticleBufferSimple articleBuffer = EconomyArticleBufferSimple.builder()
                .nameDatePressString(testEconomyArticleBuffer.getNameDatePressString()
                        .replace("2024", "2000")
                        .replace("8", "2"))
                .linkString(testEconomyArticleBuffer.getLinkString())
                .importance(testEconomyArticleBuffer.getImportance())
                .subjectCountry(testEconomyArticleBuffer.getSubjectCountry())
                .targetEconomyContents(testEconomyArticleBuffer.getTargetEconomyContents()).build();

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_ECONOMY_ARTICLE_WITH_STRING_PATH, new HashMap<>() {{
                    put(nameDatePressString, articleBuffer.getNameDatePressString());
                    put(linkString, articleBuffer.getLinkString());
                    put(SUBJECT_COUNTRY, articleBuffer.getSubjectCountry());
                    put(TARGET_ECONOMY_CONTENT, articleBuffer.getTargetEconomyContents());
                }}))
                .andExpectAll(view().name(
                                RELATIVE_REDIRECT_PATH + ADD_ECONOMY_ARTICLE_WITH_STRING_PATH + FINISH_PATH),
                        model().attribute(IS_BEAN_VALIDATION_ERROR, String.valueOf(false)),
                        model().attribute(ERROR_SINGLE, (String) null)));
    }

    @DisplayName("중복 기사명 또는 기사 링크를 사용하는, 문자열을 사용하는 경제 기사들 추가")
    @Test
    void duplicatedNameOrLinkEconomyArticleAddWithString() throws Exception {
        // given
        EconomyArticleBufferSimple articleBufferDuplicatedName = EconomyArticleBufferSimple.builder().article(EconomyArticle.builder()
                .article(testEqualDateEconomyArticle).link(testEconomyArticle.getLink()).build()).build();
        EconomyArticleBufferSimple articleBufferDuplicatedLink = EconomyArticleBufferSimple.builder().article(EconomyArticle.builder()
                .article(testEqualDateEconomyArticle).name(testEconomyArticle.getName()).build()).build();

        // when
        articleService.registerArticle(testEqualDateEconomyArticle);

        // then
        for (EconomyArticleBufferSimple articleBuffer : List.of(articleBufferDuplicatedName, articleBufferDuplicatedLink)) {
            requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_ECONOMY_ARTICLE_WITH_STRING_PATH, new HashMap<>() {{
                        put(nameDatePressString, articleBuffer.getNameDatePressString());
                        put(linkString, articleBuffer.getLinkString());
                        put(SUBJECT_COUNTRY, articleBuffer.getSubjectCountry());
                        put(TARGET_ECONOMY_CONTENT, articleBuffer.getTargetEconomyContents());
                    }}))
                    .andExpectAll(view().name(
                                    RELATIVE_REDIRECT_PATH + ADD_ECONOMY_ARTICLE_WITH_STRING_PATH + FINISH_PATH),
                            model().attribute(IS_BEAN_VALIDATION_ERROR, String.valueOf(false)),
                            model().attribute(ERROR_SINGLE, (String) null)));
        }
    }
}
