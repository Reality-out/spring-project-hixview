package site.hixview.web.controller;

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
import site.hixview.domain.entity.article.CompanyArticle;
import site.hixview.domain.entity.article.CompanyArticleBufferSimple;
import site.hixview.domain.entity.article.IndustryArticle;
import site.hixview.domain.entity.article.IndustryArticleBufferSimple;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;
import site.hixview.domain.service.IndustryArticleService;
import site.hixview.util.ControllerUtils;
import site.hixview.util.test.CompanyArticleTestUtils;
import site.hixview.util.test.CompanyTestUtils;
import site.hixview.util.test.IndustryArticleTestUtils;

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
import static site.hixview.domain.vo.manager.RequestURL.ADD_COMPANY_ARTICLE_WITH_STRING_URL;
import static site.hixview.domain.vo.manager.RequestURL.ADD_INDUSTRY_ARTICLE_WITH_STRING_URL;
import static site.hixview.domain.vo.manager.ViewName.ADD_COMPANY_ARTICLE_VIEW;
import static site.hixview.domain.vo.manager.ViewName.ADD_INDUSTRY_ARTICLE_VIEW;
import static site.hixview.domain.vo.name.EntityName.Article.*;
import static site.hixview.domain.vo.name.ExceptionName.IS_BEAN_VALIDATION_ERROR;
import static site.hixview.domain.vo.name.SchemaName.*;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
@AutoConfigureMockMvc
@Transactional
public class ManagerArticleWithStringTest implements CompanyArticleTestUtils, IndustryArticleTestUtils, CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyArticleService companyArticleService;

    @Autowired
    private IndustryArticleService industryArticleService;

    @Autowired
    private CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    ManagerArticleWithStringTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANY_ARTICLES_SCHEMA, true);
        resetTable(jdbcTemplateTest, TEST_INDUSTRY_ARTICLES_SCHEMA, true);
        resetTable(jdbcTemplateTest, TEST_COMPANIES_SCHEMA);
    }

    @DisplayName("문자열을 사용하는 기업 기사들 추가 페이지 접속")
    @Test
    void accessCompanyArticleAddWithString() throws Exception {
        mockMvc.perform(get(ADD_COMPANY_ARTICLE_WITH_STRING_URL))
                .andExpectAll(status().isOk(),
                        view().name(addStringCompanyArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT));
    }

    @DisplayName("문자열을 사용하는 기업 기사들 추가 완료 페이지 접속")
    @Test
    void accessCompanyArticleAddWithStringFinish() throws Exception {
        // given
        CompanyArticle article1 = testEqualDateCompanyArticle;
        CompanyArticle article2 = testNewCompanyArticle;
        CompanyArticleBufferSimple articleBufferOriginal = testCompanyArticleBuffer;
        CompanyArticleBufferSimple articleBufferAddFaultNameDatePress = CompanyArticleBufferSimple.builder()
                .articleBuffer(articleBufferOriginal)
                .nameDatePressString(CompanyArticleBufferSimple.builder().article(testCompanyArticle).build()
                        .getNameDatePressString().replace("2024-", "")).build();

        List<String> nameList = Stream.of(article1, article2)
                .map(CompanyArticle::getName).collect(Collectors.toList());
        String nameListForURL = toStringForUrl(nameList);
        String nameListString = "nameList";

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        for (CompanyArticleBufferSimple articleBuffer : List.of(articleBufferOriginal, articleBufferAddFaultNameDatePress)) {
            ModelMap modelMapPost = requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                        put(nameDatePressString, articleBuffer.getNameDatePressString());
                        put(SUBJECT_COMPANY, articleBuffer.getSubjectCompany());
                        put(linkString, articleBuffer.getLinkString());
                    }}))
                    .andExpectAll(status().isFound(),
                            redirectedUrlPattern(ADD_COMPANY_ARTICLE_WITH_STRING_URL + FINISH_URL + ALL_QUERY_STRING))
                    .andReturn().getModelAndView()).getModelMap();

            assertThat(modelMapPost.get(nameListString)).usingRecursiveComparison().isEqualTo(nameListForURL);
            assertThat(modelMapPost.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(String.valueOf(false));
            assertThat(modelMapPost.get(ERROR_SINGLE)).isEqualTo(null);

            companyArticleService.removeArticleByName(article1.getName());
            companyArticleService.removeArticleByName(article2.getName());
        }

        companyArticleService.registerArticle(article1);
        companyArticleService.registerArticle(article2);

        ModelMap modelMapGet = requireNonNull(mockMvc.perform(getWithMultipleParam(
                        ADD_COMPANY_ARTICLE_WITH_STRING_URL + FINISH_URL,
                        new HashMap<>() {{
                            put(nameListString, nameListForURL);
                            put(IS_BEAN_VALIDATION_ERROR, String.valueOf(false));
                            put(ERROR_SINGLE, null);
                        }}))
                .andExpectAll(status().isOk(),
                        view().name(ADD_COMPANY_ARTICLE_VIEW + "multiple-finish-page"),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_LAYOUT),
                        model().attribute(nameListString, ControllerUtils.decodeWithUTF8(nameList)))
                .andReturn().getModelAndView()).getModelMap();

        assertThat(modelMapGet.get(nameListString)).usingRecursiveComparison().isEqualTo(ControllerUtils.decodeWithUTF8(nameList));
        assertThat(modelMapGet.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(false);
        assertThat(modelMapGet.get(ERROR_SINGLE)).isEqualTo(null);

        assertThat(companyArticleService.findArticleByName(nameList.getFirst()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article1);

        assertThat(companyArticleService.findArticleByName(nameList.getLast()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article2);
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
                        put(SUBJECT_SECOND_CATEGORY, articleBuffer.getSubjectSecondCategory());
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
                        view().name(ADD_INDUSTRY_ARTICLE_VIEW + "multiple-finish-page"),
                        model().attribute(LAYOUT_PATH, ADD_FINISH_LAYOUT),
                        model().attribute(nameListString, ControllerUtils.decodeWithUTF8(nameList)))
                .andReturn().getModelAndView()).getModelMap();

        assertThat(modelMapGet.get(nameListString)).usingRecursiveComparison().isEqualTo(ControllerUtils.decodeWithUTF8(nameList));
        assertThat(modelMapGet.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(false);
        assertThat(modelMapGet.get(ERROR_SINGLE)).isEqualTo(null);

        assertThat(industryArticleService.findArticleByName(nameList.getFirst()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article1);

        assertThat(industryArticleService.findArticleByName(nameList.getLast()).orElseThrow())
                .usingRecursiveComparison()
                .ignoringFields(NUMBER)
                .isEqualTo(article2);
    }
}
