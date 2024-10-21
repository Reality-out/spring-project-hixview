package site.hixview.web.filter.nomock;

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
import site.hixview.domain.entity.article.*;
import site.hixview.domain.service.*;
import site.hixview.support.property.TestSchemaName;
import site.hixview.support.util.ArticleMainTestUtils;
import site.hixview.support.util.CompanyArticleTestUtils;
import site.hixview.support.util.CompanyTestUtils;
import site.hixview.support.util.IndustryArticleTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.ERROR_SINGLE;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.name.EntityName.Article.*;
import static site.hixview.domain.vo.name.ExceptionName.IS_BEAN_VALIDATION_ERROR;
import static site.hixview.support.util.EconomyArticleTestUtils.*;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
@AutoConfigureMockMvc
@TestSchemaName
@Transactional
class FilterTest implements CompanyArticleTestUtils, IndustryArticleTestUtils, ArticleMainTestUtils, CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyArticleService companyArticleService;

    @Autowired
    private IndustryArticleService industryArticleService;

    @Autowired
    private EconomyArticleService economyArticleService;

    @Autowired
    private ArticleMainService articleMainService;

    @Autowired
    private CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    FilterTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANY_ARTICLES_SCHEMA, true);
        resetTable(jdbcTemplateTest, TEST_COMPANIES_SCHEMA);
    }

    @DisplayName("문자열을 사용하는 기업 기사 추가에 대한 기사 지원 필터 테스트")
    @Test
    void companyArticleDtoSupportFilterAddWithStringTest() throws Exception {
        // given
        CompanyArticle article1 = testEqualDateCompanyArticle;
        CompanyArticle article2 = testNewCompanyArticle;
        CompanyArticleBufferSimple articleBuffer = testCompanyArticleBuffer;

        List<String> nameList = Stream.of(article1, article2)
                .map(CompanyArticle::getName).collect(Collectors.toList());
        String nameListForURL = toStringForUrl(nameList);
        String nameListString = "nameList";

        String articleStringLeftSpace = articleBuffer.getNameDatePressString()
                .replace(article1.getName(), " " + article1.getName());
        String articleStringRightSpace = articleBuffer.getNameDatePressString()
                .replace(article1.getName(), article1.getName() + " ");
        String articleStringKorean = testCompanyArticleBuffer.getNameDatePressString()
                .replace(article1.getPress().name(), article1.getPress().getValue())
                .replace(article2.getPress().name(), article2.getPress().getValue());
        String articleStringLowercase = testCompanyArticleBuffer.getNameDatePressString()
                .replace(article1.getPress().name(), article1.getPress().name().toLowerCase())
                .replace(article2.getPress().name(), article2.getPress().name().toLowerCase());

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        for (String articleString : List.of(articleStringLeftSpace, articleStringRightSpace,
                articleStringKorean, articleStringLowercase)) {
            ModelMap modelMapPost = requireNonNull(mockMvc.perform(postWithMultipleParams(
                            ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                                put(nameDatePressString, articleString);
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
    }

    @DisplayName("문자열을 사용하는 산업 기사 추가에 대한 기사 지원 필터 테스트")
    @Test
    void industryArticleDtoSupportFilterAddWithStringTest() throws Exception {
        // given & when
        IndustryArticle article1 = testEqualDateIndustryArticle;
        IndustryArticle article2 = testNewIndustryArticle;
        IndustryArticleBufferSimple articleBuffer = testIndustryArticleBuffer;

        List<String> nameList = Stream.of(article1, article2)
                .map(IndustryArticle::getName).collect(Collectors.toList());
        String nameListForURL = toStringForUrl(nameList);
        String nameListString = "nameList";

        String articleStringLeftSpace = articleBuffer.getNameDatePressString()
                .replace(article1.getName(), " " + article1.getName());
        String articleStringRightSpace = articleBuffer.getNameDatePressString()
                .replace(article1.getName(), article1.getName() + " ");
        String articleStringKorean = testIndustryArticleBuffer.getNameDatePressString()
                .replace(article1.getPress().name(), article1.getPress().getValue())
                .replace(article2.getPress().name(), article2.getPress().getValue());
        String articleStringLowercase = testIndustryArticleBuffer.getNameDatePressString()
                .replace(article1.getPress().name(), article1.getPress().name().toLowerCase())
                .replace(article2.getPress().name(), article2.getPress().name().toLowerCase());

        // then
        for (String articleString : List.of(articleStringLeftSpace, articleStringRightSpace,
                articleStringKorean, articleStringLowercase)) {
            ModelMap modelMapPost = requireNonNull(mockMvc.perform(postWithMultipleParams(
                            ADD_INDUSTRY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                                put(nameDatePressString, articleString);
                                put(linkString, articleBuffer.getLinkString());
                                put(SUBJECT_FIRST_CATEGORY, articleBuffer.getSubjectFirstCategory());
                                put(SUBJECT_SECOND_CATEGORY, articleBuffer.getSubjectSecondCategories());
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
    }

    @DisplayName("문자열을 사용하는 경제 기사 추가에 대한 기사 지원 필터 테스트")
    @Test
    void economyArticleDtoSupportFilterAddWithStringTest() throws Exception {
        // given & when
        EconomyArticle article1 = testEqualDateEconomyArticle;
        EconomyArticle article2 = testNewEconomyArticle;
        EconomyArticleBufferSimple articleBuffer = testEconomyArticleBuffer;

        List<String> nameList = Stream.of(article1, article2)
                .map(EconomyArticle::getName).collect(Collectors.toList());
        String nameListForURL = toStringForUrl(nameList);
        String nameListString = "nameList";

        String articleStringLeftSpace = articleBuffer.getNameDatePressString()
                .replace(article1.getName(), " " + article1.getName());
        String articleStringRightSpace = articleBuffer.getNameDatePressString()
                .replace(article1.getName(), article1.getName() + " ");
        String articleStringKorean = testEconomyArticleBuffer.getNameDatePressString()
                .replace(article1.getPress().name(), article1.getPress().getValue())
                .replace(article2.getPress().name(), article2.getPress().getValue());
        String articleStringLowercase = testEconomyArticleBuffer.getNameDatePressString()
                .replace(article1.getPress().name(), article1.getPress().name().toLowerCase())
                .replace(article2.getPress().name(), article2.getPress().name().toLowerCase());

        // then
        for (String articleString : List.of(articleStringLeftSpace, articleStringRightSpace,
                articleStringKorean, articleStringLowercase)) {
            ModelMap modelMapPost = requireNonNull(mockMvc.perform(postWithMultipleParams(
                            ADD_ECONOMY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                                put(nameDatePressString, articleString);
                                put(linkString, articleBuffer.getLinkString());
                                put(SUBJECT_COUNTRY, articleBuffer.getSubjectCountry());
                                put(TARGET_ECONOMY_CONTENTS, articleBuffer.getTargetEconomyContents());
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
    }
}