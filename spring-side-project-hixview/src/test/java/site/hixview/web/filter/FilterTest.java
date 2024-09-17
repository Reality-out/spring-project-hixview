package site.hixview.web.filter;

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
import site.hixview.domain.entity.*;
import site.hixview.domain.entity.article.*;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.entity.company.CompanyDto;
import site.hixview.domain.service.ArticleMainService;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.CompanyService;
import site.hixview.domain.service.IndustryArticleService;
import site.hixview.util.test.ArticleMainTestUtils;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.ERROR_SINGLE;
import static site.hixview.domain.vo.Word.NAME;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.name.EntityName.Article.*;
import static site.hixview.domain.vo.name.ExceptionName.IS_BEAN_VALIDATION_ERROR;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANIES_SCHEMA;
import static site.hixview.domain.vo.name.SchemaName.TEST_COMPANY_ARTICLES_SCHEMA;

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
        resetTable(jdbcTemplateTest, TEST_COMPANY_ARTICLES_SCHEMA, true);
        resetTable(jdbcTemplateTest, TEST_COMPANIES_SCHEMA);
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

        String commonName = createTestCompanyArticleDto().getName();
        String redirectedURL = fromPath(ADD_SINGLE_COMPANY_ARTICLE_URL + FINISH_URL).queryParam(NAME, commonName).build().toUriString();

        // when
        companyService.registerCompany(samsungElectronics);

        // then
        for (CompanyArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace, articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));
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
                            redirectedUrlPattern(ADD_COMPANY_ARTICLE_WITH_STRING_URL + FINISH_URL + ALL_QUERY_STRING))
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

        String redirectedURL = fromPath(UPDATE_COMPANY_ARTICLE_URL + FINISH_URL).queryParam(NAME, article.getName()).build().toUriString();

        // when
        companyService.registerCompany(samsungElectronics);
        companyArticleService.registerArticle(beforeModifyArticle);

        // then
        for (CompanyArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace, articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithCompanyArticleDto(modifyCompanyArticleFinishUrl, articleDto))
                    .andExpectAll(status().isFound(), redirectedUrlPattern(redirectedURL));
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

        String redirectedURL = fromPath(ADD_SINGLE_INDUSTRY_ARTICLE_URL + FINISH_URL).queryParam(NAME, createTestIndustryArticleDto().getName()).build().toUriString();

        // then
        for (IndustryArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace,
                articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithIndustryArticleDto(ADD_SINGLE_INDUSTRY_ARTICLE_URL, articleDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));
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
                            redirectedUrlPattern(ADD_INDUSTRY_ARTICLE_WITH_STRING_URL + FINISH_URL + ALL_QUERY_STRING))
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

        String redirectedURL = fromPath(UPDATE_INDUSTRY_ARTICLE_URL + FINISH_URL).queryParam(NAME, article.getName()).build().toUriString();

        // when
        industryArticleService.registerArticle(beforeModifyArticle);

        // then
        for (IndustryArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace,
                articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithIndustryArticleDto(modifyIndustryArticleFinishUrl, articleDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));
        }
    }

    @DisplayName("추가에 대한 기사 메인 지원 필터 테스트")
    @Test
    public void articleMainDtoSupportFilterAddTest() throws Exception {
        // given & when
        ArticleMainDto articleDtoOriginal = createTestArticleMainDto();
        ArticleMainDto articleDtoLeftSpace = createTestArticleMainDto();
        articleDtoLeftSpace.setName(" " + articleDtoLeftSpace.getName());
        ArticleMainDto articleDtoRightSpace = createTestArticleMainDto();
        articleDtoRightSpace.setName(articleDtoRightSpace.getName() + " ");
        ArticleMainDto articleDtoKoreanArticleClassName = createTestArticleMainDto();
        articleDtoKoreanArticleClassName.setArticleClassName(testCompanyArticleMain.getArticleClassName().getArticleClassNameValue());
        ArticleMainDto articleDtoLowerCase = createTestArticleMainDto();
        articleDtoLowerCase.setArticleClassName(articleDtoLowerCase.getArticleClassName().toLowerCase());

        String redirectedURL = fromPath(ADD_ARTICLE_MAIN_URL + FINISH_URL).queryParam(NAME, articleDtoOriginal.getName())
                .build().toUriString();

        // then
        for (ArticleMainDto articleDto : List.of(
                articleDtoLeftSpace, articleDtoRightSpace, articleDtoKoreanArticleClassName, articleDtoLowerCase)){
            mockMvc.perform(postWithArticleMainDto(ADD_ARTICLE_MAIN_URL, articleDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

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

        String redirectedURL = fromPath(UPDATE_ARTICLE_MAIN_URL + FINISH_URL).queryParam(NAME, article.getName()).build().toUriString();

        // when
        articleMainService.registerArticle(beforeModifyArticle);

        // then
        for (ArticleMainDto articleDto : List.of(
                articleDtoLeftSpace, articleDtoRightSpace, articleDtoKoreanArticleClassName, articleDtoLowerCase)) {
            mockMvc.perform(postWithArticleMainDto(modifyArticleMainFinishUrl, articleDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));
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

        String redirectedURL = fromPath(ADD_SINGLE_COMPANY_URL + FINISH_URL).queryParam(NAME, createSamsungElectronicsDto().getName()).build().toUriString();

        // then
        for (CompanyDto companyDto : List.of(companyDtoKorean, companyDtoLowercase)) {
            mockMvc.perform(postWithCompanyDto(ADD_SINGLE_COMPANY_URL, companyDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));

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

        String redirectedURL = fromPath(modifyCompanyFinishUrl).queryParam(NAME, createSamsungElectronicsDto().getName()).build().toUriString();

        // when
        companyService.registerCompany(beforeModifyCompany);
        String commonName = beforeModifyCompany.getName();

        // then
        for (CompanyDto companyDto : List.of(companyDtoKorean, companyDtoLowercase)) {
            mockMvc.perform(postWithCompanyDto(modifyCompanyFinishUrl, companyDto))
                    .andExpectAll(status().isFound(), redirectedUrl(redirectedURL));
        }
    }
}
