package site.hixview.web.controller.nomock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.entity.article.IndustryArticleBufferSimple;
import site.hixview.domain.service.IndustryArticleService;
import site.hixview.support.property.TestSchemaName;
import site.hixview.support.util.IndustryArticleTestUtils;
import site.hixview.util.ControllerUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.ADD_FINISH_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_INDUSTRY_ARTICLE_WITH_STRING_URL;
import static site.hixview.domain.vo.manager.ViewName.ADD_INDUSTRY_ARTICLE_VIEW;
import static site.hixview.domain.vo.name.ExceptionName.IS_BEAN_VALIDATION_ERROR;
import static site.hixview.domain.vo.name.ViewName.VIEW_MULTIPLE_FINISH;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
@AutoConfigureMockMvc
@TestSchemaName
@Transactional
public class ManagerIndustryArticleControllerTest implements IndustryArticleTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IndustryArticleService industryArticleService;

    private final JdbcTemplate jdbcTemplateTest;
    private final String[] fieldNames;

    @Autowired
    ManagerIndustryArticleControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
        fieldNames = IndustryArticle.getFieldNamesWithNoNumber();
    }

    @BeforeEach
    void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_INDUSTRY_ARTICLES_SCHEMA, true);
    }

    @DisplayName("문자열을 사용하는 산업 기사들 추가 페이지 접속")
    @Test
    void accessIndustryArticleAddWithString() throws Exception {
        mockMvc.perform(get(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL))
                .andExpectAll(status().isOk(),
                        view().name(addStringIndustryArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT));
    }

    @DisplayName("문자열을 사용하는 산업 기사들 추가 완료 페이지 접속")
    @Test
    void accessIndustryArticleAddWithStringFinish() throws Exception {
        // given
        IndustryArticle article1 = testEqualDateIndustryArticle;
        IndustryArticle article2 = testNewIndustryArticle;
        IndustryArticleBufferSimple articleBufferOriginal = testIndustryArticleBuffer;
        IndustryArticleBufferSimple articleBufferAddFaultNameDatePress = IndustryArticleBufferSimple.builder()
                .articleBuffer(articleBufferOriginal)
                .nameDatePressString(IndustryArticleBufferSimple.builder().article(testIndustryArticle).build()
                        .getNameDatePressString().replace("2024-", "")).build();

        List<String> nameList = Stream.of(article1, article2)
                .map(IndustryArticle::getName).collect(Collectors.toList());
        String nameListForURL = toStringForUrl(nameList);
        String nameListString = "nameList";

        // then
        for (IndustryArticleBufferSimple articleBuffer : List.of(articleBufferOriginal, articleBufferAddFaultNameDatePress)) {
            ModelMap modelMapPost = requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                        put(nameDatePressString, articleBuffer.getNameDatePressString());
                        put(linkString, articleBuffer.getLinkString());
                        put(SUBJECT_FIRST_CATEGORY, articleBuffer.getSubjectFirstCategory());
                        put(SUBJECT_SECOND_CATEGORY, articleBuffer.getParsedArticles().getFirst().getSubjectSecondCategories().getFirst().name());
                    }}))
                    .andExpectAll(status().isFound(),
                            redirectedUrlPattern(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + FINISH_URL + ALL_QUERY_STRING))
                    .andReturn().getModelAndView()).getModelMap();

            assertThat(modelMapPost.get(nameListString)).usingRecursiveComparison().isEqualTo(nameListForURL);
            assertThat(modelMapPost.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(String.valueOf(false));
            assertThat(modelMapPost.get(ERROR_SINGLE)).isEqualTo(null);

            industryArticleService.removeArticleByName(article1.getName());
            industryArticleService.removeArticleByName(article2.getName());
        }

        industryArticleService.registerArticle(article1);
        industryArticleService.registerArticle(article2);

        ModelMap modelMapGet = requireNonNull(mockMvc.perform(getWithMultipleParam(
                        ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + FINISH_URL,
                        new HashMap<>() {{
                            put(nameListString, nameListForURL);
                            put(IS_BEAN_VALIDATION_ERROR, String.valueOf(false));
                            put(ERROR_SINGLE, null);
                        }}))
                .andExpectAll(status().isOk(),
                        view().name(ADD_INDUSTRY_ARTICLE_VIEW + VIEW_MULTIPLE_FINISH),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_LAYOUT),
                        model().attribute("repeatUrl", ADD_INDUSTRY_ARTICLE_WITH_STRING_URL),
                        model().attribute(nameListString, ControllerUtils.decodeWithUTF8(nameList)))
                .andReturn().getModelAndView()).getModelMap();

        assertThat(modelMapGet.get(nameListString)).usingRecursiveComparison().isEqualTo(ControllerUtils.decodeWithUTF8(nameList));
        assertThat(modelMapGet.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(false);
        assertThat(modelMapGet.get(ERROR_SINGLE)).isEqualTo(null);

        assertThat(industryArticleService.findArticleByName(nameList.getFirst()).orElseThrow())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(article1);

        assertThat(industryArticleService.findArticleByName(nameList.getLast()).orElseThrow())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(article2);
    }
}
