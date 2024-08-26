package springsideproject1.springsideproject1build.domain.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticle;
import springsideproject1.springsideproject1build.domain.entity.article.CompanyArticleDto;
import springsideproject1.springsideproject1build.domain.service.CompanyArticleService;
import springsideproject1.springsideproject1build.domain.service.CompanyService;
import springsideproject1.springsideproject1build.util.test.CompanyArticleTestUtils;
import springsideproject1.springsideproject1build.util.test.CompanyTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.*;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.ARTICLE;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.SUBJECT_COMPANY;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.COMPANY_ARTICLE_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.COMPANY_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.ADD_PROCESS_PATH;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CompanyArticleFieldValidatorTest implements CompanyArticleTestUtils, CompanyTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyArticleService articleService;

    @Autowired
    CompanyService companyService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public CompanyArticleFieldValidatorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, COMPANY_ARTICLE_TABLE, true);
        resetTable(jdbcTemplateTest, COMPANY_TABLE, false);
    }

    @DisplayName("중복 기사명을 사용하는 기업 기사 추가")
    @Test
    public void duplicatedNameCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticle article1 = testArticle;
        String commonName = article1.getName();
        CompanyArticleDto articleDto2 = createTestNewArticleDto();
        articleDto2.setName(commonName);

        // when
        articleService.registerArticle(article1);
        companyService.registerCompany(samsungElectronics);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto2))
                .andExpectAll(view().name(addSingleArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto2);
    }

    @DisplayName("중복 기사 링크를 사용하는 기업 기사 추가")
    @Test
    public void duplicatedLinkCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticle article1 = testArticle;
        String commonLink = article1.getLink();
        CompanyArticleDto articleDto2 = createTestNewArticleDto();
        articleDto2.setLink(commonLink);

        // when
        articleService.registerArticle(article1);
        companyService.registerCompany(samsungElectronics);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto2))
                .andExpectAll(view().name(addSingleArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto2);
    }

    @DisplayName("대상 기업이 등록되지 않은 기업 기사 추가")
    @Test
    public void notRegisteredSubjectCompanyArticleAdd() throws Exception {
        // given & when
        CompanyArticleDto articleDto = createTestArticleDto();

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithCompanyArticleDto(ADD_SINGLE_COMPANY_ARTICLE_URL, articleDto))
                .andExpectAll(view().name(addSingleArticleProcessPage),
                        model().attribute(LAYOUT_PATH, ADD_PROCESS_PATH),
                        model().attribute(ERROR, (String) null))
                .andReturn().getModelAndView()).getModelMap().get(ARTICLE))
                .usingRecursiveComparison()
                .isEqualTo(articleDto);
    }

    @DisplayName("중복 기사명을 사용하는, 문자열을 사용하는 기업 기사들 추가")
    @Test
    public void duplicatedNameCompanyArticleAddWithString() throws Exception {
        // given & when
        articleService.registerArticle(CompanyArticle.builder().article(testArticle).name(testEqualDateArticle.getName()).build());
        companyService.registerCompany(samsungElectronics);

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, testEqualDateArticleStringBuffer.getNameDatePressString());
                    put(SUBJECT_COMPANY, testEqualDateArticleStringBuffer.getSubjectCompany());
                    put(linkString, testEqualDateArticleStringBuffer.getLinkString());
                }}))
                .andExpectAll(view().name(
                                URL_REDIRECT_PREFIX + ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX),
                        model().attribute(IS_BEAN_VALIDATION_ERROR, String.valueOf(false)),
                        model().attribute(ERROR_SINGLE, (String) null)));
    }

    @DisplayName("중복 기사 링크를 사용하는, 문자열을 사용하는 기업 기사들 추가")
    @Test
    public void duplicatedLinkCompanyArticleAddWithString() throws Exception {
        // given & when
        articleService.registerArticle(CompanyArticle.builder().article(testArticle).link(testEqualDateArticle.getLink()).build());
        companyService.registerCompany(samsungElectronics);

        // then
        requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
                    put(nameDatePressString, testEqualDateArticleStringBuffer.getNameDatePressString());
                    put(SUBJECT_COMPANY, testEqualDateArticleStringBuffer.getSubjectCompany());
                    put(linkString, testEqualDateArticleStringBuffer.getLinkString());
                }}))
                .andExpectAll(view().name(
                                URL_REDIRECT_PREFIX + ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX),
                        model().attribute(IS_BEAN_VALIDATION_ERROR, String.valueOf(false)),
                        model().attribute(ERROR_SINGLE, (String) null)));
    }
}
