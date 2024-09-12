package springsideproject1.springsideproject1build.web.filter;

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
import springsideproject1.springsideproject1build.domain.entity.*;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMain;
import springsideproject1.springsideproject1build.domain.entity.article.ArticleMainDto;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleBufferSimple;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleDto;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticle;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticleBufferSimple;
import springsideproject1.springsideproject1build.domain.entity.article.IndustryArticleDto;
import springsideproject1.springsideproject1build.domain.entity.company.*;
import springsideproject1.springsideproject1build.domain.service.ArticleMainService;
import springsideproject1.springsideproject1build.domain.service.CompanyArticleService;
import springsideproject1.springsideproject1build.domain.service.CompanyService;
import springsideproject1.springsideproject1build.domain.service.IndustryArticleService;
import springsideproject1.springsideproject1build.util.test.ArticleMainTestUtils;
import springsideproject1.springsideproject1build.util.test.CompanyArticleTestUtils;
import springsideproject1.springsideproject1build.util.test.CompanyTestUtils;
import springsideproject1.springsideproject1build.util.test.IndustryArticleTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.ERROR_SINGLE;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.IS_BEAN_VALIDATION_ERROR;
import static springsideproject1.springsideproject1build.domain.vo.CLASS.*;
import static springsideproject1.springsideproject1build.domain.vo.DATABASE.TEST_COMPANY_ARTICLE_TABLE;
import static springsideproject1.springsideproject1build.domain.vo.DATABASE.TEST_COMPANY_TABLE;
import static springsideproject1.springsideproject1build.domain.vo.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.vo.WORD.NAME;
import static springsideproject1.springsideproject1build.util.ControllerUtils.encodeWithUTF8;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FilterTest implements CompanyArticleTestUtils, IndustryArticleTestUtils, ArticleMainTestUtils, CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleService companyArticleService;

    @Autowired
    IndustryArticleService industryArticleService;

    @Autowired
    ArticleMainService articleMainService;

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public FilterTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_COMPANY_ARTICLE_TABLE, true);
        resetTable(jdbcTemplateTest, TEST_COMPANY_TABLE);
    }

    @DisplayName("기업 기사 추가에 대한 기사 지원 필터 테스트")
    @Test
    public void companyArticleDtoSupportFilterAddTest() throws Exception {
        // given
        CompanyArticleDto articleDtoLeftSpace = createTestCompanyArticleDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        CompanyArticleDto articleDtoRightSpace = createTestCompanyArticleDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        CompanyArticleDto articleDtoKorean = createTestCompanyArticleDto();
        articleDtoKorean.setPress(Press.valueOf(articleDtoKorean.getPress()).getPressValue());
        CompanyArticleDto articleDtoLowercase = createTestCompanyArticleDto();
        articleDtoLowercase.setPress(Press.valueOf(articleDtoLowercase.getPress()).name().toLowerCase());

        // when
        companyService.registerCompany(samsungElectronics);
        String commonName = createTestCompanyArticleDto().getName();

        // then
        for (CompanyArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace,
                articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                    .andExpectAll(status().isFound(),
                            redirectedUrlPattern(ADD_SINGLE_COMPANY_ARTICLE_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                            model().attribute(NAME, encodeWithUTF8(commonName)));
            companyArticleService.removeArticleByName(commonName);
        }
    }

    @DisplayName("문자열을 사용하는 기업 기사 추가에 대한 기사 지원 필터 테스트")
    @Test
    public void companyArticleDtoSupportFilterAddWithStringTest() throws Exception {
        // given
        CompanyArticle article1 = testEqualDateCompanyArticle;
        CompanyArticle article2 = testNewCompanyArticle;
        CompanyArticleBufferSimple articleBuffer = testCompanyArticleStringBuffer;

        List<String> nameList = Stream.of(article1, article2)
                .map(CompanyArticle::getName).collect(Collectors.toList());
        String nameListForURL = toStringForUrl(nameList);
        String nameListString = "nameList";

        String articleStringLeftSpace = articleBuffer.getNameDatePressString()
                .replace(article1.getName(), " " + article1.getName());
        String articleStringRightSpace = articleBuffer.getNameDatePressString()
                .replace(article1.getName(), article1.getName() + " ");
        String articleStringKorean = testCompanyArticleStringBuffer.getNameDatePressString()
                .replace(article1.getPress().name(), article1.getPress().getPressValue())
                .replace(article2.getPress().name(), article2.getPress().getPressValue());
        String articleStringLowercase = testCompanyArticleStringBuffer.getNameDatePressString()
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
                            redirectedUrlPattern(ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING))
                    .andReturn().getModelAndView()).getModelMap();

            assertThat(modelMapPost.get(nameListString)).usingRecursiveComparison().isEqualTo(nameListForURL);
            assertThat(modelMapPost.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(String.valueOf(false));
            assertThat(modelMapPost.get(ERROR_SINGLE)).isEqualTo(null);

            companyArticleService.removeArticleByName(article1.getName());
            companyArticleService.removeArticleByName(article2.getName());
        }
    }

    @DisplayName("기업 기사 변경에 대한 기사 지원 필터 테스트")
    @Test
    public void companyArticleDtoSupportFilterModifyTest() throws Exception {
        // given
        CompanyArticle beforeModifyArticle = testCompanyArticle;
        CompanyArticle article = CompanyArticle.builder().article(testNewCompanyArticle)
                .name(beforeModifyArticle.getName()).link(beforeModifyArticle.getLink()).build();
        CompanyArticleDto articleDtoLeftSpace = article.toDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        CompanyArticleDto articleDtoRightSpace = article.toDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        CompanyArticleDto articleDtoKorean = article.toDto();
        articleDtoKorean.setPress(Press.valueOf(articleDtoKorean.getPress()).getPressValue());
        CompanyArticleDto articleDtoLowercase = article.toDto();
        articleDtoLowercase.setPress(Press.valueOf(articleDtoLowercase.getPress()).name().toLowerCase());

        // when
        companyService.registerCompany(samsungElectronics);
        companyArticleService.registerArticle(beforeModifyArticle);

        // then
        for (CompanyArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace,
                articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto))
                    .andExpectAll(status().isFound(),
                            redirectedUrlPattern(modifyCompanyArticleFinishUrl + ALL_QUERY_STRING),
                            model().attribute(NAME, encodeWithUTF8(beforeModifyArticle.getName())));
        }
    }

    @DisplayName("산업 기사 추가에 대한 기사 지원 필터 테스트")
    @Test
    public void industryArticleDtoSupportFilterAddTest() throws Exception {
        // given & when
        IndustryArticleDto articleDtoLeftSpace = createTestIndustryArticleDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        IndustryArticleDto articleDtoRightSpace = createTestIndustryArticleDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        IndustryArticleDto articleDtoKorean = createTestIndustryArticleDto();
        articleDtoKorean.setPress(Press.valueOf(articleDtoKorean.getPress()).getPressValue());
        IndustryArticleDto articleDtoLowercase = createTestIndustryArticleDto();
        articleDtoLowercase.setPress(Press.valueOf(articleDtoLowercase.getPress()).name().toLowerCase());
        String commonName = createTestIndustryArticleDto().getName();

        // then
        for (IndustryArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace,
                articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                    .andExpectAll(status().isFound(),
                            redirectedUrlPattern(ADD_SINGLE_INDUSTRY_ARTICLE_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                            model().attribute(NAME, encodeWithUTF8(commonName)));
            industryArticleService.removeArticleByName(commonName);
        }
    }

    @DisplayName("문자열을 사용하는 산업 기사 추가에 대한 기사 지원 필터 테스트")
    @Test
    public void industryArticleDtoSupportFilterAddWithStringTest() throws Exception {
        // given & when
        IndustryArticle article1 = testEqualDateIndustryArticle;
        IndustryArticle article2 = testNewIndustryArticle;
        IndustryArticleBufferSimple articleBuffer = testIndustryArticleStringBuffer;

        List<String> nameList = Stream.of(article1, article2)
                .map(IndustryArticle::getName).collect(Collectors.toList());
        String nameListForURL = toStringForUrl(nameList);
        String nameListString = "nameList";

        String articleStringLeftSpace = articleBuffer.getNameDatePressString()
                .replace(article1.getName(), " " + article1.getName());
        String articleStringRightSpace = articleBuffer.getNameDatePressString()
                .replace(article1.getName(), article1.getName() + " ");
        String articleStringKorean = testIndustryArticleStringBuffer.getNameDatePressString()
                .replace(article1.getPress().name(), article1.getPress().getPressValue())
                .replace(article2.getPress().name(), article2.getPress().getPressValue());
        String articleStringLowercase = testIndustryArticleStringBuffer.getNameDatePressString()
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
                                put(SUBJECT_SECOND_CATEGORY, articleBuffer.getSubjectSecondCategory());
                            }}))
                    .andExpectAll(status().isFound(),
                            redirectedUrlPattern(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING))
                    .andReturn().getModelAndView()).getModelMap();

            assertThat(modelMapPost.get(nameListString)).usingRecursiveComparison().isEqualTo(nameListForURL);
            assertThat(modelMapPost.get(IS_BEAN_VALIDATION_ERROR)).isEqualTo(String.valueOf(false));
            assertThat(modelMapPost.get(ERROR_SINGLE)).isEqualTo(null);

            industryArticleService.removeArticleByName(article1.getName());
            industryArticleService.removeArticleByName(article2.getName());
        }
    }

    @DisplayName("산업 기사 변경에 대한 기사 지원 필터 테스트")
    @Test
    public void industryArticleDtoSupportFilterModifyTest() throws Exception {
        // given
        IndustryArticle beforeModifyArticle = testIndustryArticle;
        IndustryArticle article = IndustryArticle.builder().article(testNewIndustryArticle)
                .name(beforeModifyArticle.getName()).link(beforeModifyArticle.getLink()).build();
        IndustryArticleDto articleDtoLeftSpace = article.toDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        IndustryArticleDto articleDtoRightSpace = article.toDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        IndustryArticleDto articleDtoKorean = article.toDto();
        articleDtoKorean.setPress(Press.valueOf(articleDtoKorean.getPress()).getPressValue());
        IndustryArticleDto articleDtoLowercase = article.toDto();
        articleDtoLowercase.setPress(Press.valueOf(articleDtoLowercase.getPress()).name().toLowerCase());

        // when
        industryArticleService.registerArticle(beforeModifyArticle);

        // then
        for (IndustryArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace,
                articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                    .andExpectAll(status().isFound(),
                            redirectedUrlPattern(modifyIndustryArticleFinishUrl + ALL_QUERY_STRING),
                            model().attribute(NAME, encodeWithUTF8(beforeModifyArticle.getName())));
        }
    }

    @DisplayName("추가에 대한 기사 메인 지원 필터 테스트")
    @Test
    public void articleMainDtoSupportFilterAddTest() throws Exception {
        ArticleMainDto articleDtoOriginal = createTestArticleMainDto();
        ArticleMainDto articleDtoLeftSpace = createTestArticleMainDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        ArticleMainDto articleDtoRightSpace = createTestArticleMainDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        ArticleMainDto articleDtoKoreanArticleClassName = createTestArticleMainDto();
        articleDtoKoreanArticleClassName.setArticleClassName(testCompanyArticleMain.getArticleClassName().getArticleClassNameValue());
        ArticleMainDto articleDtoLowerCase = createTestArticleMainDto();
        articleDtoLowerCase.setArticleClassName(articleDtoLowerCase.getArticleClassName().toLowerCase());

        for (ArticleMainDto articleDto : List.of(
                articleDtoLeftSpace, articleDtoRightSpace, articleDtoKoreanArticleClassName, articleDtoLowerCase)){
            mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                    .andExpectAll(status().isFound(),
                            redirectedUrlPattern(ADD_ARTICLE_MAIN_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                            model().attribute(NAME, encodeWithUTF8(articleDtoOriginal.getName())));

            articleMainService.removeArticleByName(articleDtoOriginal.getName());
        }
    }

    @DisplayName("변경에 대한 기사 메인 지원 필터 테스트")
    @Test
    public void articleMainDtoSupportFilterModifyTest() throws Exception {
        // given
        ArticleMain beforeModifyArticle = testCompanyArticleMain;
        ArticleMain article = ArticleMain.builder().article(testNewCompanyArticleMain).name(beforeModifyArticle.getName()).build();
        ArticleMainDto articleDtoLeftSpace = article.toDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        ArticleMainDto articleDtoRightSpace = article.toDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        ArticleMainDto articleDtoKoreanArticleClassName = createTestArticleMainDto();
        articleDtoKoreanArticleClassName.setArticleClassName(testCompanyArticleMain.getArticleClassName().getArticleClassNameValue());
        ArticleMainDto articleDtoLowerCase = createTestArticleMainDto();
        articleDtoLowerCase.setArticleClassName(articleDtoLowerCase.getArticleClassName().toLowerCase());

        // when
        articleMainService.registerArticle(beforeModifyArticle);

        // then
        for (ArticleMainDto articleDto : List.of(
                articleDtoLeftSpace, articleDtoRightSpace, articleDtoKoreanArticleClassName, articleDtoLowerCase)) {
            mockMvc.perform(postWithArticleMainDto(modifyArticleMainFinishUrl, articleDto))
                    .andExpectAll(status().isFound(),
                            redirectedUrlPattern(modifyArticleMainFinishUrl + ALL_QUERY_STRING),
                            model().attribute(NAME, encodeWithUTF8(beforeModifyArticle.getName())));
        }
    }

    @DisplayName("추가에 대한 기업 지원 필터 테스트")
    @Test
    public void companyDtoSupportFilterAddTest() throws Exception {
        // given & when
        CompanyDto companyDtoKorean = createSamsungElectronicsDto();
        companyDtoKorean.setCountry(Country.valueOf(companyDtoKorean.getCountry()).getCountryValue());
        companyDtoKorean.setScale(Scale.valueOf(companyDtoKorean.getScale()).getScaleValue());
        companyDtoKorean.setFirstCategory(FirstCategory.valueOf(companyDtoKorean.getFirstCategory()).getFirstCategoryValue());
        companyDtoKorean.setSecondCategory(SecondCategory.valueOf(companyDtoKorean.getSecondCategory()).getSecondCategoryValue());

        CompanyDto companyDtoLowercase = createSamsungElectronicsDto();
        companyDtoLowercase.setCountry(Country.valueOf(companyDtoLowercase.getCountry()).name().toLowerCase());
        companyDtoLowercase.setScale(Scale.valueOf(companyDtoLowercase.getScale()).name().toLowerCase());
        companyDtoLowercase.setFirstCategory(FirstCategory.valueOf(companyDtoLowercase.getFirstCategory()).name().toLowerCase());
        companyDtoLowercase.setSecondCategory(SecondCategory.valueOf(companyDtoLowercase.getSecondCategory()).name().toLowerCase());

        // then
        for (CompanyDto companyDto : List.of(companyDtoKorean, companyDtoLowercase)) {
            mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                    .andExpectAll(status().isFound(),
                            redirectedUrlPattern(ADD_SINGLE_COMPANY_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                            model().attribute(NAME, encodeWithUTF8(companyDto.getName())));

            companyService.removeCompanyByCode(companyDto.getCode());
        }
    }

    @DisplayName("변경에 대한 기업 지원 필터 테스트")
    @Test
    public void companyDtoSupportFilterModifyTest() throws Exception {
        // given
        Company beforeModifyCompany = samsungElectronics;
        Company company = Company.builder().company(skHynix)
                .name(beforeModifyCompany.getName()).code(beforeModifyCompany.getCode()).build();

        CompanyDto companyDtoKorean = company.toDto();
        companyDtoKorean.setCountry(Country.valueOf(companyDtoKorean.getCountry()).getCountryValue());
        companyDtoKorean.setScale(Scale.valueOf(companyDtoKorean.getScale()).getScaleValue());
        companyDtoKorean.setFirstCategory(FirstCategory.valueOf(companyDtoKorean.getFirstCategory()).getFirstCategoryValue());
        companyDtoKorean.setSecondCategory(SecondCategory.valueOf(companyDtoKorean.getSecondCategory()).getSecondCategoryValue());

        CompanyDto companyDtoLowercase = company.toDto();
        companyDtoLowercase.setCountry(Country.valueOf(companyDtoLowercase.getCountry()).name().toLowerCase());
        companyDtoLowercase.setScale(Scale.valueOf(companyDtoLowercase.getScale()).name().toLowerCase());
        companyDtoLowercase.setFirstCategory(FirstCategory.valueOf(companyDtoLowercase.getFirstCategory()).name().toLowerCase());
        companyDtoLowercase.setSecondCategory(SecondCategory.valueOf(companyDtoLowercase.getSecondCategory()).name().toLowerCase());

        // when
        companyService.registerCompany(beforeModifyCompany);
        String commonName = beforeModifyCompany.getName();

        // then
        for (CompanyDto companyDto : List.of(companyDtoKorean, companyDtoLowercase)) {
            mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                    .andExpectAll(status().isFound(),
                            redirectedUrlPattern(modifyCompanyFinishUrl + ALL_QUERY_STRING),
                            model().attribute(NAME, encodeWithUTF8(commonName)));
        }
    }
}
