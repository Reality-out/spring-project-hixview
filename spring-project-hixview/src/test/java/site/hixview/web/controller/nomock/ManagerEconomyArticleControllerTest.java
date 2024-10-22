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
import site.hixview.domain.entity.article.EconomyArticle;
import site.hixview.domain.entity.article.EconomyArticleBufferSimple;
import site.hixview.domain.service.EconomyArticleService;
import site.hixview.support.property.TestSchemaName;
import site.hixview.support.util.EconomyArticleTestUtils;
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
import static site.hixview.domain.vo.Word.ERROR_SINGLE;
import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.manager.Layout.ADD_FINISH_LAYOUT;
import static site.hixview.domain.vo.manager.Layout.ADD_PROCESS_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.ADD_ECONOMY_ARTICLE_WITH_STRING_URL;
import static site.hixview.domain.vo.manager.ViewName.ADD_ECONOMY_ARTICLE_VIEW;
import static site.hixview.domain.vo.name.EntityName.Article.SUBJECT_COUNTRY;
import static site.hixview.domain.vo.name.EntityName.Article.TARGET_ECONOMY_CONTENT;
import static site.hixview.domain.vo.name.ExceptionName.IS_BEAN_VALIDATION_ERROR;
import static site.hixview.domain.vo.name.ViewName.VIEW_MULTIPLE_FINISH;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
@AutoConfigureMockMvc
@TestSchemaName
@Transactional
public class ManagerEconomyArticleControllerTest implements EconomyArticleTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EconomyArticleService economyArticleService;

    private final JdbcTemplate jdbcTemplateTest;
    private final String[] fieldNames;

    @Autowired
    ManagerEconomyArticleControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
        fieldNames = EconomyArticle.getFieldNamesWithNoNumber();
    }

    @BeforeEach
    void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_ECONOMY_ARTICLES_SCHEMA, true);
    }

    @DisplayName("문자열을 사용하는 경제 기사들 추가 페이지 접속")
    @Test
    void accessEconomyArticleAddWithString() throws Exception {
        mockMvc.perform(get(ADD_ECONOMY_ARTICLE_WITH_STRING_URL))
                .andExpectAll(status().isOk(),
                        view().name(addStringEconomyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT));
    }

    @DisplayName("문자열을 사용하는 경제 기사들 추가 완료 페이지 접속")
    @Test
    void accessEconomyArticleAddWithStringFinish() throws Exception {
        // given
        EconomyArticle article1 = testEqualDateEconomyArticle;
        EconomyArticle article2 = testNewEconomyArticle;
        EconomyArticleBufferSimple articleBufferOriginal = testEconomyArticleBuffer;
        EconomyArticleBufferSimple articleBufferAddFaultNameDatePress = EconomyArticleBufferSimple.builder()
                .articleBuffer(articleBufferOriginal)
                .nameDatePressString(EconomyArticleBufferSimple.builder().article(testEconomyArticle).build()
                        .getNameDatePressString().replace("2024-", "")).build();

        List<String> nameList = Stream.of(article1, article2)
                .map(EconomyArticle::getName).collect(Collectors.toList());
        String nameListForURL = toStringForUrl(nameList);
        String nameListString = "nameList";

        // then
        for (EconomyArticleBufferSimple articleBuffer : List.of(articleBufferOriginal, articleBufferAddFaultNameDatePress)) {
            ModelMap modelMapPost = requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_ECONOMY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                        put(nameDatePressString, articleBuffer.getNameDatePressString());
                        put(linkString, articleBuffer.getLinkString());
                        put(SUBJECT_COUNTRY, articleBuffer.getSubjectCountry());
                        put(TARGET_ECONOMY_CONTENT, articleBuffer.getTargetEconomyContents());
                    }}))
                    .andExpectAll(status().isFound(),
                            redirectedUrlPattern(ADD_ECONOMY_ARTICLE_WITH_STRING_URL + FINISH_URL + ALL_QUERY_STRING))
                    .andReturn().getModelAndView()).getModelMap();

            assertThat(modelMapPost.get(nameListString)).usingRecursiveComparison().isEqualTo(nameListForURL);
            assertThat(modelMapPost.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(String.valueOf(false));
            assertThat(modelMapPost.get(ERROR_SINGLE)).isEqualTo(null);

            economyArticleService.removeArticleByName(article1.getName());
            economyArticleService.removeArticleByName(article2.getName());
        }

        economyArticleService.registerArticle(article1);
        economyArticleService.registerArticle(article2);

        ModelMap modelMapGet = requireNonNull(mockMvc.perform(getWithMultipleParam(
                        ADD_ECONOMY_ARTICLE_WITH_STRING_URL + FINISH_URL,
                        new HashMap<>() {{
                            put(nameListString, nameListForURL);
                            put(IS_BEAN_VALIDATION_ERROR, String.valueOf(false));
                            put(ERROR_SINGLE, null);
                        }}))
                .andExpectAll(status().isOk(),
                        view().name(ADD_ECONOMY_ARTICLE_VIEW + VIEW_MULTIPLE_FINISH),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_LAYOUT),
                        model().attribute("repeatUrl", ADD_ECONOMY_ARTICLE_WITH_STRING_URL),
                        model().attribute(nameListString, ControllerUtils.decodeWithUTF8(nameList)))
                .andReturn().getModelAndView()).getModelMap();

        assertThat(modelMapGet.get(nameListString)).usingRecursiveComparison().isEqualTo(ControllerUtils.decodeWithUTF8(nameList));
        assertThat(modelMapGet.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(false);
        assertThat(modelMapGet.get(ERROR_SINGLE)).isEqualTo(null);

        assertThat(economyArticleService.findArticleByName(nameList.getFirst()).orElseThrow())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(article1);

        assertThat(economyArticleService.findArticleByName(nameList.getLast()).orElseThrow())
                .usingRecursiveComparison()
                .comparingOnlyFields(fieldNames)
                .isEqualTo(article2);
    }
}
