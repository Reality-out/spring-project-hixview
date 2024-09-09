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
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleBufferSimple;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleDto;
import springsideproject1.springsideproject1build.domain.entity.article.Press;
import springsideproject1.springsideproject1build.domain.entity.company.*;
import springsideproject1.springsideproject1build.domain.service.CompanyArticleService;
import springsideproject1.springsideproject1build.domain.service.CompanyService;
import springsideproject1.springsideproject1build.util.test.CompanyArticleTestUtils;
import springsideproject1.springsideproject1build.util.test.CompanyTestUtils;

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
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.SUBJECT_COMPANY;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_COMPANY_ARTICLE_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_COMPANY_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;
import static springsideproject1.springsideproject1build.util.MainUtils.encodeWithUTF8;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FilterTest implements CompanyArticleTestUtils, CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleService articleService;

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

    @DisplayName("기업 기사 지원 필터 추가에 대한 테스트")
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
            articleService.removeArticleByName(commonName);
        }
    }

    @DisplayName("기업 기사 지원 필터 문자열을 사용하는 추가에 대한 테스트")
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

            articleService.removeArticleByName(article1.getName());
            articleService.removeArticleByName(article2.getName());
        }
    }

    @DisplayName("기업 기사 지원 필터 변경에 대한 테스트")
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
        articleService.registerArticle(beforeModifyArticle);

        // then
        for (CompanyArticleDto articleDto : List.of(articleDtoLeftSpace, articleDtoRightSpace,
                articleDtoKorean, articleDtoLowercase)) {
            mockMvc.perform(postWithCompanyArticleDto(modifyArticleFinishUrl, articleDto))
                    .andExpectAll(status().isFound(),
                            redirectedUrlPattern(modifyArticleFinishUrl + ALL_QUERY_STRING),
                            model().attribute(NAME, encodeWithUTF8(beforeModifyArticle.getName())));
        }
    }

    @DisplayName("기업 지원 필터 추가에 대한 테스트")
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

    @DisplayName("기업 지원 필터 변경에 대한 테스트")
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
